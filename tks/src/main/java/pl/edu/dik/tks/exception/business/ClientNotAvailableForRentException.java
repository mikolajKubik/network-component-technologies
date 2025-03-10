package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.ConflictException;

public class ClientNotAvailableForRentException extends ConflictException {
    public ClientNotAvailableForRentException(String message) {
        super(message);
    }
}
