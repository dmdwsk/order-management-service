package com.codedmdwsk.ordermanagementservice.exceptions;

public class CustomerDeletionNotAllowedException extends RuntimeException {
    public CustomerDeletionNotAllowedException(String message) {
        super(message);
    }
}
