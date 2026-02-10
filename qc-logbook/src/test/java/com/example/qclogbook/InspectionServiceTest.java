package com.example.qclogbook;

import com.example.qclogbook.domain.entity.Inspection;
import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.domain.enums.EnvironmentType;
import com.example.qclogbook.domain.enums.Judgement;
import com.example.qclogbook.service.InspectionService;
import com.example.qclogbook.service.dto.InspectionCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class InspectionServiceTest {

    @Autowired
    private InspectionService inspectionService;

    @Test
    void createInspection_generatesThreeSessionsWithDefaults() {
        InspectionCreateRequest req = new InspectionCreateRequest();
        setField(req, "productName", "Prod-A");
        setField(req, "lotNo", "LOT-001");
        setField(req, "createdBy", "tester");

        Inspection saved = inspectionService.createInspection(req);

        assertNotNull(saved.getId());
        assertNotNull(saved.getSessions());
        assertEquals(3, saved.getSessions().size());

        Map<EnvironmentType, SoakSession> byEnv = saved.getSessions().stream()
            .collect(Collectors.toMap(SoakSession::getEnvType, Function.identity()));

        assertEquals(90, byEnv.get(EnvironmentType.HIGH).getTargetTempC());
        assertEquals(23, byEnv.get(EnvironmentType.AMBIENT).getTargetTempC());
        assertEquals(-10, byEnv.get(EnvironmentType.LOW).getTargetTempC());

        for (SoakSession session : saved.getSessions()) {
            assertEquals(5, session.getToleranceC());
            assertEquals(0, session.getOutOfSpecCount());
            assertEquals(Judgement.PENDING, session.getJudgement());
            assertEquals(60000, session.getWindowBeforeMs());
            assertEquals(60000, session.getWindowAfterMs());
            assertEquals(0, new BigDecimal("12.00").compareTo(session.getTargetVoltageV()));
        }
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