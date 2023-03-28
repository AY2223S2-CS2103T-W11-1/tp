package seedu.address.logic.crew.linkflight;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.fp.Lazy;
import seedu.address.commons.util.GetUtil;
import seedu.address.logic.core.Command;
import seedu.address.logic.core.CommandFactory;
import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.logic.core.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.Crew;
import seedu.address.model.crew.FlightCrewType;
import seedu.address.model.exception.IndexOutOfBoundException;
import seedu.address.model.flight.Flight;

/**
 * The factory that creates {@code LinkCrewCommand}.
 */
public class LinkCrewToFlightCommandFactory implements CommandFactory<LinkCrewToFlightCommand> {
    private static final String COMMAND_WORD = "linkflight";
    private static final String FLIGHT_PREFIX = "/fl";
    private static final String CABIN_SERVICE_DIRECTOR_PREFIX = "/csd";
    private static final String SENIOR_FLIGHT_ATTENDANT_PREFIX = "/sfa";
    private static final String FLIGHT_ATTENDANT_PREFIX = "/fa";
    private static final String TRAINEE_PREFIX = "/tr";

    private static final String NO_FLIGHT_MESSAGE =
            "No flight has been entered.\n"
                    + "Please enter /fl followed by the flight ID.";
    private static final String NO_CREW_MESSAGE =
            "No crew has been entered.\n"
                    + "Please enter at least 1 of the following:\n"
                    + "     /csd for the Cabin Service Director, "
                    + "/sfa for the Senior Flight Attendants,\n"
                    + "     /fa for the Flight Attendants, "
                    + "/tr for the Trainees.";
    private static final String INVALID_INDEX_VALUE_MESSAGE =
                    "%s is an invalid value.\n"
                            + "Please try using an integer instead.";
    private static final String INDEX_OUT_OF_BOUNDS_MESSAGE =
            "Index %s is out of bounds.\n"
                    + "Please enter a valid index.";

    private final Lazy<ReadOnlyItemManager<Crew>> crewManagerLazy;
    private final Lazy<ReadOnlyItemManager<Flight>> flightManagerLazy;

    /**
     * Creates a new link command factory with the model registered.
     */
    public LinkCrewToFlightCommandFactory() {
        this(GetUtil.getLazy(Model.class));
    }

    /**
     * Creates a new link command factory with the given modelLazy.
     *
     * @param modelLazy the modelLazy used for the creation of the link command factory.
     */
    public LinkCrewToFlightCommandFactory(Lazy<Model> modelLazy) {
        this(
                modelLazy.map(Model::getCrewManager),
                modelLazy.map(Model::getFlightManager)
        );
    }

    /**
     * Creates a new link crew command factory with the given crew manager
     * lazy and the flight manager lazy.
     *
     * @param crewManagerLazy   the lazy instance of the crew manager.
     * @param flightManagerLazy the lazy instance of the flight manager.
     */
    public LinkCrewToFlightCommandFactory(
            Lazy<ReadOnlyItemManager<Crew>> crewManagerLazy,
            Lazy<ReadOnlyItemManager<Flight>> flightManagerLazy
    ) {
        this.crewManagerLazy = crewManagerLazy;
        this.flightManagerLazy = flightManagerLazy;
    }

    /**
     * Creates a new link crew command factory with the given crew manager
     * and the flight manager.
     *
     * @param flightManager the flight manager.
     * @param crewManager   the crew manager.
     */
    public LinkCrewToFlightCommandFactory(
            ReadOnlyItemManager<Flight> flightManager,
            ReadOnlyItemManager<Crew> crewManager
    ) {
        this(Lazy.of(crewManager), Lazy.of(flightManager));
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public Optional<Set<String>> getPrefixes() {
        return Optional.of(Set.of(
                FLIGHT_PREFIX,
                CABIN_SERVICE_DIRECTOR_PREFIX,
                SENIOR_FLIGHT_ATTENDANT_PREFIX,
                FLIGHT_ATTENDANT_PREFIX,
                TRAINEE_PREFIX
        ));
    }

    private boolean addCrew(Optional<String> crewIdOptional, FlightCrewType type, Map<FlightCrewType, Crew> target)
            throws CommandException {
        if (crewIdOptional.isEmpty()) {
            return false;
        }

        int crewId;
        try {
            crewId = Command.parseIntegerToZeroBasedIndex(crewIdOptional.get());
        } catch (NumberFormatException e) {
            throw new CommandException(String.format(
                    INVALID_INDEX_VALUE_MESSAGE,
                    crewIdOptional.get()
            ));
        }

        boolean isCrewIndexValid = (crewId < crewManagerLazy.get().size());
        if (!isCrewIndexValid) {
            throw new CommandException(String.format(
                    INDEX_OUT_OF_BOUNDS_MESSAGE,
                    crewId + 1));
        }

        Optional<Crew> crewOptional = crewManagerLazy.get().getItemOptional(crewId);
        if (crewOptional.isEmpty()) {
            return false;
        }
        target.put(type, crewOptional.get());
        return true;
    }

    private Flight getFlightOrThrow(Optional<String> flightIdOptional) throws ParseException, CommandException {
        if (flightIdOptional.isEmpty()) {
            throw new ParseException(NO_FLIGHT_MESSAGE);
        }

        int flightId;
        try {
            flightId = Command.parseIntegerToZeroBasedIndex(flightIdOptional.get());
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(INVALID_INDEX_VALUE_MESSAGE, flightIdOptional.get()));
        }

        boolean isFlightIndexValid = (flightId < flightManagerLazy.get().size());
        if (!isFlightIndexValid) {
            throw new CommandException(String.format(
                    INDEX_OUT_OF_BOUNDS_MESSAGE,
                    flightId + 1));
        }

        Optional<Flight> flightOptional = flightManagerLazy.get().getItemOptional(flightId);
        if (flightOptional.isEmpty()) {
            throw new ParseException(NO_FLIGHT_MESSAGE);
        }

        return flightOptional.get();
    }

    @Override
    public LinkCrewToFlightCommand createCommand(CommandParam param) throws ParseException, IndexOutOfBoundException {
        Optional<String> cabinServiceDirectorIdOptional =
                param.getNamedValues(CABIN_SERVICE_DIRECTOR_PREFIX);
        Optional<String> seniorFlightAttendantIdOptional =
                param.getNamedValues(SENIOR_FLIGHT_ATTENDANT_PREFIX);
        Optional<String> flightAttendantIdOptional =
                param.getNamedValues(FLIGHT_ATTENDANT_PREFIX);
        Optional<String> traineeIdOptional =
                param.getNamedValues(TRAINEE_PREFIX);

        Flight flight;
        try {
            flight = getFlightOrThrow(param.getNamedValues(FLIGHT_PREFIX));
        } catch (CommandException e) {
            throw new ParseException(e.getMessage());
        }

        Map<FlightCrewType, Crew> crews = new HashMap<>();
        boolean hasFoundCrew;
        try {
            hasFoundCrew = addCrew(
                    cabinServiceDirectorIdOptional,
                    FlightCrewType.CABIN_SERVICE_DIRECTOR,
                    crews
            ) || addCrew(
                    seniorFlightAttendantIdOptional,
                    FlightCrewType.SENIOR_FLIGHT_ATTENDANT,
                    crews
            ) || addCrew(
                    flightAttendantIdOptional,
                    FlightCrewType.FLIGHT_ATTENDANT,
                    crews
            ) || addCrew(
                    traineeIdOptional,
                    FlightCrewType.TRAINEE,
                    crews
            );
        } catch (CommandException e) {
            throw new ParseException(e.getMessage());
        }

        if (!hasFoundCrew) {
            throw new ParseException(NO_CREW_MESSAGE);
        }

        return new LinkCrewToFlightCommand(flight, crews);
    }
}
