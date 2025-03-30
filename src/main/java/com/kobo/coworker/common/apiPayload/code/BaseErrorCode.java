package com.kobo.coworker.common.apiPayload.code;

public interface BaseErrorCode {

    public ErrorReasonDto getReason();
    public ErrorReasonDto getReasonHttpStatus();

}
