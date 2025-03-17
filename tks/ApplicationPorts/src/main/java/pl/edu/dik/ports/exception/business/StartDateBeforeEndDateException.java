package pl.edu.dik.ports.exception.business;

import pl.edu.dik.ports.exception.ConflictException;

public class StartDateBeforeEndDateException extends ConflictException {
    public StartDateBeforeEndDateException(String message) {
        super(message);
    }
}
