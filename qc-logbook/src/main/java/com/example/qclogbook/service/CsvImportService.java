package com.example.qclogbook.service;

import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.domain.entity.TempPoint;
import com.example.qclogbook.repository.SoakSessionRepository;
import com.example.qclogbook.repository.TempPointRepository;
import com.example.qclogbook.service.dto.CsvImportResult;
import com.example.qclogbook.service.exception.CsvAllSkippedException;
import com.example.qclogbook.service.exception.CsvDuplicateOffsetException;
import com.example.qclogbook.service.exception.CsvEmptyFileException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV取込を行うサービス。
 */
@Service
public class CsvImportService {

    private static final int MIN_OFFSET_MS = -60000;
    private static final int MAX_OFFSET_MS = 60000;

    private final SoakSessionRepository soakSessionRepository;
    private final TempPointRepository tempPointRepository;
    private final SoakSessionService soakSessionService;
    private final CsvTempPointParser csvTempPointParser;

    public CsvImportService(
        SoakSessionRepository soakSessionRepository,
        TempPointRepository tempPointRepository,
        SoakSessionService soakSessionService,
        CsvTempPointParser csvTempPointParser
    ) {
        this.soakSessionRepository = soakSessionRepository;
        this.tempPointRepository = tempPointRepository;
        this.soakSessionService = soakSessionService;
        this.csvTempPointParser = csvTempPointParser;
    }

    @Transactional
    public CsvImportResult importTempPoints(Long sessionId, MultipartFile file) {
        SoakSession session = soakSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("SoakSession not found: " + sessionId));

        CsvTempPointParser.ParseResult parsed = parse(file);

        if (parsed.totalLines() == 0) {
            throw new CsvEmptyFileException("CSV file is empty");
        }

        List<TempPoint> validPoints = new ArrayList<>();
        int skipped = parsed.skippedLines();

        for (TempPoint point : parsed.points()) {
            Integer offsetMs = point.getOffsetMs();
            if (offsetMs == null || offsetMs < MIN_OFFSET_MS || offsetMs > MAX_OFFSET_MS) {
                skipped++;
                continue;
            }
            point.assignSession(session);
            validPoints.add(point);
        }

        if (validPoints.isEmpty()) {
            throw new CsvAllSkippedException("All lines were skipped");
        }

        try {
            tempPointRepository.saveAll(validPoints);
        } catch (DataIntegrityViolationException ex) {
            throw new CsvDuplicateOffsetException("Duplicate offsetMs in the same session", ex);
        }

        soakSessionService.recomputeSession(sessionId);

        return new CsvImportResult(
            sessionId,
            parsed.totalLines(),
            validPoints.size(),
            skipped,
            null,
            0
        );
    }

    private CsvTempPointParser.ParseResult parse(MultipartFile file) {
        try {
            return csvTempPointParser.parse(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV", e);
        }
    }
}