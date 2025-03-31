package com.kobo.coworker.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailDto {

    private final String from;
    private final String to;
    private final String title;
    private final String message;

}
