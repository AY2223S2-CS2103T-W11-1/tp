package seedu.address.logic.flight.unlinkplane;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.core.CommandFactory;
import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.ParseException;

/**
 * The factory that's responsible for creating a {@code UnlinkPlaneCommand}.
 */
public class UnlinkPlaneCommandFactory implements CommandFactory<UnlinkPlaneCommand> {
    public static final String COMMAND_WORD = "unlink";
    public static final String PREFIX_FLIGHT_ID = "/flight";

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public Optional<Set<String>> getPrefixes() {
        return Optional.of(Set.of(PREFIX_FLIGHT_ID));
    }

    @Override
    public UnlinkPlaneCommand createCommand(CommandParam param) throws ParseException {
        String flightId = param.getNamedValuesOrThrow(PREFIX_FLIGHT_ID);
        return new UnlinkPlaneCommand(flightId);
    }
}
