package com.example.qclogbook.service;

import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.domain.entity.TempPoint;
import com.example.qclogbook.domain.enums.EnvironmentType;
import com.example.qclogbook.domain.enums.Judgement;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SoakSessionJudgementServiceTest {

    private final SoakSessionJudgementService service = new SoakSessionJudgementService();

    @Test
    void emptyPoints_setsPendingAndNullSummary() {
        SoakSession session = sessionWithConditions();

        service.recompute(session, List.of());

        assertEquals(Judgement.PENDING, session.getJudgement());
        assertEquals(0, session.getOutOfSpecCount());
        assertNull(session.getSummaryMinC());
        assertNull(session.getSummaryMaxC());
        assertNull(session.getSummaryAvgC());
        assertNull(session.getTriggerTempC());
    }

    @Test
    void allInSpec_withTriggerAtZero_setsPass() {
        SoakSession session = sessionWithConditions();

        List<TempPoint> points = List.of(
            point(0, "90.12"),
            point(-20, "89.10"),
            point(20, "90.13")
        );

        service.recompute(session, points);

        assertEquals(Judgement.PASS, session.getJudgement());
        assertEquals(0, session.getOutOfSpecCount());
        assertEquals(new BigDecimal("89.10"), session.getSummaryMinC());
        assertEquals(new BigDecimal("90.13"), session.getSummaryMaxC());
        assertEquals(new BigDecimal("89.78"), session.getSummaryAvgC());
        assertEquals(new BigDecimal("90.12"), session.getTriggerTempC());
    }

    @Test
    void outOfSpec_exists_setsFail() {
        SoakSession session = sessionWithConditions();

        List<TempPoint> points = List.of(
            point(0, "90.00"),
            point(10, "96.00")
        );

        service.recompute(session, points);

        assertEquals(Judgement.FAIL, session.getJudgement());
        assertEquals(1, session.getOutOfSpecCount());
    }

    @Test
    void missingTriggerPoint_setsFail() {
        SoakSession session = sessionWithConditions();

        List<TempPoint> points = List.of(
            point(-20, "90.00"),
            point(20, "90.00")
        );

        service.recompute(session, points);

        assertEquals(Judgement.FAIL, session.getJudgement());
        assertNull(session.getTriggerTempC());
    }

    private static SoakSession sessionWithConditions() {
        return SoakSession.createDefault(
            null,
            EnvironmentType.HIGH,
            90,
            5,
            new BigDecimal("12.00"),
            60000,
            60000
        );
    }

    private static TempPoint point(int offsetMs, String tempC) {
        TempPoint p = new TempPoint();
        setField(p, "offsetMs", offsetMs);
        setField(p, "tempC", new BigDecimal(tempC));
        return p;
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