package com.kayles.hotels.exception;

public class BaseException extends RuntimeException {

    public BaseException() {}

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Object... args) {
        super(String.format(message, args));
    }
}
