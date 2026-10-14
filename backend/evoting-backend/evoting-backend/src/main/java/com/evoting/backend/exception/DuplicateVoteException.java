package com.evoting.backend.exception;

public class DuplicateVoteException extends RuntimeException {

    public DuplicateVoteException(String message) {
        super(message);
    }
}
