package service;

/**
 * Thrown when a user has already borrowed the maximum number of books.
 */
public class UserLimitExceededException extends Exception {

    public UserLimitExceededException(String message) {
        super(message);
    }

}
