package pl.edu.dik.application.services.exception.business;

import pl.edu.dik.application.services.exception.NotFoundException;

public class RentNotFoundException extends NotFoundException {
    public RentNotFoundException(String message) {
        super(message);
    }
}
