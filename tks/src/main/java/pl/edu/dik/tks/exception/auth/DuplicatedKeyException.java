package pl.edu.dik.tks.exception.auth;

import pl.edu.dik.tks.exception.AppException;
import pl.edu.dik.tks.exception.ConflictException;

public class DuplicatedKeyException extends ConflictException {
    public DuplicatedKeyException(String message) {
        super(message);
    }
}
