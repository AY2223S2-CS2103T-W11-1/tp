package wingman.logic.core.exceptions;

import wingman.commons.exceptions.IllegalValueException;

/**
 * Represents a parse error encountered by a parser.
 */
public class ParseException extends IllegalValueException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Formats a message with the given arguments.
     *
     * @param message the message to format
     * @param objects the arguments to format the message with
     * @return the formatted message
     */
    public static ParseException formatted(String message, Object... objects) {
        return new ParseException(String.format(
                message,
                objects
        ));
    }
}
