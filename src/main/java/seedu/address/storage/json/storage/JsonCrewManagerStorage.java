package seedu.address.storage.json.storage;

import java.nio.file.Path;

import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.Crew;
import seedu.address.storage.json.JsonItemStorage;
import seedu.address.storage.json.adapted.JsonAdaptedCrew;
import seedu.address.storage.json.serializable.JsonSerializableCrewManager;

/**
 * Represents a storage for {@link Crew}s.
 */
public class JsonCrewManagerStorage
        extends JsonItemStorage<Crew, JsonAdaptedCrew, JsonSerializableCrewManager> {
    /**
     * Creates a new JsonIdentifiableStorage object.
     *
     * @param filePath the path to the file to be read from and written to.
     */
    public JsonCrewManagerStorage(Path filePath) {
        super(filePath);
    }

    @Override
    protected Class<JsonSerializableCrewManager> getManagerClass() {
        return JsonSerializableCrewManager.class;
    }

    @Override
    protected JsonSerializableCrewManager createManager(
            ReadOnlyItemManager<Crew> modelManager
    ) {
        return JsonSerializableCrewManager.from(modelManager);
    }
}
