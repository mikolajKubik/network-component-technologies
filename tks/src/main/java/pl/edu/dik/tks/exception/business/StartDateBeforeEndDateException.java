package pl.edu.dik.tks.exception.business;

import pl.edu.dik.tks.exception.BadRequestException;

public class StartDateBeforeEndDateException extends BadRequestException {
    public StartDateBeforeEndDateException(String message) {
        super(message);
    }
}
