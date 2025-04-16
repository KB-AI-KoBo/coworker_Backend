package com.kobo.coworker.document.mapper;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.dto.DocumentReqDto;

public class DocumentMapper {

    public static Document toEntity(DocumentReqDto dto) {
        if (dto == null) return null;

        return Document.builder()
                .fileUrl(dto.getFileUrl())
                .originalFilename(dto.getOriginalFilename())
                .fileType(dto.getFileType())
                .build();
    }
}
