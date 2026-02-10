package com.example.qclogbook.service.exception;

/**
 * CSVヘッダが不正な場合の例外。
 */
public class CsvInvalidHeaderException extends RuntimeException {

    public CsvInvalidHeaderException(String message) {
        super(message);
    }
}