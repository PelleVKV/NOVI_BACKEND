package com.ffa.FFA_flight_booking_system.exceptions;

public class CapacityExceededException extends Exception {
    public CapacityExceededException() {
        super();
    }

    public CapacityExceededException(String message) {
        super(message);
    }

    public CapacityExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
