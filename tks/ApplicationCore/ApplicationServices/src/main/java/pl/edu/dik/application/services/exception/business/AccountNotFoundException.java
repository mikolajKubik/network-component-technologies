package pl.edu.dik.application.services.exception.business;

import pl.edu.dik.application.services.exception.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
