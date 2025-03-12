package com.kobo.coworker.analysis.domain;

import com.kobo.coworker.document.domain.Document;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "analysis_results")
@Getter
@Setter
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = true)
    private Document document;

    @Column(name = "content", nullable = false)
    private String Content;

    @Column(name = "result", columnDefinition = "TEXT")
    private String result;

    @Column(name = "label")
    private String label;
}
