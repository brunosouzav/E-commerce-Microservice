package com.user.microservice.exceptions;

public class CodeInvalidException extends RuntimeException{


    private static final long serialVersionUID = 1L;

    public CodeInvalidException (String message) {
        super(message);
    }
}
