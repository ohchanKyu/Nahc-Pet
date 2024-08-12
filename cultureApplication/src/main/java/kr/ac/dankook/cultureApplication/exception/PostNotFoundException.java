package kr.ac.dankook.cultureApplication.exception;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public PostNotFoundException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
