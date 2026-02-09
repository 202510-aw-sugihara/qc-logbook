package com.example.qclogbook.domain.enums;

/**
 * 試験結果の判定ステータスを表す区分。
 */
public enum Judgement {
    /** 未判定 */
    PENDING("未判定"),
    /** 合格 */
    PASS("合格"),
    /** 不合格 */
    FAIL("不合格");

    private final String label;

    Judgement(String label) {
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