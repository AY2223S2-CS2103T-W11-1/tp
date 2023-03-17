package seedu.address.logic.flight.linkplane;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.core.CommandFactory;
import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.ParseException;

/**
 * The factory that's responsible for creating a {@code LinkPlaneCommand}.
 */
public class LinkPlaneCommandFactory implements CommandFactory<LinkPlaneCommand> {
    public static final String COMMAND_WORD = "link";
    public static final String PREFIX_FLIGHT_ID = "/flight";
    public static final String PREFIX_PLANE_ID = "/plane";

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public Optional<Set<String>> getPrefixes() {
        return Optional.of(Set.of(PREFIX_FLIGHT_ID, PREFIX_PLANE_ID));
    }

    @Override
    public LinkPlaneCommand createCommand(CommandParam param) throws ParseException {
        String flightId = param.getNamedValuesOrThrow(PREFIX_FLIGHT_ID);
        String planeId = param.getNamedValuesOrThrow(PREFIX_PLANE_ID);

        return new LinkPlaneCommand(flightId, planeId);
    }
}
