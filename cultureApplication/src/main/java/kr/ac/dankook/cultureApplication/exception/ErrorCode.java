package kr.ac.dankook.cultureApplication.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE, "해당 포스트가 존재하지 않음"),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_ACCEPTABLE,"사용자가 존재하지 않음"),
    SEND_EMAIL_ERROR(HttpStatus.NOT_ACCEPTABLE,"이메일 전송 에러"),
    DUPLICATED_EMAIL(HttpStatus.NOT_ACCEPTABLE,"이메일이 중복됩니다."),
    API_JSON_PARSING_ERROR(HttpStatus.NOT_ACCEPTABLE,"API JSON Parsing Error"),
    NoEntityException(HttpStatus.NOT_ACCEPTABLE,"No Entity Found");

    private final HttpStatus httpStatus;
    private final String message;
}