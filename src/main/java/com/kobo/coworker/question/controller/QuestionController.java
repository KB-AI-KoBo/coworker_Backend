package com.kobo.coworker.question.controller;

import com.kobo.coworker.common.AIClient;
import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import com.kobo.coworker.document.dto.DocumentInfoDto;
import com.kobo.coworker.document.service.DocumentService;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.service.QuestionService;
import com.kobo.coworker.document.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final AIClient aiClient;
    private final DocumentService documentService;

    @Autowired
    public QuestionController(QuestionService questionService, AIClient aiClient, DocumentService documentService) {
        this.questionService = questionService;
        this.aiClient = aiClient;
        this.documentService = documentService;
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String submitQuestion(
            @RequestParam("email") String email,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("content") String content) {

        Document document = null;
        if (file != null && !file.isEmpty()) {
            DocumentInfoDto documentInfoDto = documentService.uploadDocument(email, file);
            document = documentInfoDto.toEntity();
        }

        questionService.submitQuestion(email, document, content);
        return aiClient.analyzeQuestion(email, document, content);
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
