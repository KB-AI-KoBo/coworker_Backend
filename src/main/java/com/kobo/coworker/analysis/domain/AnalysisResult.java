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
    private Long analysisId;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Document document;

    private String content;
    private String result;
    private int label;

    @Builder
    public AnalysisResult(Document document, String content, String result, int label) {
        this.document = document;
        this.content = content;
        this.result = result;
        this.label = label;
    }

}
