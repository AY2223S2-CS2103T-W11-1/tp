package seedu.address.logic.toplevel.syntax;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.pilot.Gender;
import seedu.address.model.pilot.Pilot;
import seedu.address.model.pilot.PilotRank;

/**
 * The syntax related to a pilot.
 */
public abstract class PilotSyntax {
    /**
     * The type name.
     */
    public static final String TYPE_NAME = "pilot";

    /**
     * The prefix for getting the name of the pilot.
     */
    public static final String PREFIX_NAME = "/n";

    /**
     * The prefix for getting the rank of the pilot.
     */
    public static final String PREFIX_RANK = "/r";

    /**
     * The prefix for getting the age of the pilot.
     */
    public static final String PREFIX_AGE = "/a";

    /**
     * The prefix for getting the gender of the pilot.
     */
    public static final String PREFIX_GENDER = "/g";

    /**
     * The prefix for getting the flight hour of the pilot.
     */
    public static final String PREFIX_FLIGHT_HOUR = "/fh";

    private static final String INVALID_PILOT_RANK_MESSAGE =
            "%s is an invalid pilot rank.\n"
                    + "Please try 0 for a Training Captain, "
                    + "1 for a Captain, "
                    + "2 for a Senior First Officer,\n"
                    + "3 for a First Officer, "
                    + "4 for a Second Officer, "
                    + "or 5 for a Cadet.";

    /**
     * The set of all prefixes of a pilot.
     */
    public static final Set<String> PREFIXES = Set.of(
            PREFIX_NAME,
            PREFIX_AGE,
            PREFIX_RANK,
            PREFIX_GENDER,
            PREFIX_FLIGHT_HOUR
    );

    /**
     * Creates a pilot.
     *
     * @param param the command parameter after parsing.
     * @return a new pilot
     * @throws ParseException if the parameter does not fit the requirements.
     */
    public static Pilot factory(CommandParam param) throws ParseException {
        final String name = param.getNamedValuesOrThrow(PREFIX_NAME);
        final int rankId = param.getNamedIntOrThrow(PREFIX_RANK);

        if (!(Stream.of(0, 1, 2, 3, 4, 5)
                .anyMatch(validRank -> validRank.equals(rankId)))) {
            throw new ParseException(String.format(
                    INVALID_PILOT_RANK_MESSAGE,
                    rankId
            ));
        }

        final PilotRank rank = PilotRank.fromIndex(rankId);
        final int age = param.getNamedIntOrThrow(PREFIX_AGE);
        final int genderId = param.getNamedIntOrThrow(PREFIX_GENDER);
        final Gender gender = Gender.fromIndex(genderId);
        final int flightHour = param.getNamedIntOrThrow(PREFIX_FLIGHT_HOUR);

        return new Pilot(name, age, gender, rank, flightHour);
    }

    /**
     * Adds the given pilot to the model.
     *
     * @param model the model to which the pilot is added.
     * @param pilot the pilot that which is added to the model.
     */
    public static void add(Model model, Pilot pilot) {
        model.addPilot(pilot);
    }

    /**
     * Gets the manager for pilot.
     *
     * @param model the model.
     * @return the manager for pilot.
     */
    public static ReadOnlyItemManager<Pilot> getManager(Model model) {
        return model.getPilotManager();
    }

    /**
     * Deletes the pilot from the model.
     *
     * @param model the model.
     * @param pilot the pilot.
     */
    public static void delete(Model model, Pilot pilot) {
        model.deletePilot(pilot);
    }
}
