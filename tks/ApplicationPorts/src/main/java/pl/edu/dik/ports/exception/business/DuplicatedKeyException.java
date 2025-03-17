package pl.edu.dik.ports.exception.business;


import pl.edu.dik.ports.exception.ConflictException;

public class DuplicatedKeyException extends ConflictException {
    public DuplicatedKeyException(String message) {
        super(message);
    }
}
