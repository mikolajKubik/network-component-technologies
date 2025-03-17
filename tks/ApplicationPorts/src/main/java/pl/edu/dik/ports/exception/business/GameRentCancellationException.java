package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.ConflictException;

public class GameRentCancellationException extends ConflictException {
    public GameRentCancellationException(String message) {
        super(message);
    }
}
