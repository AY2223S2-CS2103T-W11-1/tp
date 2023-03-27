package seedu.address.logic.pilot.unlinklocation;

import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.link.exceptions.LinkException;
import seedu.address.model.location.Location;
import seedu.address.model.location.PilotLocationType;
import seedu.address.model.pilot.Pilot;

/**
 * The command class that unlinks pilot to
 * locations, where they reside.
 */
public class UnlinkPilotToLocationCommand implements Command {
    private static final String LOCATION_NOT_FOUND_EXCEPTION =
            "Location with id [%s] is not found.";
    private static final String PILOT_NOT_FOUND_EXCEPTION =
            "Pilot with id [%s] is not found.";
    private static final String DISPLAY_MESSAGE =
            "Unlinked pilot [%s] from location [%s].";

    /**
     * The id of the location
     */
    private final Location location;

    /**
     * The id of the pilot
     */
    private final Map<PilotLocationType, Pilot> pilot;

    /**
     * Creates a new link command.
     *
     * @param location the id of the location.
     * @param pilot the id of the pilot.
     */
    public UnlinkPilotToLocationCommand(Location location, Map<PilotLocationType, Pilot> pilot) {
        this.location = location;
        this.pilot = pilot;
    }

    @Override
    public String toString() {
        String result = pilot.entrySet()
                .stream()
                .map((entry) -> String.format(
                        "%s: %s",
                        entry.getKey(),
                        entry.getValue().getName()))
                .collect(Collectors.joining(","));
        return String.format(DISPLAY_MESSAGE, result, location.getName());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            for (Map.Entry<PilotLocationType, Pilot> entry : pilot.entrySet()) {
                location.getPilotLink().delete(entry.getKey(), entry.getValue());
            }
        } catch (LinkException e) {
            throw new CommandException(e.getMessage());
        }

        return new CommandResult(this.toString());
    }
}
