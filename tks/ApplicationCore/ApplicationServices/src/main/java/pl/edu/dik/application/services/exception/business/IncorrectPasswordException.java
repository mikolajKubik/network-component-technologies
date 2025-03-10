package pl.edu.dik.application.services.exception.business;

import pl.edu.dik.application.services.exception.ConflictException;

public class IncorrectPasswordException extends ConflictException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
