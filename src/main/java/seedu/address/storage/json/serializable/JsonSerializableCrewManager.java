package seedu.address.storage.json.serializable;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.Crew;
import seedu.address.storage.json.JsonItemManager;
import seedu.address.storage.json.adapted.JsonAdaptedCrew;

/**
 * Represents a serializable manager of crew.
 */
@JsonRootName(value = "crewmanager")
public class JsonSerializableCrewManager
        extends JsonItemManager<Crew, JsonAdaptedCrew> {

    @JsonCreator
    public JsonSerializableCrewManager(
            @JsonProperty("items") List<JsonAdaptedCrew> crew
    ) {
        this.items.addAll(crew);
    }

    /**
     * Creates a new JsonSerializableCrewManager from the given manager.
     *
     * @param manager the manager to create the JsonSerializableCrewManager
     *                from, it should be a ReadOnlyItemManager
     *                &lt;Crew&gt;
     * @return a new JsonSerializableCrewManager
     */
    public static JsonSerializableCrewManager from(
            ReadOnlyItemManager<Crew> manager
    ) {
        final JsonSerializableCrewManager res =
                new JsonSerializableCrewManager(new ArrayList<>());
        res.readFromManager(manager);
        return res;
    }

    @Override
    protected JsonAdaptedCrew getJsonAdaptedModel(Crew item) {
        return new JsonAdaptedCrew(item);
    }
}
