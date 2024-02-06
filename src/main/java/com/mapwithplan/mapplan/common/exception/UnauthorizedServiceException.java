package com.mapwithplan.mapplan.common.exception;

public class UnauthorizedServiceException extends RuntimeException{


    public UnauthorizedServiceException() {
    }

    public UnauthorizedServiceException(String message) {
        super(message);
    }

    public UnauthorizedServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
