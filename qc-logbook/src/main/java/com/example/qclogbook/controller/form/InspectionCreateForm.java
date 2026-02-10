package com.example.qclogbook.controller.form;

import com.example.qclogbook.service.dto.InspectionCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * Thymeleafフォーム用のInspection作成DTO。
 */
@Getter
public class InspectionCreateForm {

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotBlank
    @Size(max = 50)
    private String lotNo;

    public InspectionCreateRequest toRequest() {
        InspectionCreateRequest req = new InspectionCreateRequest();
        setField(req, "productName", productName);
        setField(req, "lotNo", lotNo);
        return req;
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}