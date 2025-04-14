package com.kobo.coworker.document.fixture;

import com.kobo.coworker.document.domain.Document;

public class TestFixture {

    public static Document createSampleDocument() {
        return Document.builder()
                .originalFilename("test.pdf")
                .fileUrl("https://coworker.s3.ap-northeast-2.amazonaws.com/세은/test.pdf")
                .build();
    }

}
