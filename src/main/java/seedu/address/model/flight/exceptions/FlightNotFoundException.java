package seedu.address.model.flight.exceptions;

/**
 * Signals that the flight cannot be found in the list.
 */
public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException() {
        super("Flight not found in the list.");
    }
}
