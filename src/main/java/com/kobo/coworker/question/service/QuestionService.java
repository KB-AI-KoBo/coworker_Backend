package com.kobo.coworker.question.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobo.coworker.analysis.service.AnalysisService;
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
import com.kobo.coworker.analysis.dto.AnalysisResultInfoDto;
import com.kobo.coworker.common.AIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public AnalysisResultInfoDto handleQuestionSubmission(String email, MultipartFile file, String content) {
        Document document = uploadIfFileExists(email, file);
        saveQuestion(email, document, content);

        String aiResponseJson = aiClient.analyzeQuestion(email, document, content);

        try {
            AnalysisResultInfoDto resultDto = objectMapper.readValue(aiResponseJson, AnalysisResultInfoDto.class);
            analysisService.save(resultDto);
            return resultDto;
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_COMMUNICATION_ERROR);
        }
    }

    private Document uploadIfFileExists(String email, MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        DocumentInfoDto documentInfoDto = documentService.uploadDocument(email, file);
        return documentInfoDto.toEntity();
    }

    @Transactional
    public void saveQuestion(String email, Document document, String content) {
        User user = userService.findUserWithUniqueEmail(email);

        if (document != null) {
            documentService.validateDocumentFileUrlUniqueness(document.getFileUrl());
        }

        Question question = QuestionInfoDto.builder()
                .user(user)
                .document(document)
                .content(content)
                .build()
                .toEntity();

        questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public QuestionInfoDto getQuestionInfoDtoById(Long id) {
        return QuestionInfoDto.fromEntity(getQuestionById(id));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS);
        }
        questionRepository.deleteById(id);
    }

    private Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS));
    }
}
