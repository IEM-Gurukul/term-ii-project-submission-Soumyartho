package service;

/**
 * Thrown when a book has no available copies for borrowing.
 */
public class BookUnavailableException extends Exception {

    public BookUnavailableException(String message) {
        super(message);
    }

}
