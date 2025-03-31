package com.kobo.coworker.question.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobo.coworker.AI.AIClient;
import com.kobo.coworker.analysis.domain.AnalysisResult;
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
    private final DocumentRepository documentRepository;

    @Autowired
    public QuestionController(QuestionService questionService, AnalysisService analysisService, DocumentRepository documentRepository) {
        this.questionService = questionService;
        this.analysisService = analysisService;
        this.documentRepository = documentRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<AnalysisResult> submitQuestion(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(value = "documentId", required = false) Long documentId,
            @RequestParam("content") String content) {

        System.out.println("submitQuestion 호출됨");

        try {
            Question question = questionService.submitQuestion(documentId, user.getUsername(), content);
            System.out.println("질문 제출 성공! 질문 내용: " + question.getContent());

            AIClient aiClient = new AIClient();
            String jsonResult = aiClient.analyzeQuestion(documentId, content);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> analysisResultMap = objectMapper.readValue(jsonResult, new TypeReference<Map<String, Object>>() {});

            AnalysisResult analysisResult = new AnalysisResult();

            Long returnedDocumentId = analysisResultMap.get("documentId") != null ? Long.valueOf(analysisResultMap.get("documentId").toString()) : null;
            String questionContent = analysisResultMap.get("content").toString();
            String result = analysisResultMap.get("result").toString();
            String label = analysisResultMap.get("label").toString();

            if (returnedDocumentId != null) {
                Document document = documentRepository.findByDocumentId(returnedDocumentId)
                        .orElseThrow(() -> new RuntimeException("문서를 찾을 수 없습니다."));
                analysisResult.setDocument(document);
                System.out.println("저장된 문서(documentId): " + returnedDocumentId);
            } else {
                System.out.println("문서 없이 질문을 제출합니다.");
            }

            analysisResult.setContent(questionContent);
            analysisResult.setResult(result);
            analysisResult.setLabel(label);

            AnalysisResult savedResult = analysisService.saveAnalysisResult(analysisResult);
            System.out.println("분석 결과 저장 성공!");

            return ResponseEntity.status(HttpStatus.CREATED).body(savedResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
