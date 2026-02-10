package com.example.qclogbook.controller;

import com.example.qclogbook.controller.form.InspectionCreateForm;
import com.example.qclogbook.domain.entity.Inspection;
import com.example.qclogbook.repository.InspectionRepository;
import com.example.qclogbook.service.InspectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Inspectionの一覧/作成/詳細を扱うコントローラ。
 */
@Controller
public class InspectionController {

    private final InspectionService inspectionService;
    private final InspectionRepository inspectionRepository;

    public InspectionController(
        InspectionService inspectionService,
        InspectionRepository inspectionRepository
    ) {
        this.inspectionService = inspectionService;
        this.inspectionRepository = inspectionRepository;
    }

    @GetMapping("/inspections")
    public String list(Model model) {
        List<Inspection> inspections = inspectionRepository.findAll();
        model.addAttribute("inspections", inspections);
        return "inspections/list";
    }

    @GetMapping("/inspections/new")
    public String newForm(Model model) {
        model.addAttribute("inspectionCreateForm", new InspectionCreateForm());
        return "inspections/new";
    }

    @PostMapping("/inspections")
    public String create(@Valid InspectionCreateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "inspections/new";
        }

        Inspection created = inspectionService.createInspection(form.toRequest());
        return "redirect:/inspections/" + created.getId();
    }

    @GetMapping("/inspections/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Inspection inspection = inspectionRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("inspection", inspection);
        model.addAttribute("sessions", inspection.getSessions());
        return "inspections/detail";
    }
}