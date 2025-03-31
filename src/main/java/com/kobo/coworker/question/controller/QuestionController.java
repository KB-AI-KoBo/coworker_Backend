package com.kobo.coworker.question.controller;

import com.kobo.coworker.AI.AIClient;
import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.dto.QuestionReqDto;
import com.kobo.coworker.question.service.QuestionService;
import com.kobo.coworker.document.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final AIClient aiClient;

    @Autowired
    public QuestionController(QuestionService questionService, AIClient aiClient) {
        this.questionService = questionService;
        this.aiClient = aiClient;
    }

    @PostMapping
    public String submitQuestionForRequestAnalysis(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody QuestionReqDto questionReqDto) {
        Document document = questionReqDto.getDocument();
        String content = questionReqDto.getContent();

        questionService.submitQuestion(user.getUsername(), document, content);
        return aiClient.analyzeQuestion(document, content);
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
