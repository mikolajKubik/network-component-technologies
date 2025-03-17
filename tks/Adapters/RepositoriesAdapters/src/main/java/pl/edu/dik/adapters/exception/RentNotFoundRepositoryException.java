package pl.edu.dik.adapters.exception;

import pl.edu.dik.ports.exception.business.RentNotFoundException;

public class RentNotFoundRepositoryException extends RentNotFoundException {
    public RentNotFoundRepositoryException(String message) {
        super(message);
    }
}
