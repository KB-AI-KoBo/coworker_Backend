package com.kobo.coworker.analysis.domain;

import com.kobo.coworker.document.domain.Document;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisId;

    @Column(name = "question_id", nullable = true)
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = true)
    private Document document;

    @Column(length = 1000)
    private String content;

    @Column(length = 1000)
    private String result;

    private int label;

    @Builder
    public AnalysisResult(Long questionId, Document document, String content, String result, int label) {
        this.questionId = questionId;
        this.document = document;
        this.content = content;
        this.result = result;
        this.label = label;
    }

}
