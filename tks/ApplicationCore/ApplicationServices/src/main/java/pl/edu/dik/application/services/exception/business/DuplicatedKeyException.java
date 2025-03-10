package pl.edu.dik.application.services.exception.business;

import pl.edu.dik.application.services.exception.ConflictException;

public class DuplicatedKeyException extends ConflictException {
    public DuplicatedKeyException(String message) {
        super(message);
    }
}
