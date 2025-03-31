package com.kobo.coworker.analysis.domain;

import com.kobo.coworker.document.domain.Document;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analysisId;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Document document;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String result;

    @Column(nullable = false)
    private String label;
}
