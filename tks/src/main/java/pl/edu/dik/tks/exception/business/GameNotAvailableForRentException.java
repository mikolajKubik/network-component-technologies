package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.ConflictException;

public class GameNotAvailableForRentException extends ConflictException {
    public GameNotAvailableForRentException(String message) {
        super(message);
    }
}
