package com.example.qclogbook.config;

import com.example.qclogbook.repository.InspectionRepository;
import com.example.qclogbook.service.InspectionService;
import com.example.qclogbook.service.dto.InspectionCreateRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 起動時にデフォルト検査データを作成する。
 */
@Component
public class DefaultInspectionDataLoader implements CommandLineRunner {

    private final InspectionRepository inspectionRepository;
    private final InspectionService inspectionService;

    public DefaultInspectionDataLoader(
        InspectionRepository inspectionRepository,
        InspectionService inspectionService
    ) {
        this.inspectionRepository = inspectionRepository;
        this.inspectionService = inspectionService;
    }

    @Override
    public void run(String... args) {
        if (inspectionRepository.count() > 0) {
            return;
        }

        InspectionCreateRequest req = new InspectionCreateRequest(
            "Default Product",
            "DEFAULT-LOT",
            "system"
        );
        inspectionService.createInspection(req);
    }
}