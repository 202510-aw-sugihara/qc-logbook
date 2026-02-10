package com.example.qclogbook.service;

import com.example.qclogbook.domain.entity.Inspection;
import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.repository.InspectionRepository;
import com.example.qclogbook.service.dto.InspectionCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Inspection作成を行うサービス。
 */
@Service
public class InspectionService {

    private static final int DEFAULT_TOLERANCE_C = 5;
    private static final BigDecimal DEFAULT_TARGET_VOLTAGE_V = new BigDecimal("12.00");
    private static final int DEFAULT_WINDOW_BEFORE_MS = 60000;
    private static final int DEFAULT_WINDOW_AFTER_MS = 60000;

    private final InspectionRepository inspectionRepository;

    public InspectionService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Transactional
    public Inspection createInspection(InspectionCreateRequest req) {
        Inspection inspection = Inspection.create(req.getProductName(), req.getLotNo(), req.getCreatedBy());

        for (SoakSessionDefaults.Default def : SoakSessionDefaults.defaults()) {
            SoakSession session = SoakSession.createDefault(
                inspection,
                def.envType(),
                def.targetTempC(),
                DEFAULT_TOLERANCE_C,
                DEFAULT_TARGET_VOLTAGE_V,
                DEFAULT_WINDOW_BEFORE_MS,
                DEFAULT_WINDOW_AFTER_MS
            );
            inspection.getSessions().add(session);
        }

        return inspectionRepository.save(inspection);
    }
}