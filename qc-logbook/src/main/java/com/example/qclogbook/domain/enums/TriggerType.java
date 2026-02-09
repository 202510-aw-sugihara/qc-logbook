package com.example.qclogbook.domain.enums;

/**
 * トリガー種別を表す区分。
 */
public enum TriggerType {
    /** 規定電圧到達 */
    V_REACHED("規定電圧到達");

    private final String label;

    TriggerType(String label) {
        this.label = label;
    }

    /**
     * 表示用ラベルを取得する。
     *
     * @return 日本語ラベル
     */
    public String getLabel() {
        return label;
    }
}