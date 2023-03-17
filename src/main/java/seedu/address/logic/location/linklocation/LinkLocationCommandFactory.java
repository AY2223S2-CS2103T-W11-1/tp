package seedu.address.logic.location.linklocation;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.core.CommandFactory;
import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.ParseException;

/**
 * The factory that creates {@code AddLocationCommand}.
 */
public class LinkLocationCommandFactory implements CommandFactory<LinkLocationCommand> {
    public static final String COMMAND_WORD = "link";
    public static final String PREFIX_FLIGHT = "/flight";
    public static final String PREFIX_DEPARTURE = "/from";
    public static final String PREFIX_ARRIVAL = "/to";

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public Optional<Set<String>> getPrefixes() {
        return Optional.of(Set.of(PREFIX_FLIGHT, PREFIX_DEPARTURE, PREFIX_ARRIVAL));
    }

    @Override
    public LinkLocationCommand createCommand(CommandParam param) throws ParseException {
        final String flightId = param.getNamedValuesOrThrow(PREFIX_FLIGHT);
        final String departureLocationId = param.getNamedValuesOrThrow(PREFIX_DEPARTURE);
        final String arrivalLocationId = param.getNamedValuesOrThrow(PREFIX_ARRIVAL);
        return new LinkLocationCommand(flightId, departureLocationId, arrivalLocationId);
    }
}
