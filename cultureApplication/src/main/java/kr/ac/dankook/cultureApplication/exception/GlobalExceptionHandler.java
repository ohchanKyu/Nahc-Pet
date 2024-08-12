package kr.ac.dankook.cultureApplication.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NoEntityException.class)
    protected ResponseEntity<ErrorResponse> handleNoEntityException(NoEntityException ex){
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    @ExceptionHandler(ApiJsonParsingException.class)
    protected ResponseEntity<ErrorResponse> handleApiJsonParsingException(ApiJsonParsingException ex){
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    @ExceptionHandler(DuplicatedEmailException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicatedEmailException(DuplicatedEmailException ex){
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    @ExceptionHandler(MailException.class)
    protected ResponseEntity<ErrorResponse> handleSendMailException(MailException ex){
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    @ExceptionHandler(PostNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlerPostNotFoundException(PostNotFoundException ex){
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }
}