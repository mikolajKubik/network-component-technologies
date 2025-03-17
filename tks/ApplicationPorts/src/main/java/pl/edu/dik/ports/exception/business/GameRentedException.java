package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.ConflictException;

public class GameRentedException extends ConflictException {
    public GameRentedException(String message) {
        super(message);
    }
}
