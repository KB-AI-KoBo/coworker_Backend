package com.kobo.coworker.document.dto;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.domain.FileType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DocumentInfoDto {

    private final String originalFilename;
    private final String fileUrl;
    private final FileType fileType;

    @Builder
    public DocumentInfoDto(String originalFilename, String fileUrl, FileType fileType) {
        this.originalFilename = originalFilename;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
    }

    public Document toEntity() {
        return Document.builder()
                .originalFilename(this.originalFilename)
                .fileUrl(this.fileUrl)
                .fileType(this.fileType)
                .build();
    }

}
