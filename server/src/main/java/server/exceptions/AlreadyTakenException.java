package server.exceptions;

/**
 * Indicates that the username is already used.
 */
public class AlreadyTakenException extends DataAccessException{
    public AlreadyTakenException(String message) {
        super(message);
    }
    public AlreadyTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
