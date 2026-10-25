package server;

/**
 * Indicates that the username is already used.
 */
public class PasswordIncorrectException extends DataAccessException{
    public PasswordIncorrectException(String message) {
        super(message);
    }
    public PasswordIncorrectException(String message, Throwable ex) {
        super(message, ex);
    }
}
