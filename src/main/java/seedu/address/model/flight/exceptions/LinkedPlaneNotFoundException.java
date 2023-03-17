package seedu.address.model.flight.exceptions;

/**
 * Signals that this flight has no linked plane.
 */
public class LinkedPlaneNotFoundException extends NullPointerException {
    public LinkedPlaneNotFoundException() {
        super("This flight has no linked plane.");
    }
}
