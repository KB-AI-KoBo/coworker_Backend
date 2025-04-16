package com.kobo.coworker.question.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.mapper.DocumentMapper;
import com.kobo.coworker.document.service.DocumentService;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.dto.QuestionReqDto;
import com.kobo.coworker.question.repository.QuestionRepository;
import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final DocumentService documentService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           UserService userService,
                           DocumentService documentService) {
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.documentService = documentService;
    }

    @Transactional
    public void submitQuestion(String username, QuestionReqDto dto) {
        User user = getUser(username);
        Document document = createAndValidateDocumentIfExists(dto);

        Question question = createQuestion(user, document, dto.getContent());
        save(question);
    }

    private User getUser(String username) {
        return userService.findUserWithUniqueUsername(username);
    }

    private Document createAndValidateDocumentIfExists(QuestionReqDto dto) {
        if (!dto.hasDocument()) return null;

        Document document = DocumentMapper.toEntity(dto.getDocument());
        validateDocumentUniqueness(document.getFileUrl());
        return document;
    }

    private void validateDocumentUniqueness(String fileUrl) {
        documentService.validateDocumentFileUrlUniqueness(fileUrl);
    }

    private Question createQuestion(User user, Document document, String content) {
        return QuestionInfoDto.builder()
                .user(user)
                .document(document)
                .content(content)
                .build()
                .toEntity();
    }

    private void save(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public QuestionInfoDto getQuestionInfoDtoById(Long id) {
        return QuestionInfoDto.fromEntity(findQuestionById(id));
    }

    private Question findQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        checkQuestionExists(id);
        questionRepository.deleteById(id);
    }

    private void checkQuestionExists(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS);
        }
    }
}
