package com.kobo.coworker.question.domain;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.user.domain.User;
import lombok.*;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Document document;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    @Builder
    public Question(Long questionId, Document document, User user, String content) {
        this.questionId = questionId;
        this.document = document;
        this.user = user;
        this.content = content;
    }

}
