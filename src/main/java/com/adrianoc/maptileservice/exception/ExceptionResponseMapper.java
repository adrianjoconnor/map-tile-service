package com.adrianoc.maptileservice.exception;

import com.adrianoc.maptileservice.model.ExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ExceptionResponseMapper extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ExceptionResponseMapper.class);

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ExceptionInfo> handleRestException(RestException e, WebRequest request) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setPath(request.getContextPath());
        exceptionInfo.setExceptionType(e.getClass().getName());
        exceptionInfo.setMessage(e.getMessage());
        exceptionInfo.setStatusCode(e.getHttpStatus().value());
        exceptionInfo.setTimeStamp(ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME));
        logger.error("RestException thrown: {}", exceptionInfo);
        e.printStackTrace();
        return new ResponseEntity<>(exceptionInfo, e.getHttpStatus());
    }
}
