package com.movies.movies_api.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String statusCode;

    public ResourceNotFoundException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}



