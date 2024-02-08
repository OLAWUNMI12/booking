package com.event.booking.exception;

public class AttendeesCountExceededException extends RuntimeException {

    private String message;

    public AttendeesCountExceededException() {
    }

    public AttendeesCountExceededException(String msg) {
        super(msg);
        this.message = msg;
    }
}
