package kr.ac.dankook.cultureApplication.exception;

import lombok.Getter;

@Getter
public class NoEntityException extends RuntimeException{
    private final ErrorCode errorCode;

    public NoEntityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}