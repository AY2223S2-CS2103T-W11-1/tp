package seedu.address.storage.json.adapted;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.location.Location;
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
     * Constructs a {@code JsonAdaptedLocation} with the given location details.
     * This is intended for Jackson to use.
     *
     * @param id   The id of the location.
     * @param name The name of the location.
     */
    @JsonCreator
    public JsonAdaptedLocation(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }

    /**
     * Converts a given {@code Location} into this class for Jackson use.
     *
     * @param location The location to be converted.
     */
    public JsonAdaptedLocation(Location location) {
        this.id = location.getId();
        this.name = location.getName();
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

        return new Location(id, name);
    }
}
