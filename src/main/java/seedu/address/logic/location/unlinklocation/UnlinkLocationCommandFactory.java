package seedu.address.logic.location.unlinklocation;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.core.CommandFactory;
import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.ParseException;

/**
 * The factory that createsw {@code UnlinkLocationCommand}.
 */
public class UnlinkLocationCommandFactory implements CommandFactory<UnlinkLocationCommand> {
    public static final String COMMAND_WORD = "unlink";
    public static final String PREFIX_FLIGHT = "/flight";

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public Optional<Set<String>> getPrefixes() {
        return Optional.of(Set.of(PREFIX_FLIGHT));
    }

    @Override
    public UnlinkLocationCommand createCommand(CommandParam param) throws ParseException {
        final String flightId = param.getNamedValuesOrThrow(PREFIX_FLIGHT);
        return new UnlinkLocationCommand(flightId);
    }
}
