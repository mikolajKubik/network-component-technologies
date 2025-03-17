package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.AppException;

public class AccountNotFoundException extends AppException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
