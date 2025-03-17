package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
