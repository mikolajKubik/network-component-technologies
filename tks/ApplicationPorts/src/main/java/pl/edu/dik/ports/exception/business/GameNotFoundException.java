package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.NotFoundException;

public class GameNotFoundException extends NotFoundException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
