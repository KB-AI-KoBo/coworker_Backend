package com.kobo.coworker.question.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobo.coworker.AI.AIClient;
import com.kobo.coworker.analysis.domain.AnalysisResult;
import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.code.status.SuccessStatus;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.analysis.service.AnalysisService;
import com.kobo.coworker.question.service.QuestionService;
import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final AnalysisService analysisService;

    @Autowired
    public QuestionController(QuestionService questionService, AnalysisService analysisService) {
        this.questionService = questionService;
        this.analysisService = analysisService;
    }

    @PostMapping("/submit")
    public String submitQuestion(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody Document document,
            @RequestParam("content") String content) {

        String fileUrl = document.getFileUrl();
        String originalFilename = document.getOriginalFilename();
        String username = user.getUsername();

        questionService.submitQuestion(fileUrl, username, content);

        AIClient aiClient = new AIClient();

        return aiClient.analyzeQuestion(originalFilename, fileUrl, content);
    }

//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> analysisResultMap = objectMapper.readValue(jsonResult, new TypeReference<Map<String, Object>>() {});
//
//        AnalysisResult analysisResult = new AnalysisResult();
//
//        Long returnedDocumentId = analysisResultMap.get("documentId") != null ? Long.valueOf(analysisResultMap.get("documentId").toString()) : null;
//        String questionContent = analysisResultMap.get("content").toString();
//        String result = analysisResultMap.get("result").toString();
//        String label = analysisResultMap.get("label").toString();
//
//        analysisResult.setContent(questionContent);
//        analysisResult.setResult(result);
//        analysisResult.setLabel(label);
//
//        AnalysisResult savedResult = analysisService.saveAnalysisResult(analysisResult);


    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionService.findQuestionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return new ResponseEntity<>("질문이 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("질문 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
