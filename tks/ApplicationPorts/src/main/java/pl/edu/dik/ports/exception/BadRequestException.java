package pl.edu.dik.ports.exception;

public class BadRequestException extends AppException {
    public BadRequestException(String message) {
        super(message);
    }
}
