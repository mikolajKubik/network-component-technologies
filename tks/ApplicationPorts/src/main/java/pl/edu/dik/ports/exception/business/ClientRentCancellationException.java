package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.ConflictException;

public class ClientRentCancellationException extends ConflictException {
    public ClientRentCancellationException(String message) {
        super(message);
    }
}
