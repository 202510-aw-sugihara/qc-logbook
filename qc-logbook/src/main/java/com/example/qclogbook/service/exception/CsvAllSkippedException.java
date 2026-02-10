package com.example.qclogbook.service.exception;

/**
 * 全行スキップで取込件数が0の場合の例外。
 */
public class CsvAllSkippedException extends RuntimeException {

    public CsvAllSkippedException(String message) {
        super(message);
    }
}