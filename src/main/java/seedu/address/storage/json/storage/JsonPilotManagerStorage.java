package seedu.address.storage.json.storage;

import java.nio.file.Path;

import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.pilot.Pilot;
import seedu.address.storage.json.JsonItemStorage;
import seedu.address.storage.json.adapted.JsonAdaptedPilot;
import seedu.address.storage.json.serializable.JsonSerializablePilotManager;

/**
 * Represents a storage for {@link Pilot}s.
 */
public class JsonPilotManagerStorage
        extends JsonItemStorage<Pilot, JsonAdaptedPilot, JsonSerializablePilotManager> {
    /**
     * Creates a new JsonIdentifiableStorage object.
     *
     * @param filePath the path to the file to be read from and written to.
     */
    public JsonPilotManagerStorage(Path filePath) {
        super(filePath);
    }

    @Override
    protected Class<JsonSerializablePilotManager> getManagerClass() {
        return JsonSerializablePilotManager.class;
    }

    @Override
    protected JsonSerializablePilotManager createManager(
            ReadOnlyItemManager<Pilot> modelManager
    ) {
        return JsonSerializablePilotManager.from(modelManager);
    }
}
