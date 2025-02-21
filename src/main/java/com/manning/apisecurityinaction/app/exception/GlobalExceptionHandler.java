package com.manning.apisecurityinaction.app.exception;

import org.dalesbred.DatabaseSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleThrownException(
            Exception ex,
            WebRequest request
    ) {
        return switch (ex) {
            case IllegalArgumentException ignored ->
                    handleIllegalArgumentException((IllegalArgumentException) ex, request);
            case HttpMessageNotReadableException ignored ->
                    handleHttpMessageNotReadableException((HttpMessageNotReadableException) ex, request);
            case DatabaseSQLException ignored ->
                    handleDatabaseSqlException((DatabaseSQLException) ex, request);
            default -> handleUnspecifiedError(ex, request);
        };
    }

    private ResponseEntity<Object> handleUnspecifiedError(
            Exception ex,
            WebRequest request
    ) {

        return handleExceptionInternal(
                ex, 
                new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
                        ex.getLocalizedMessage()
                ), 
                new HttpHeaders(), 
                HttpStatus.INTERNAL_SERVER_ERROR, 
                request
        );
    }

    private ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        var status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternal(ex, new ErrorDTO(status.value(), status.getReasonPhrase(), ex.getLocalizedMessage()), new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            WebRequest request
    ) {
        var status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternal(ex, new ErrorDTO(status.value(), status.getReasonPhrase(), ex.getLocalizedMessage()), new HttpHeaders(), status, request);
    }
    
    private ResponseEntity<Object> handleDatabaseSqlException(
            DatabaseSQLException ex,
            WebRequest request
    ) {
        var status = HttpStatus.CONFLICT;
        return handleExceptionInternal(ex, new ErrorDTO(status.value(), status.getReasonPhrase(), status.getReasonPhrase()), new HttpHeaders(), status, request);
    }
}
