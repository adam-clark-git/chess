package server.exceptions;

/**
 * Indicates that the request is missing values
 */
public class BadRequestException extends DataAccessException{
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(String message, Throwable ex) {
        super(message, ex);
    }
}
