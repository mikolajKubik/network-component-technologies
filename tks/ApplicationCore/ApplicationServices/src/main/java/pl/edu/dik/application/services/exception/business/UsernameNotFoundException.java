package pl.edu.dik.application.services.exception.business;

import pl.edu.dik.application.services.exception.NotFoundException;

public class UsernameNotFoundException extends NotFoundException {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
