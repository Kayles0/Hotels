package com.kayles.hotels.exception;

public class DuplicateException extends BaseException {
    public DuplicateException() {}

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Object... args) {
        super(message, args);
    }
}
