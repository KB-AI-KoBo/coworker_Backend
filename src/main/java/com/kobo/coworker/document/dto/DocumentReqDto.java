package com.kobo.coworker.document.dto;

import com.kobo.coworker.document.domain.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentReqDto {
    private String originalFilename;
    private String fileUrl;
    private FileType fileType;
}
