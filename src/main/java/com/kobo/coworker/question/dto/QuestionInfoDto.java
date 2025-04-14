package com.kobo.coworker.question.dto;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.question.domain.Question;
import com.kobo.coworker.user.domain.User;
import lombok.*;

@Setter
@Getter
@Builder
public class QuestionInfoDto {

    private Long id;
    private Document document;
    private User user;
    private String content;

    @Builder
    public QuestionInfoDto(Long id, Document document, User user, String content) {
        this.id = id;
        this.document = document;
        this.user = user;
        this.content = content;
    }

    public Question toEntity() {
        return Question.builder()
                .id(id)
                .document(document)
                .user(user)
                .content(content)
                .build();
    }

    public static QuestionInfoDto fromEntity(Question question) {
        return QuestionInfoDto.builder()
                .id(question.getId())
                .document(question.getDocument())
                .user(question.getUser())
                .content(question.getContent())
                .build();
    }

}
