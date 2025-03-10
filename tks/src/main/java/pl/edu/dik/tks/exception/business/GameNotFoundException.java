package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.NotFoundException;

public class GameNotFoundException extends NotFoundException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
