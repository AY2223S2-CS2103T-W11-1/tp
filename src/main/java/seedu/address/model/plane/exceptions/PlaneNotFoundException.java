package seedu.address.model.plane.exceptions;

/**
 * Signals that the plane cannot be found in the list.
 */
public class PlaneNotFoundException extends RuntimeException {
    public PlaneNotFoundException() {
        super("Plane not found in the list.");
    }
}
