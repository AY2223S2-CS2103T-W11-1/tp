package seedu.address.model.link.exceptions;

/**
 * The exception to be thrown when there is a duplicate.
 */
public class LinkDuplicateException extends LinkException {
    /**
     * Creates a link exception.
     *
     * @param message the message of the link exception.
     */
    public LinkDuplicateException(String message) {
        super(message);
    }
}
