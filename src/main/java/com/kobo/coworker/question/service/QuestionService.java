package com.kobo.coworker.question.service;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.service.DocumentService;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.question.dto.QuestionInfoDto;
import com.kobo.coworker.question.repository.QuestionRepository;
import com.kobo.coworker.user.domain.User;
import com.kobo.coworker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public void submitQuestion(String username, Document document, String content) {
        User user = getUserByUsername(username);

        if (document != null) {
            validDocumentByFileUrl(document.getFileUrl());
        }

        Question question = createQuestionEntity(user, document, content);
        saveQuestion(question);
    }

    private User getUserByUsername(String username) {
        return userService.findUserWithUniqueUsername(username);
    }

    private void validDocumentByFileUrl(String fileUrl) {
        documentService.validateDocumentFileUrlUniqueness(fileUrl);
    }

    private Question createQuestionEntity(User user, Document document, String content) {
        QuestionInfoDto questionInfoDto = QuestionInfoDto.builder()
                .user(user)
                .document(document)
                .content(content)
                .build();
        return questionInfoDto.toEntity();
    }

    private void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public QuestionInfoDto getQuestionInfoDtoById(Long id) {
        Question question = findQuestionIdWithExists(id);
        return QuestionInfoDto.fromEntity(question);
    }

    private Question findQuestionIdWithExists(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        ensureQuestionExistsById(id);
        questionRepository.deleteById(id);
    }

    public void ensureQuestionExistsById(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new GeneralException(ErrorStatus.QUESTION_NOT_EXISTS);
        }
    }

}
