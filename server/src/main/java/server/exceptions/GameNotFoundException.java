package server.exceptions;

/**
 * Indicates there was no game found in the database
 */
public class GameNotFoundException extends DataAccessException{
    public GameNotFoundException(String message) {
        super(message);
    }
    public GameNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
