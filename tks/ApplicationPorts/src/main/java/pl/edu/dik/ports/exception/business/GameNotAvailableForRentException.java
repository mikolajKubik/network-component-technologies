package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.ConflictException;

public class GameNotAvailableForRentException extends ConflictException {
    public GameNotAvailableForRentException(String message) {
        super(message);
    }
}
