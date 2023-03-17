package seedu.address.logic.location.unlinklocation;

import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.flight.Flight;
import seedu.address.model.flight.exceptions.FlightNotFoundException;

/**
 * The command that unlinks flights to departure and arrival locations.
 */
public class UnlinkLocationCommand implements Command {
    /**
     * The id of the light to unlink locations.
     */
    private final String flightId;

    /**
     * Creates a command that unlinks a flight to locations.
     * @param flightId the id of the flight to unlink
     */
    public UnlinkLocationCommand(String flightId) {
        this.flightId = flightId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Flight flightToUnlink;
        try {
            flightToUnlink = model.getFlightById(flightId);
        } catch (FlightNotFoundException e) {
            return new CommandResult(String.format("Flight id %s not found. ", flightId));
        }

        model.unlinkFlightToLocations(flightToUnlink);
        return new CommandResult(
                String.format("Unlink departure and arrival locations from flight %s", flightId)
        );
    }
}
