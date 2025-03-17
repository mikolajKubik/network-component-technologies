package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.ConflictException;

public class ClientNotAvailableForRentException extends ConflictException {
    public ClientNotAvailableForRentException(String message) {
        super(message);
    }
}
