package com.kobo.coworker.question.dto;

import com.kobo.coworker.document.domain.Document;
import lombok.Getter;

@Getter
public class QuestionReqDto {
    private Document document;
    private String content;
}
