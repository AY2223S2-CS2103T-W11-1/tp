package seedu.address.storage.json.adapted;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.crew.Crew;
import seedu.address.model.crew.CrewRank;
import seedu.address.model.crew.exceptions.InvalidCrewRankException;
import seedu.address.storage.json.JsonAdaptedModel;

/**
 * Represents a Jackson-friendly version of a Crew.
 */
public class JsonAdaptedCrew implements JsonAdaptedModel<Crew> {
    public static final String MISSING_FIELD_MESSAGE_FORMAT =
            "Crew's %s field is missing!";

    /**
     * The id of the crew.
     */
    private final String id;

    /**
     * The name of the crew.
     */
    private final String name;

    /**
     * The rank of the crew.
     */
    private final int rank;


    /**
     * Constructs a {@code JsonAdaptedPilot} with the given crew details.
     * This is intended for Jackson to use.
     *
     * @param id   The id of the crew.
     * @param rank The rank of the crew.
     */
    @JsonCreator
    public JsonAdaptedCrew(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("rank") int rank
    ) {
        this.id = id;
        this.name = name;
        this.rank = rank;
    }


    /**
     * Converts a given {@code Pilot} into this class for Jackson use.
     *
     * @param crew The crew to be converted.
     */
    public JsonAdaptedCrew(Crew crew) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.rank = crew.getRank().toIndex();
    }

    @Override
    public Crew toModelType() throws IllegalValueException {
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

        final CrewRank rank;

        try {
            rank = CrewRank.fromIndex(this.rank);
        } catch (InvalidCrewRankException e) {
            throw new IllegalValueException(e.getMessage());
        }

        return new Crew(id, name, rank);
    }
}
