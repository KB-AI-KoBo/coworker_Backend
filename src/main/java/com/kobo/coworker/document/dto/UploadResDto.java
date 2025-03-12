package com.kobo.coworker.document.dto;

import lombok.Getter;

@Getter
public class UploadResDto {
    private final String originalFilename;
    private final String fileUrl;

    public UploadResDto(String originalFilename, String fileUrl) {
        this.originalFilename = originalFilename;
        this.fileUrl = fileUrl;
    }
}
