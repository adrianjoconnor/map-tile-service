package com.adrianoc.maptileservice.exception;


import org.springframework.http.HttpStatus;

public class ImageConversionException extends RestException {
    public ImageConversionException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
