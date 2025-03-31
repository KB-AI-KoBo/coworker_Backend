package com.kobo.coworker.analysis.domain;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Entity
@Getter
@Setter
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
}
