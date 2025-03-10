package pl.edu.dik.tks.exception.auth;

import pl.edu.dik.tks.exception.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
