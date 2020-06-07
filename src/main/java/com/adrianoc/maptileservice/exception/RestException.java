package com.adrianoc.maptileservice.exception;

import org.springframework.http.HttpStatus;

public abstract class RestException extends RuntimeException {
    private HttpStatus httpStatus;

    public RestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
