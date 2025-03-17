package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.ConflictException;

public class IncorrectPlayerNumberException extends ConflictException {
    public IncorrectPlayerNumberException(String message) {
        super(message);
    }
}
