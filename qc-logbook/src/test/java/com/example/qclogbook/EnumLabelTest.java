package com.example.qclogbook;

import com.example.qclogbook.domain.enums.EnvironmentType;
import com.example.qclogbook.domain.enums.Judgement;
import com.example.qclogbook.domain.enums.TriggerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Enumのラベル取得が期待通りであることを確認するテスト。
 */
class EnumLabelTest {

    @Test
    void environmentTypeLabels() {
        assertEquals("高温", EnvironmentType.HIGH.getLabel());
        assertEquals("常温", EnvironmentType.AMBIENT.getLabel());
        assertEquals("低温", EnvironmentType.LOW.getLabel());
    }

    @Test
    void judgementLabels() {
        assertEquals("未判定", Judgement.PENDING.getLabel());
        assertEquals("合格", Judgement.PASS.getLabel());
        assertEquals("不合格", Judgement.FAIL.getLabel());
    }

    @Test
    void triggerTypeLabel() {
        assertEquals("規定電圧到達", TriggerType.V_REACHED.getLabel());
    }
}