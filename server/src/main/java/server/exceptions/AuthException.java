package server.exceptions;

/**
 * Indicates there was no authToken found in the database
 */
public class AuthException extends DataAccessException{
    public AuthException(String message) {
        super(message);
    }
    public AuthException(String message, Throwable ex) {
        super(message, ex);
    }
}
