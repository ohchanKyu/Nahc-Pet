package kr.ac.dankook.cultureApplication.exception;

import lombok.Getter;

@Getter
public class DuplicatedEmailException extends RuntimeException{
    private final ErrorCode errorCode;
    public DuplicatedEmailException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
