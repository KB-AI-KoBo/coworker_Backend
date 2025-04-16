package com.kobo.coworker.question.dto;

import com.kobo.coworker.document.domain.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionReqDto {
    private Document document;
    private String content;
}
