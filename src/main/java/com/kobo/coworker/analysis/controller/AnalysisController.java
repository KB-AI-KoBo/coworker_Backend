package com.kobo.coworker.analysis.controller;

import com.kobo.coworker.analysis.domain.AnalysisResult;
import com.kobo.coworker.analysis.dto.AnalysisResultReqDto;
import com.kobo.coworker.analysis.service.AnalysisService;
import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<SuccessStatus> receiveAnalysis(@RequestBody AnalysisResultReqDto dto) {
        analysisService.saveAnalysisResult(dto);
        return ResponseEntity.ok(SuccessStatus._OK);
    }

    @GetMapping("/result/{id}")
    public ResponseEntity<AnalysisResult> getAnalysisResultById(@PathVariable Long id) {
        return analysisService.findAnalysisResultById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/results/document/{documentId}")
    public ResponseEntity<List<AnalysisResult>> getAnalysisResultsByDocument(@PathVariable Long documentId) {
        List<AnalysisResult> results = analysisService.findAnalysisResultsByDocumentId(documentId);
        return results.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(results);
    }

    @DeleteMapping("/result/{id}")
    public ResponseEntity<Void> deleteAnalysisResult(@PathVariable Long id) {
        if (analysisService.findAnalysisResultById(id).isPresent()) {
            analysisService.deleteAnalysisResultById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
