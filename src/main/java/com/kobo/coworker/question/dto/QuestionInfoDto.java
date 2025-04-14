package com.kobo.coworker.question.dto;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.user.domain.User;
import lombok.*;

@Setter
@Getter
@Builder
public class QuestionInfoDto {

    private Long questionId;
    private Document document;
    private User user;
    private String content;

    @Builder
    public QuestionInfoDto(Long questionId, Document document, User user, String content) {
        this.questionId = questionId;
        this.document = document;
        this.user = user;
        this.content = content;
    }

    public Question toEntity() {
        return Question.builder()
                .id(questionId)
                .document(document)
                .user(user)
                .content(content)
                .build();
    }

    public static QuestionInfoDto fromEntity(Question question) {
        return QuestionInfoDto.builder()
                .questionId(question.getId())
                .document(question.getDocument())
                .user(question.getUser())
                .content(question.getContent())
                .build();
    }

}
