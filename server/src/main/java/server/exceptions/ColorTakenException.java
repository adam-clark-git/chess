package server.exceptions;

/**
 * Indicates that the game already contains that player's color
 */
public class ColorTakenException extends DataAccessException{
    public ColorTakenException(String message) {
        super(message);
    }
    public ColorTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
