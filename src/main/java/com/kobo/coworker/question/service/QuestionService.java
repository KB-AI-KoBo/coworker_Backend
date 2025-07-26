package com.kobo.coworker.question.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobo.coworker.analysis.dto.AnalysisResultInfoDto;
import com.kobo.coworker.analysis.service.AnalysisService;
import com.kobo.coworker.common.AIClient;
import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.dto.DocumentInfoDto;
import com.kobo.coworker.document.service.DocumentService;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.repository.QuestionRepository;
import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final DocumentService documentService;
    private final AnalysisService analysisService;
    private final AIClient aiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public AnalysisResultInfoDto handleQuestionSubmission(Principal principal, MultipartFile file, String content) {
        String email = principal.getName();
        User user = userService.findUserWithUniqueEmail(email);
        Document document = handleFileUploadIfPresent(email, file);
        Question question = saveQuestion(user, document, content);
        return analyzeAndSaveResult(document, content, question.getId());
    }

    private Document handleFileUploadIfPresent(String email, MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        DocumentInfoDto uploaded = documentService.uploadDocument(email, file);
        return uploaded.toEntity();
    }

    @Transactional
    public Question saveQuestion(User user, Document document, String content) {
        if (document != null) {
            documentService.validateDocumentFileUrlUniqueness(document.getFileUrl());
        }

        Question question = buildQuestion(user, document, content);
        return questionRepository.save(question);
    }

    private Question buildQuestion(User user, Document document, String content) {
        return QuestionInfoDto.builder()
                .user(user)
                .document(document)
                .content(content)
                .build()
                .toEntity();
    }

    private AnalysisResultInfoDto analyzeAndSaveResult(Document document, String content, Long questionId) {
        String aiResponseJson = aiClient.analyzeQuestion(document, content);
        try {
            AnalysisResultInfoDto resultDto = objectMapper.readValue(aiResponseJson, AnalysisResultInfoDto.class);
            Long savedId = analysisService.save(resultDto);
            resultDto.setAnalysisId(savedId);
            resultDto.setQuestionId(questionId);
            return resultDto;
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_COMMUNICATION_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public QuestionInfoDto getQuestionInfoDtoById(Long id) {
        return QuestionInfoDto.fromEntity(findQuestionOrThrow(id));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS);
        }
        questionRepository.deleteById(id);
    }

    private Question findQuestionOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS));
    }
}
