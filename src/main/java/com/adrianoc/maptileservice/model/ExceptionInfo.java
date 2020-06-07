package com.adrianoc.maptileservice.model;

public class ExceptionInfo {
    private String message;
    private String exceptionType;
    private String timeStamp;
    private int statusCode;
    private String path;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ExceptionInfo{" +
                "message='" + message + '\'' +
                ", exceptionType='" + exceptionType + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", statusCode=" + statusCode +
                ", path='" + path + '\'' +
                '}';
    }
}
