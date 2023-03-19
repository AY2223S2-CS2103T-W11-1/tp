package seedu.address.storage.json.storage;

import java.nio.file.Path;

import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.plane.Plane;
import seedu.address.storage.json.JsonItemStorage;
import seedu.address.storage.json.adapted.JsonAdaptedPlane;
import seedu.address.storage.json.serializable.JsonSerializablePlaneManager;

/**
 * Represents a storage for {@link Plane}s.
 */
public class JsonPlaneManagerStorage
        extends JsonItemStorage<Plane, JsonAdaptedPlane, JsonSerializablePlaneManager> {
    public JsonPlaneManagerStorage(Path filePath) {
        super(filePath);
    }

    @Override
    protected Class<JsonSerializablePlaneManager> getManagerClass() {
        return JsonSerializablePlaneManager.class;
    }

    @Override
    protected JsonSerializablePlaneManager createManager(
            ReadOnlyItemManager<Plane> modelManager
    ) {
        return JsonSerializablePlaneManager.from(modelManager);
    }
}
