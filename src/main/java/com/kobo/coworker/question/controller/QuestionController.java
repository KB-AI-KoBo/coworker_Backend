package com.kobo.coworker.question.controller;

import com.kobo.coworker.analysis.dto.AnalysisResultInfoDto;
import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnalysisResultInfoDto> submitQuestion(
            @RequestParam("email") String email,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("content") String content) {

        AnalysisResultInfoDto result = questionService.handleQuestionSubmission(email, file, content);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public QuestionInfoDto getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionInfoDtoById(id);
    }

    @DeleteMapping("/{id}")
    public SuccessStatus deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return SuccessStatus._OK;
    }

}
