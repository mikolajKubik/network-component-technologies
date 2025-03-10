package pl.edu.dik.tks.exception.auth;

import pl.edu.dik.tks.exception.ConflictException;

public class IncorrectPasswordException extends ConflictException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
