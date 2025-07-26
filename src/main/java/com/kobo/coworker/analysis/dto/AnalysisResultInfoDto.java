package com.kobo.coworker.analysis.dto;

import com.kobo.coworker.analysis.domain.AnalysisResult;
import com.kobo.coworker.document.domain.Document;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResultInfoDto {
    private Long analysisId;
    private Long questionId;
    private Document document;
    private String content;
    private String result;
    private int label;

    public AnalysisResult toEntity() {
        return AnalysisResult.builder()
                .questionId(questionId)
                .document(document)
                .content(content)
                .result(result)
                .label(label)
                .build();
    }

    public static AnalysisResultInfoDto fromEntity(AnalysisResult entity) {
        return AnalysisResultInfoDto.builder()
                .analysisId(entity.getAnalysisId())
                .questionId(entity.getQuestionId())
                .document(entity.getDocument())
                .content(entity.getContent())
                .result(entity.getResult())
                .label(entity.getLabel())
                .build();
    }
}
