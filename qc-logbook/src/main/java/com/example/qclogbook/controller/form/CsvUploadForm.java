package com.example.qclogbook.controller.form;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * CSVアップロード用フォーム。
 */
public class CsvUploadForm {

    @NotNull
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public boolean isEmpty() {
        return file == null || file.isEmpty();
    }
}