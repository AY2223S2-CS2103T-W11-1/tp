package seedu.address.model.location;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import seedu.address.commons.util.GetUtil;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.Crew;
import seedu.address.model.item.Item;
import seedu.address.model.link.Link;
import seedu.address.model.pilot.Pilot;

/**
 * Location is a unit place that the flight can travel to or
 *  arrive at.
 */
public class Location implements Item {

    /**
     * The shape of the link between location and flight
     */
    public static final Map<FlightLocationType, Integer> SHAPE =
            Map.of(FlightLocationType.LOCATION_DEPARTURE, 1,
                    FlightLocationType.LOCATION_ARRIVAL, 1
            );
    private static final String NAME_STRING = "Name";
    private static final String ID_STRING = "ID";
    public final Link<CrewLocationType, Crew, ReadOnlyItemManager<Crew>> crewLink;
    public final Link<PilotLocationType, Pilot, ReadOnlyItemManager<Pilot>> pilotLink;
    private final String name;
    private final String id;

    /**
     * Creates a Location object with the given name.
     * @param name name of the location
     */
    public Location(
            String name
    ) {
        this(
            UUID.randomUUID().toString(),
            name,
            new Link<>(
                    Crew.SHAPE_FOR_LOCATION,
                    GetUtil.getLazy(Model.class).map(Model::getCrewManager)
            ),
            new Link<>(
                    Pilot.SHAPE_FOR_LOCATION,
                    GetUtil.getLazy(Model.class).map(Model::getPilotManager)
            )
        );
    }

    /**
     * Creates a Location object from the given id and name
     * @param id a unique id assigned to the location
     * @param name the name of the location
     * @param crewLink the link to the crews that stay in
     *                 this location
     * @param pilotLink the link to the pilot that stays
     *                  in this location
     */
    public Location(
            String id,
            String name,
            Link<CrewLocationType, Crew, ReadOnlyItemManager<Crew>> crewLink,
            Link<PilotLocationType, Pilot, ReadOnlyItemManager<Pilot>> pilotLink
    ) {
        this.id = id;
        this.name = name;
        this.crewLink = crewLink;
        this.pilotLink = pilotLink;
    }

    /**
     * Returns the name of the location in string.
     * @return the name of the location
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<String> getDisplayList() {
        return List.of(
                String.format("%s: %s", NAME_STRING, name),
                String.format("%s: %s", ID_STRING, id));
    }

    /**
     * Returns true if both locations have the same name.
     * This defines a weaker notion of equality between two locations.
     */
    public boolean equals(Location otherLocation) {
        if (otherLocation == this) {
            return true;
        }

        return otherLocation != null
                && otherLocation.getId().equals(getId());
    }
}
