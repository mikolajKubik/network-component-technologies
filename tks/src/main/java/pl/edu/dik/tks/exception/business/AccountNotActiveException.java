package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.BadRequestException;

public class AccountNotActiveException extends BadRequestException {
    public AccountNotActiveException(String message) {
        super(message);
    }
}
