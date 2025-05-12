package com.idol.global.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(final ExceptionMessage message) {
        super(message.getMessage());
    }
}
