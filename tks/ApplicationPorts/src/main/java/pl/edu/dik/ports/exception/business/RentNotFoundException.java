package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.NotFoundException;

public class RentNotFoundException extends NotFoundException {
    public RentNotFoundException(String message) {
        super(message);
    }
}
