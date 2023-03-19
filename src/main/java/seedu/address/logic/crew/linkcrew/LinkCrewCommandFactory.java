package seedu.address.logic.crew.linkcrew;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.fp.Lazy;
import seedu.address.commons.util.GetUtil;
import seedu.address.logic.core.CommandFactory;
import seedu.address.logic.core.CommandParam;
import seedu.address.logic.core.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.crew.Crew;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.FlightCrewType;
import seedu.address.model.flight.Flight;

/**
 * The factory that creates {@code LinkCrewCommand}.
 */
public class LinkCrewCommandFactory implements CommandFactory<LinkCrewCommand> {
    private static final String COMMAND_WORD = "link";
    private static final String CABIN_SERVICE_DIRECTOR_PREFIX = "/csd";
    private static final String SENIOR_FLIGHT_ATTENDANT_PREFIX = "/sfa";
    private static final String FLIGHT_ATTENDANT_PREFIX = "/fa";
    private static final String TRAINEE_PREFIX = "/tr";
    private static final String FLIGHT_PREFIX = "/fl";
    private static final String NO_CREW_MESSAGE =
            "No crew has been entered. Please enter /csd for the Cabin Service Director."
                    + " Optional crew include: /sfa for Senior Flight Attendants,"
                    + " /fa for Flight Attendants and /tr for Trainees";
    private static final String NO_FLIGHT_MESSAGE =
            "No flight has been entered. Please enter /fl for the flight.";

    private final Lazy<ReadOnlyItemManager<Crew>> crewManagerLazy;

    private final Lazy<ReadOnlyItemManager<Flight>> flightManagerLazy;

    /**
     * Creates a new link command factory with the model registered.
     */
    public LinkCrewCommandFactory() {
        this(GetUtil.getLazy(Model.class));
    }

    /**
     * Creates a new link command factory with the given modelLazy.
     *
     * @param modelLazy the modelLazy used for the creation of the link command factory.
     */
    public LinkCrewCommandFactory(Lazy<Model> modelLazy) {
        this(
                modelLazy.map(Model::getCrewManager),
                modelLazy.map(Model::getFlightManager)
        );
    }

    /**
     * Creates a new link crew command factory with the given crew manager
     * lazy and the flight manager lazy.
     *
     * @param crewManagerLazy  the lazy instance of the crew manager.
     * @param flightManagerLazy the lazy instance of the flight manager.
     */
    public LinkCrewCommandFactory(
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
     * @param crewManager  the crew manager.
     * @param flightManager the flight manager.
     */
    public LinkCrewCommandFactory(
            ReadOnlyItemManager<Crew> crewManager,
            ReadOnlyItemManager<Flight> flightManager
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
                CABIN_SERVICE_DIRECTOR_PREFIX,
                SENIOR_FLIGHT_ATTENDANT_PREFIX,
                FLIGHT_ATTENDANT_PREFIX,
                TRAINEE_PREFIX,
                FLIGHT_PREFIX
        ));
    }


    private boolean addCrew(
            Optional<String> crewIdOptional,
            FlightCrewType type,
            Map<FlightCrewType, Crew> target
    ) {
        if (crewIdOptional.isEmpty()) {
            return false;
        }
        Optional<Crew> crewOptional =
                crewManagerLazy.get().getItem(crewIdOptional.get());
        if (crewOptional.isEmpty()) {
            return false;
        }
        target.put(type, crewOptional.get());
        return true;
    }

    private Flight getFlightOrThrow(
            Optional<String> flightIdOptional
    ) throws ParseException {
        if (flightIdOptional.isEmpty()) {
            throw new ParseException(NO_FLIGHT_MESSAGE);
        }
        Optional<Flight> flightOptional =
                flightManagerLazy.get().getItem(flightIdOptional.get());
        if (flightOptional.isEmpty()) {
            throw new ParseException(NO_FLIGHT_MESSAGE);
        }
        return flightOptional.get();
    }


    @Override
    public LinkCrewCommand createCommand(CommandParam param) throws ParseException {
        Optional<String> cabinServiceDirectorIdOptional =
                param.getNamedValues(CABIN_SERVICE_DIRECTOR_PREFIX);
        Map<FlightCrewType, Crew> crews = new HashMap<>();

        boolean hasFoundCrew = addCrew(
                cabinServiceDirectorIdOptional,
                FlightCrewType.CABIN_SERVICE_DIRECTOR,
                crews
        );

        if (!hasFoundCrew) {
            throw new ParseException(NO_CREW_MESSAGE);
        }

        Flight flight = getFlightOrThrow(param.getNamedValues(FLIGHT_PREFIX));
        return new LinkCrewCommand(crews, flight);
    }
}
