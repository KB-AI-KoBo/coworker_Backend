package com.kobo.coworker.document.fixture;

import com.kobo.coworker.document.domain.Document;
import com.kobo.coworker.document.dto.DocumentInfoDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class TestFixture {

    public static Document createSampleDocument() {
        return Document.builder()
                .originalFilename("test.pdf")
                .fileUrl("https://coworker.s3.ap-northeast-2.amazonaws.com/세은/test.pdf")
                .build();
    }

    public static DocumentInfoDto createSampleDocumentInfoDto() {
        return DocumentInfoDto.builder()
                .originalFilename("test.pdf")
                .fileUrl("https://coworker.s3.ap-northeast-2.amazonaws.com/세은/test.pdf")
                .build();
    }

    public static MultipartFile createSampleMultipartFile() {
        return new MockMultipartFile(
                "file",
                "filename.unknown",
                "application/octet-stream",
                "fake-content".getBytes()
        );
    }

}
