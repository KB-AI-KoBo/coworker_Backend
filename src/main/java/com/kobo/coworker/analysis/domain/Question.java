package com.kobo.coworker.analysis.domain;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = true)
    private Document documentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;

    @Column(name = "label")
    private String label;
}
