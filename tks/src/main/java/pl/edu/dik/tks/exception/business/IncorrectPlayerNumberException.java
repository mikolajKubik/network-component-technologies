package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.BadRequestException;

public class IncorrectPlayerNumberException extends BadRequestException {
    public IncorrectPlayerNumberException(String message) {
        super(message);
    }
}
