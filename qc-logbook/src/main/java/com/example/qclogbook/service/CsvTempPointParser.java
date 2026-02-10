package com.example.qclogbook.service;

import com.example.qclogbook.domain.entity.TempPoint;
import com.example.qclogbook.service.exception.CsvInvalidHeaderException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV（offsetMs,tempC）を安全にパースするヘルパー。
 */
@Service
public class CsvTempPointParser {

    /**
     * パース結果。
     *
     * @param points 読み取れた温度点
     * @param skippedLines スキップした行数
     * @param totalLines 対象行数（空行・ヘッダ除外）
     */
    public record ParseResult(List<TempPoint> points, int skippedLines, int totalLines) {
    }

    public ParseResult parse(Reader reader) throws IOException {
        List<TempPoint> points = new ArrayList<>();
        int skipped = 0;
        int total = 0;
        boolean headerChecked = false;

        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                if (!headerChecked) {
                    headerChecked = true;
                    if (isHeader(trimmed)) {
                        continue;
                    }
                    if (isLikelyHeader(trimmed)) {
                        throw new CsvInvalidHeaderException("Invalid CSV header: " + trimmed);
                    }
                }

                total++;

                String[] parts = trimmed.split(",", -1);
                if (parts.length != 2) {
                    skipped++;
                    continue;
                }

                try {
                    int offsetMs = Integer.parseInt(parts[0].trim());
                    BigDecimal tempC = new BigDecimal(parts[1].trim());
                    points.add(TempPoint.create(offsetMs, tempC));
                } catch (NumberFormatException ex) {
                    skipped++;
                }
            }
        }

        return new ParseResult(points, skipped, total);
    }

    private boolean isHeader(String line) {
        return "offsetMs,tempC".equalsIgnoreCase(line.replace(" ", ""));
    }

    private boolean isLikelyHeader(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (Character.isLetter(line.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
