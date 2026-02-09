package com.example.qclogbook.domain.enums;

/**
 * 試験時の温度環境を表す区分。
 */
public enum EnvironmentType {
    /** 高温 */
    HIGH("高温"),
    /** 常温 */
    AMBIENT("常温"),
    /** 低温 */
    LOW("低温");

    private final String label;

    EnvironmentType(String label) {
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