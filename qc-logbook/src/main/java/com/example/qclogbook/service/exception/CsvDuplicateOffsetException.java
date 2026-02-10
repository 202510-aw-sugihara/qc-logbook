package com.example.qclogbook.service.exception;

/**
 * Unique制約違反（重複offsetMsなど）の例外。
 */
public class CsvDuplicateOffsetException extends RuntimeException {

    public CsvDuplicateOffsetException(String message, Throwable cause) {
        super(message, cause);
    }
}