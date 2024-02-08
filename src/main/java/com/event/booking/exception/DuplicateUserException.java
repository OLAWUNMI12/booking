package com.event.booking.exception;

public class DuplicateUserException extends RuntimeException {
    private String message;

    public DuplicateUserException() {
    }

    public DuplicateUserException(String msg) {
        super(msg);
        this.message = msg;
    }

}
