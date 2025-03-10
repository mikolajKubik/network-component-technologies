package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.ConflictException;

public class GameRentedException extends ConflictException {
    public GameRentedException(String message) {
        super(message);
    }
}
