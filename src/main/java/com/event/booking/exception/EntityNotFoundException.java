package com.event.booking.exception;

public class EntityNotFoundException extends RuntimeException {

    private String message;

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String msg) {
        super(msg);
        this.message = msg;
    }
}
