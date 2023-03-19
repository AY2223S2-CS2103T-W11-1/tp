package seedu.address.logic.pilot.linkpilot;

import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.flight.Flight;
import seedu.address.model.link.exceptions.LinkException;
import seedu.address.model.pilot.FlightPilotType;
import seedu.address.model.pilot.Pilot;

/**
 * The command that links the pilot.
 */
public class LinkPilotCommand implements Command {
    private static final String FLIGHT_NOT_FOUND_EXCEPTION =
        "Flight with id %s is not found.";
    private static final String PILOT_NOT_FOUND_EXCEPTION =
        "Pilot with id %s is not found.";
    private static final String DISPLAY_MESSAGE =
        "Link %s to flight %s.";

    /**
     * The id of the pilot
     */
    private final Map<FlightPilotType, Pilot> pilots;

    /**
     * The id of the flight
     */
    private final Flight flight;

    /**
     * Creates a new link command.
     *
     * @param pilots the id of the pilots.
     * @param flight the id of the flight.
     */
    public LinkPilotCommand(Map<FlightPilotType, Pilot> pilots, Flight flight) {
        this.pilots = pilots;
        this.flight = flight;
    }

    @Override
    public String toString() {
        String result = pilots.entrySet()
                            .stream()
                            .map((entry) -> String.format(
                                "%s: %s",
                                entry.getKey(),
                                entry.getValue().getName()))
                            .collect(Collectors.joining(","));
        return String.format(DISPLAY_MESSAGE, result, flight.getCode());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            for (Map.Entry<FlightPilotType, Pilot> entry : pilots.entrySet()) {
                flight.pilotLink.putRevolve(entry.getKey(), entry.getValue());
            }
        } catch (LinkException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(this.toString());
    }
}
