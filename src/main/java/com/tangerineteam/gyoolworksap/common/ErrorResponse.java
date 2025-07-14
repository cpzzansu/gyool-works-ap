package com.tangerineteam.gyoolworksap.common;

public class ErrorResponse {
    private final String code;
    private final String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(ResponseMassege rm) {
        this.code = rm.name();
        this.message = rm.getMessage();
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}