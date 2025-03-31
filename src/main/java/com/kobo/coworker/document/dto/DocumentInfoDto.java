package com.kobo.coworker.document.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DocumentInfoDto {
    private final String originalFilename;
    private final String fileUrl;

    @Builder
    public DocumentInfoDto(String originalFilename, String fileUrl) {
        this.originalFilename = originalFilename;
        this.fileUrl = fileUrl;
    }
}
