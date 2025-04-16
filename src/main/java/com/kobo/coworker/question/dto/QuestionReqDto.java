package com.kobo.coworker.question.dto;

import com.kobo.coworker.document.dto.DocumentReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionReqDto {
    private DocumentReqDto document;
    private String content;

    public boolean hasDocument() {
        return document != null &&
                document.getFileUrl() != null &&
                !document.getFileUrl().isBlank();
    }
}
