package com.kobo.coworker.analysis.dto;

import com.kobo.coworker.analysis.domain.AnalysisResult;
import com.kobo.coworker.document.domain.Document;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnalysisResultInfoDto {

    private Document document;
    private String content;
    private String result;
    private int label;

    @Builder
    public AnalysisResultInfoDto(Document document, String content, String result, int label) {
        this.document = document;
        this.content = content;
        this.result = result;
        this.label = label;
    }

    public AnalysisResult toEntity() {
        return AnalysisResult.builder()
                .document(document)
                .content(content)
                .result(result)
                .label(label)
                .build();
    }

    public static AnalysisResultInfoDto fromEntity(AnalysisResult analysisResult) {
        return AnalysisResultInfoDto.builder()
                .document(analysisResult.getDocument())
                .content(analysisResult.getContent())
                .result(analysisResult.getResult())
                .label(analysisResult.getLabel())
                .build();
    }

}
