package com.example.qclogbook.service.dto;

import java.math.BigDecimal;

/**
 * CSV取込結果を画面表示するためのDTO。
 */
public record CsvImportResult(
    Long sessionId,
    int totalLines,
    int importedPoints,
    int skippedLines,
    BigDecimal triggerTempC,
    int outOfSpecCount
) {
}