package com.kayles.hotels.exception;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException() {}

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
