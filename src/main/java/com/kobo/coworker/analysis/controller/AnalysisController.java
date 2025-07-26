package com.kobo.coworker.analysis.controller;

import com.kobo.coworker.analysis.dto.AnalysisResultInfoDto;
import com.kobo.coworker.analysis.service.AnalysisService;
import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/{id}")
    public AnalysisResultInfoDto getAnalysisResultById(@PathVariable Long id) {
        return analysisService.getDtoById(id);
    }

    @GetMapping("/document/{documentId}")
    public List<AnalysisResultInfoDto> getAnalysisResultsByDocument(@PathVariable Long documentId) {
        return analysisService.findAnalysisResultsByDocumentId(documentId);
    }

    @GetMapping("/question/{questionId}")
    public List<AnalysisResultInfoDto> getAnalysisResultsByQuestion(@PathVariable Long questionId) {
        return analysisService.findAnalysisResultsByQuestionId(questionId);
    }

    @DeleteMapping("/{id}")
    public SuccessStatus deleteAnalysisResult(@PathVariable Long id) {
        analysisService.deleteById(id);
        return SuccessStatus._OK;
    }

}
