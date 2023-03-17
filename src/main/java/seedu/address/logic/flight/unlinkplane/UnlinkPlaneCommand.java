package seedu.address.logic.flight.unlinkplane;

import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.flight.Flight;

/**
 * The command that unlinks a plane from a flight in the Wingman app.
 */
public class UnlinkPlaneCommand implements Command {
    /**
     * The flight which the plane is being unlinked from.
     */
    private final String flightId;

    /**
     * Creates a command that, when executed, resets the linked plane from a flight in the address book.
     *
     * @param flightId The flight to be unlinked from.
     */
    public UnlinkPlaneCommand(String flightId) {
        this.flightId = flightId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Flight flight = null;
        for (Flight f : model.getFilteredFlightList()) {
            if (f.getId().equals(flightId)) {
                flight = f;
            }
        }
        // TODO: add exception when flight id is invalid
        model.unlinkPlane(flight);
        return new CommandResult(flight.getId() + " no longer has a linked plane.");
    }
}
