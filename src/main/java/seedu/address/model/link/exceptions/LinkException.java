package seedu.address.model.link.exceptions;

/**
 * The exception that happens with a link.
 */
public class LinkException extends Exception {
    /**
     * Creates a link exception.
     *
     * @param message the message of the link exception.
     */
    public LinkException(String message) {
        super(message);
    }
}
