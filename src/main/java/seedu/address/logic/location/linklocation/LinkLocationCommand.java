package seedu.address.logic.location.linklocation;

import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.flight.Flight;
import seedu.address.model.flight.exceptions.FlightNotFoundException;
import seedu.address.model.location.Location;
import seedu.address.model.location.exceptions.LocationNotFoundException;


/**
 * The command that links departure and arrival locations to a flight.
 */
public class LinkLocationCommand implements Command {
    /**
     * The flight which the location is to be linked to.
     */
    private final String flightId;

    /**
     * The arrival location to link flight with.
     */
    private final String arrivalLocationId;

    /**
     * The departure location to link flight with.
     */
    private final String departureLocationId;


    /**
     * Creates a command to link location to a flight.
     * @param flightId the id of the flight
     * @param arrivalLocationId the id of the arrival location
     * @param departureLocationId the id of the departure location
     */
    public LinkLocationCommand(String flightId, String departureLocationId, String arrivalLocationId) {
        this.flightId = flightId;
        this.departureLocationId = departureLocationId;
        this.arrivalLocationId = arrivalLocationId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Flight flightToLink;
        try {
            flightToLink = model.getFlightById(flightId);
        } catch (FlightNotFoundException e) {
            return new CommandResult(String.format("Flight id %s not found.", flightId));
        }

        Location departureLocationToLink;
        try {
            departureLocationToLink = model.getLocationById(departureLocationId);
        } catch (LocationNotFoundException e) {
            return new CommandResult(String.format("Departure location id %s not found.", departureLocationId));
        }

        Location arrivalLocationToLink;
        try {
            arrivalLocationToLink = model.getLocationById(arrivalLocationId);
        } catch (LocationNotFoundException e) {
            return new CommandResult(String.format("Arrival location id %s not found.", arrivalLocationId));
        }

        if (departureLocationToLink.equals(arrivalLocationToLink)) {
            return new CommandResult("Departure and arrival locations cannot be the same.");
        }

        model.linkFlightToLocations(flightToLink, departureLocationToLink, arrivalLocationToLink);
        return new CommandResult(
                String.format("Linked departure location %s and arrival location %s to flight %s.",
                        departureLocationId,
                        arrivalLocationId,
                        flightId)
        );
    }
}
