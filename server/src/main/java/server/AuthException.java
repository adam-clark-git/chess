package server;

/**
 * Indicates there was an error connecting to the database
 */
public class AuthException extends Exception{
    public AuthException(String message) {
        super(message);
    }
    public AuthException(String message, Throwable ex) {
        super(message, ex);
    }
}
