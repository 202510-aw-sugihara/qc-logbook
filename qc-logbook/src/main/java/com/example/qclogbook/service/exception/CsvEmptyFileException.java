package com.example.qclogbook.service.exception;

/**
 * CSVが空である場合の例外。
 */
public class CsvEmptyFileException extends RuntimeException {

    public CsvEmptyFileException(String message) {
        super(message);
    }
}