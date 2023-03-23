package seedu.address.storage.json.adapted;

import java.util.Deque;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.GetUtil;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.Crew;
import seedu.address.model.link.Link;
import seedu.address.model.location.CrewLocationType;
import seedu.address.model.location.Location;
import seedu.address.model.location.PilotLocationType;
import seedu.address.model.pilot.Pilot;
import seedu.address.storage.json.JsonAdaptedModel;

/**
 * Represents a Jackson-friendly version of a Pilot.
 */
public class JsonAdaptedLocation implements JsonAdaptedModel<Location> {
    public static final String MISSING_FIELD_MESSAGE_FORMAT =
            "Location's %s field is missing!";

    /**
     * The id of the location.
     */
    private final String id;

    /**
     * The name of the location.
     */
    private final String name;

    /**
     * Linked crews, i.e., crews that reside in this location.
     */
    private final Map<CrewLocationType, Deque<String>> crewLink;

    /**
     * Linked pilots
     */
    private final Map<PilotLocationType, Deque<String>> pilotLink;

    /**
     * Constructs a {@code JsonAdaptedLocation} with the given location details.
     * This is intended for Jackson to use.
     *
     * @param id   The id of the location.
     * @param name The name of the location.
     * @param crewLink The link between locations to crews.
     *
     */
    @JsonCreator
    public JsonAdaptedLocation(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("crewLink") Map<CrewLocationType, Deque<String>> crewLink,
            @JsonProperty("pilotLink") Map<PilotLocationType, Deque<String>> pilotLink
    ) {
        this.id = id;
        this.name = name;
        this.crewLink = crewLink;
        this.pilotLink = pilotLink;
    }

    /**
     * Converts a given {@code Location} into this class for Jackson use.
     *
     * @param location The location to be converted.
     */
    public JsonAdaptedLocation(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.crewLink = location.crewLink.getCopiedContents();
        this.pilotLink = location.pilotLink.getCopiedContents();
    }

    @Override
    public Location toModelType() throws IllegalValueException {
        if (id == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "id")
            );
        }

        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "name")
            );
        }

        Location location;

        Link<CrewLocationType, Crew, ReadOnlyItemManager<Crew>> linkCrew =
                Link.fromOrCreate(
                        Crew.SHAPE_FOR_LOCATION,
                        crewLink,
                        GetUtil.getLazy(Model.class)
                                .map(Model::getCrewManager)
                );

        Link<PilotLocationType, Pilot, ReadOnlyItemManager<Pilot>> linkPilot =
                Link.fromOrCreate(
                        Pilot.SHAPE_FOR_LOCATION,
                        pilotLink,
                        GetUtil.getLazy(Model.class)
                                .map(Model::getPilotManager)
                );

        location = new Location(
                id,
                name,
                linkCrew,
                linkPilot
        );

        return location;
    }
}
