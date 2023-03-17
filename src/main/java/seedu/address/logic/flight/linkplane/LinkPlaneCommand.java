package seedu.address.logic.flight.linkplane;

import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.flight.Flight;
import seedu.address.model.plane.Plane;

/**
 * The command that links a plane to a flight in the Wingman app.
 */
public class LinkPlaneCommand implements Command {
    /**
     * The flight which the plane is being linked to.
     */
    private final String flightId;

    /**
     * The plane to be linked to the flight.
     */
    private final String planeId;

    /**
     * Creates a command that, when executed, links the plane to a flight in the address book.
     *
     * @param flightId The flight to be linked to.
     * @param planeId  The plane to be linked.
     */
    public LinkPlaneCommand(String flightId, String planeId) {
        this.flightId = flightId;
        this.planeId = planeId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Flight flight = null;
        for (Flight f : model.getFilteredFlightList()) {
            if (f.getId().equals(flightId)) {
                flight = f;
            }
        }

        Plane plane = null;
        for (Plane p : model.getFilteredPlaneList()) {
            if (p.getId().equals(planeId)) {
                plane = p;
            }
        }

        // TODO: add exception when flight id or plane id is invalid
        model.linkPlane(flight, plane);
        return new CommandResult("Linked plane: " + plane.getId()
                + " to flight: " + flight.getId());
    }
}
