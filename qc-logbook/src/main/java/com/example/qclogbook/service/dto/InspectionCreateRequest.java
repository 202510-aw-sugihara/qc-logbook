package com.example.qclogbook.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * Inspection作成用の入力DTO。
 */
@Getter
public class InspectionCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotBlank
    @Size(max = 50)
    private String lotNo;

    @Size(max = 50)
    private String createdBy;

    public InspectionCreateRequest() {
    }

    public InspectionCreateRequest(String productName, String lotNo, String createdBy) {
        this.productName = productName;
        this.lotNo = lotNo;
        this.createdBy = createdBy;
    }
}