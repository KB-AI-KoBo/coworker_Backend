package com.kobo.coworker.common.apiPayload.code.status;

import com.kobo.coworker.common.apiPayload.code.BaseErrorCode;
import com.kobo.coworker.common.apiPayload.code.ErrorReasonDto;
import com.kobo.coworker.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 사용자 관련 에러
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4002", "이미 존재하는 사용자입니다."),

    // 문서 관련 에러
    DOCUMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "DOCUMENT4002", "이미 존재하는 문서입니다."),
    DOCUMENT_INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "DOCUMENT4003", "허용되지 않는 파일 형식입니다."),
    DOCUMENT_FILENAME_REQUIRED(HttpStatus.BAD_REQUEST, "DOCUMENT4004", "파일명은 Null이 불가능합니다."),

    // AI 서버 관련 에러
    AI_SERVER_INVALID_URI(HttpStatus.INTERNAL_SERVER_ERROR, "AI5001", "AI 서버 URI가 잘못되었습니다."),
    AI_SERVER_IO_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "AI5002", "AI 서버와의 연결 중 입출력 오류가 발생했습니다."),
    AI_SERVER_REQUEST_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "AI5003", "AI 서버 요청이 중단되었습니다."),
    AI_SERVER_COMMUNICATION_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "AI5004", "AI 서버와의 통신에 실패했습니다."),

    // 질문 관련 에러
    QUESTION_NOT_EXISTS(HttpStatus.NOT_FOUND, "QUESTION404", "존재하지 않는 질문입니다."),

    // 분석 결과 관련 에러
    ANALYSIS_RESULT_NOT_EXISTS(HttpStatus.NOT_FOUND, "ANALYSIS404", "존재하지 않는 분석결과입니다."),

    // 이메일 관련 에러
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL4002", "이미 존재하는 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }

}
