package seedu.address.storage.json.adapted;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.plane.Plane;
import seedu.address.storage.json.JsonAdaptedModel;

/**
 * Represents a Jackson-friendly version of a Plane.
 */
public class JsonAdaptedPlane implements JsonAdaptedModel<Plane> {
    public static final String MISSING_FIELD_MESSAGE_FORMAT =
            "Plane's %s field is missing!";
    private final String id;
    private final String planeModel;
    private final int planeAge;

    /**
     * Constructs a {@code JsonAdaptedPlane} with the given plane details.
     * This is intended for Jackson to use.
     *
     * @param id         the id of the plane.
     * @param planeModel the model of the plane.
     * @param planeAge   the age of the plane.
     */
    @JsonCreator
    public JsonAdaptedPlane(
            @JsonProperty("id") String id,
            @JsonProperty("planeModel") String planeModel,
            @JsonProperty("planeAge") int planeAge
    ) {
        this.id = id;
        this.planeModel = planeModel;
        this.planeAge = planeAge;
    }

    /**
     * Converts a given {@code Plane} into this class for Jackson use.
     *
     * @param plane the plane to be converted.
     */
    public JsonAdaptedPlane(Plane plane) {
        this.id = plane.getId();
        this.planeModel = plane.getModel();
        this.planeAge = plane.getAge();
    }

    @Override
    public Plane toModelType() throws IllegalValueException {
        if (id == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "id")
            );
        }

        if (planeModel == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "model")
            );
        }

        return new Plane(id, planeModel, planeAge);
    }
}
