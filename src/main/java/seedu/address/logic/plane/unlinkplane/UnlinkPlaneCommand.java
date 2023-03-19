package seedu.address.logic.plane.unlinkplane;

import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.flight.Flight;
import seedu.address.model.link.exceptions.LinkException;
import seedu.address.model.plane.FlightPlaneType;
import seedu.address.model.plane.Plane;


/**
 * The command that unlinks a plane from a flight
 */
public class UnlinkPlaneCommand implements Command {
    private static final String FLIGHT_NOT_FOUND_EXCEPTION =
            "Flight with id %s is not found.";
    private static final String PLANE_NOT_FOUND_EXCEPTION =
            "Plane with id %s is not found.";
    private static final String DISPLAY_MESSAGE =
            "Unlinked %s from flight %s.";

    /**
     * The id of the plane
     */
    private final Map<FlightPlaneType, Plane> planes;

    /**
     * The id of the flight
     */
    private final Flight flight;

    /**
     * Creates a new link command.
     *
     * @param planes the id of the planes.
     * @param flight the id of the flight.
     */
    public UnlinkPlaneCommand(Map<FlightPlaneType, Plane> planes, Flight flight) {
        this.planes = planes;
        this.flight = flight;
    }

    @Override
    public String toString() {
        String result = planes.entrySet()
                .stream()
                .map((entry) -> String.format(
                        "%s: %s",
                        entry.getKey(),
                        entry.getValue().getModel()))
                .collect(Collectors.joining(","));
        return String.format(DISPLAY_MESSAGE, result, flight.getCode());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            for (Map.Entry<FlightPlaneType, Plane> entry : planes.entrySet()) {
                flight.planeLink.delete(entry.getKey(), entry.getValue());
            }
        } catch (LinkException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(this.toString());
    }
}
