package seedu.address.logic.crew.linklocation;

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
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.Crew;
import seedu.address.model.location.CrewLocationType;
import seedu.address.model.location.Location;

/**
 * Links a crew to location.
 * The location could be a place where they reside.
 * To link a crew to a flight, the crew should reside in
 * the same location as the flight's starting location.
 */
public class LinkCrewToLocationCommandFactory implements CommandFactory<LinkCrewToLocationCommand> {
    private static final String COMMAND_WORD = "linklocation";
    private static final String LOCATION_PREFIX = "/lo";
    private static final String CREW_PREFIX = "/cr";

    private static final String NO_LOCATION_MESSAGE =
            "No location has been entered. "
                    + "Please enter /lo followed by the location ID.";
    private static final String NO_CREW_MESSAGE =
            "No crew has been entered. " +
                    "Please enter /cr followed by the crew ID.";

    private final Lazy<ReadOnlyItemManager<Crew>> crewManagerLazy;

    private final Lazy<ReadOnlyItemManager<Location>> locationManagerLazy;

    /**
     * Creates a new link command factory with the model registered.
     */
    public LinkCrewToLocationCommandFactory() {
        this(GetUtil.getLazy(Model.class));
    }

    /**
     * Creates a new link command factory with the given modelLazy.
     *
     * @param modelLazy the modelLazy used for the creation of the link command factory.
     */
    public LinkCrewToLocationCommandFactory(Lazy<Model> modelLazy) {
        this(
                modelLazy.map(Model::getCrewManager),
                modelLazy.map(Model::getLocationManager)
        );
    }

    /**
     * Creates a new link crew command factory with the given crew manager
     * lazy and the flight manager lazy.
     *
     * @param crewManagerLazy     the lazy instance of the crew manager.
     * @param locationManagerLazy the lazy instance of the location manager.
     */
    public LinkCrewToLocationCommandFactory(
            Lazy<ReadOnlyItemManager<Crew>> crewManagerLazy,
            Lazy<ReadOnlyItemManager<Location>> locationManagerLazy
    ) {
        this.crewManagerLazy = crewManagerLazy;
        this.locationManagerLazy = locationManagerLazy;
    }

    /**
     * Creates a new link crew command factory with the given crew manager
     * and the location manager.
     *
     * @param crewManager     the crew manager.
     * @param locationManager the flight manager.
     */
    public LinkCrewToLocationCommandFactory(
            ReadOnlyItemManager<Crew> crewManager,
            ReadOnlyItemManager<Location> locationManager
    ) {
        this(
                Lazy.of(crewManager),
                Lazy.of(locationManager)
        );
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public Optional<Set<String>> getPrefixes() {
        return Optional.of(Set.of(
                LOCATION_PREFIX,
                CREW_PREFIX
        ));
    }

    private boolean addCrew(
            Optional<String> crewIdOptional,
            CrewLocationType type,
            Map<CrewLocationType, Crew> target
    ) {
        if (crewIdOptional.isEmpty()) {
            return false;
        }
        int indexOfCrew =
                Integer.parseInt(crewIdOptional.get());
        Optional<Crew> crewOptional =
                crewManagerLazy.get().getItemOptional(indexOfCrew);
        if (crewOptional.isEmpty()) {
            return false;
        }
        target.put(type, crewOptional.get());
        return true;
    }

    private Location getLocationOrThrow(
            Optional<String> locationIdOptional
    ) throws ParseException {
        if (locationIdOptional.isEmpty()) {
            throw new ParseException(NO_LOCATION_MESSAGE);
        }
        int indexOfLocation =
                Integer.parseInt(locationIdOptional.get());
        Optional<Location> locationOptional =
                locationManagerLazy.get().getItemOptional(indexOfLocation);
        if (locationOptional.isEmpty()) {
            throw new ParseException(NO_LOCATION_MESSAGE);
        }

        return locationOptional.get();
    }

    @Override
    public LinkCrewToLocationCommand createCommand(CommandParam param) throws ParseException {
        Optional<String> locationIdOptional =
                param.getNamedValues(LOCATION_PREFIX);
        Optional<String> crewIdOptional =
                param.getNamedValues(CREW_PREFIX);

        Location location = getLocationOrThrow(locationIdOptional);
        Map<CrewLocationType, Crew> crews = new HashMap<>();

        boolean hasFoundCrew = addCrew(
                crewIdOptional,
                CrewLocationType.LOCATION_USING,
                crews
        );

        if (!hasFoundCrew) {
            throw new ParseException(NO_CREW_MESSAGE);
        }

        return new LinkCrewToLocationCommand(location, crews);
    }
}
