package com.example.qclogbook.service;

import com.example.qclogbook.domain.entity.Inspection;
import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.domain.enums.Judgement;
import com.example.qclogbook.repository.SoakSessionRepository;
import com.example.qclogbook.repository.TempPointRepository;
import com.example.qclogbook.service.dto.InspectionCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CsvImportServiceTest {

    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private SoakSessionRepository soakSessionRepository;

    @Autowired
    private TempPointRepository tempPointRepository;

    @Test
    void importTempPoints_savesPointsAndUpdatesJudgement() {
        InspectionCreateRequest req = new InspectionCreateRequest();
        setField(req, "productName", "Prod-A");
        setField(req, "lotNo", "LOT-001");
        setField(req, "createdBy", "tester");

        Inspection inspection = inspectionService.createInspection(req);
        SoakSession session = inspection.getSessions().get(0);

        String csv = "offsetMs,tempC\n-20,89.00\n0,90.00\n20,91.00\n";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "points.csv",
            "text/csv",
            csv.getBytes()
        );

        csvImportService.importTempPoints(session.getId(), file);

        assertEquals(3, tempPointRepository.findBySessionIdOrderByOffsetMsAsc(session.getId()).size());

        SoakSession updated = soakSessionRepository.findById(session.getId()).orElseThrow();
        assertEquals(Judgement.PASS, updated.getJudgement());
        assertNotNull(updated.getTriggerTempC());
        assertEquals(0, new BigDecimal("90.00").compareTo(updated.getTriggerTempC()));
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}