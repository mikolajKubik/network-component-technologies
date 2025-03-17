package pl.edu.dik.ports.exception.business;


import pl.edu.dik.ports.exception.ConflictException;

public class IncorrectPasswordException extends ConflictException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
