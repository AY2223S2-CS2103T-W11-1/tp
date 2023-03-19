package seedu.address.logic.flight.addflight;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.core.CommandFactory;
import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.ParseException;
import seedu.address.model.flight.Flight;
import seedu.address.model.link.exceptions.LinkException;


/**
 * The factory that creates {@code AddLocationCommand}.
 */
public class AddFlightCommandFactory implements CommandFactory<AddFlightCommand> {
    public static final String COMMAND_WORD = "add";
    public static final String PREFIX_CODE = "/code";

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public Optional<Set<String>> getPrefixes() {
        return Optional.of(Set.of(PREFIX_CODE));
    }

    @Override
    public AddFlightCommand createCommand(CommandParam param) throws ParseException {
        final String code = param.getNamedValuesOrThrow(PREFIX_CODE);
        try {
            final Flight flight = new Flight(code);
            return new AddFlightCommand(flight);
        } catch (LinkException e) {
            throw new ParseException(e.getMessage());
        }
    }
}
