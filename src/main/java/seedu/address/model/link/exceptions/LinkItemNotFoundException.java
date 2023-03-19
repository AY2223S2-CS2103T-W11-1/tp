package seedu.address.model.link.exceptions;

/**
 * The exception to be thrown when the given link item is not found.
 */
public class LinkItemNotFoundException extends LinkException {
    private static final String MESSAGE =
        "Item of type %s with id %s not found.";

    /**
     * Creates a link exception.
     *
     * @param key the key of the item.
     * @param id  the id of the item.
     */
    public LinkItemNotFoundException(String key, String id) {
        super(String.format(MESSAGE, key, id));
    }
}
