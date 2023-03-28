package seedu.address.logic.plane.unlinkflight;

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
 * The command that unlinks a plane from a flight.
 */
public class UnlinkPlaneToFlightCommand implements Command {
    private static final String FLIGHT_NOT_FOUND_EXCEPTION =
            "Flight with ID %s can't be found.";
    private static final String PLANE_NOT_FOUND_EXCEPTION =
            "Plane with ID %s can't be found.";
    private static final String DISPLAY_MESSAGE =
            "Unlinked %s from %s.";

    /**
     * The id of the flight
     */
    private final Flight flight;

    /**
     * The id of the plane
     */
    private final Map<FlightPlaneType, Plane> planes;

    /**
     * Creates a new link command.
     *
     * @param flight the id of the flight.
     * @param planes the id of the planes.
     */
    public UnlinkPlaneToFlightCommand(Flight flight, Map<FlightPlaneType, Plane> planes) {
        this.flight = flight;
        this.planes = planes;
    }

    @Override
    public String toString() {
        String result = planes.entrySet()
                .stream()
                .map((entry) -> String.format(
                        "%s",
                        entry.getValue().toString()))
                .collect(Collectors.joining(","));
        return String.format(DISPLAY_MESSAGE, result, flight.getCode());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            for (Map.Entry<FlightPlaneType, Plane> entry : planes.entrySet()) {
                flight.planeLink.delete(entry.getKey(), entry.getValue());
                entry.getValue().setAvailable();
            }
        } catch (LinkException e) {
            throw new CommandException(e.getMessage());
        }

        return new CommandResult(this.toString());
    }
}
