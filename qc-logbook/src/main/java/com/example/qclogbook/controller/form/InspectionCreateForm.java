package com.example.qclogbook.controller.form;

import com.example.qclogbook.service.dto.InspectionCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Thymeleafフォーム用のInspection作成DTO。
 */
@Getter
@Setter
public class InspectionCreateForm {

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotBlank
    @Size(max = 50)
    private String lotNo;

    public InspectionCreateRequest toRequest() {
        return new InspectionCreateRequest(productName, lotNo, null);
    }
}