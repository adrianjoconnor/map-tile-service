package com.adrianoc.maptileservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidParamatersException extends RestException {
    public InvalidParamatersException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
