package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.NotFoundException;

public class RentNotFoundException extends NotFoundException {
    public RentNotFoundException(String message) {
        super(message);
    }
}
