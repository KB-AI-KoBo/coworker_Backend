package com.kobo.coworker.question.controller;

import com.kobo.coworker.AI.AIClient;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.question.dto.QuestionReqDto;
import com.kobo.coworker.question.service.QuestionService;
import com.kobo.coworker.document.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
