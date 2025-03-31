package com.kobo.coworker.question.service;

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
import java.util.Optional;

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
        validDocumentByFileUrl(document.getFileUrl());
        Question question = createQuestionEntity(user, document, content);
        saveQuestion(question);
    }

    private User getUserByUsername(String username) {
        return userService.findUserWithUniqueUsername(username);
    }

    private void validDocumentByFileUrl(String fileUrl) {
        documentService.findDocumentWithUniqueFileUrl(fileUrl);
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



    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new RuntimeException("질문을 찾을 수 없습니다.");
        }
    }
}
