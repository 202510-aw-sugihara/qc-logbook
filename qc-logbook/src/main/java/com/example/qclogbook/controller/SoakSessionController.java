package com.example.qclogbook.controller;

import com.example.qclogbook.controller.form.CsvUploadForm;
import com.example.qclogbook.domain.entity.SoakSession;
import com.example.qclogbook.domain.entity.TempPoint;
import com.example.qclogbook.repository.SoakSessionRepository;
import com.example.qclogbook.repository.TempPointRepository;
import com.example.qclogbook.service.CsvImportService;
import com.example.qclogbook.service.dto.CsvImportResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * SoakSessionの詳細表示とCSVアップロードを扱うコントローラ。
 */
@Controller
public class SoakSessionController {

    private static final int MAX_POINTS = 200;

    private final SoakSessionRepository soakSessionRepository;
    private final TempPointRepository tempPointRepository;
    private final CsvImportService csvImportService;

    public SoakSessionController(
        SoakSessionRepository soakSessionRepository,
        TempPointRepository tempPointRepository,
        CsvImportService csvImportService
    ) {
        this.soakSessionRepository = soakSessionRepository;
        this.tempPointRepository = tempPointRepository;
        this.csvImportService = csvImportService;
    }

    @GetMapping("/sessions/{id}")
    public String detail(@PathVariable Long id, Model model) {
        SoakSession session = soakSessionRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 表示負荷を抑えるため、先頭200件まで表示
        List<TempPoint> points = tempPointRepository.findBySessionIdOrderByOffsetMsAsc(id)
            .stream()
            .limit(MAX_POINTS)
            .toList();

        model.addAttribute("session", session);
        model.addAttribute("points", points);
        model.addAttribute("csvUploadForm", new CsvUploadForm());
        return "sessions/detail";
    }

    @PostMapping("/sessions/{id}/upload")
    public String upload(
        @PathVariable Long id,
        @Valid CsvUploadForm form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors() || form.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "CSVファイルを選択してください。");
            return "redirect:/sessions/" + id;
        }

        try {
            CsvImportResult result = csvImportService.importTempPoints(id, form.getFile());
            String message = String.format(
                "取込完了: total=%d, imported=%d, skipped=%d",
                result.totalLines(),
                result.importedPoints(),
                result.skippedLines()
            );
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/sessions/" + id;
    }
}