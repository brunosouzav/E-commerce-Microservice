package com.user.microservice.exceptions;

public class UserInvalidException extends RuntimeException{
    public UserInvalidException(String message) {
        super(message);
    }
}
