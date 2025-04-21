package com.kobo.coworker.analysis.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnalysisResultInfoDto {

    private DocumentInfo document;
    private String content;
    private String result;
    private int label;

    @Data
    public static class DocumentInfo {
        private String originalFilename;
        private String fileUrl;
    }

}
