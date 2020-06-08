package com.adrianoc.maptileservice.exception;

import org.springframework.http.HttpStatus;

public class ImageLoadingException extends RestException {
    public ImageLoadingException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
