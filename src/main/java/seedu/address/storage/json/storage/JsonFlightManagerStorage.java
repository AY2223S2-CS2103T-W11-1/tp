package seedu.address.storage.json.storage;

import java.nio.file.Path;

import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.flight.Flight;
import seedu.address.storage.json.JsonItemStorage;
import seedu.address.storage.json.adapted.JsonAdaptedFlight;
import seedu.address.storage.json.serializable.JsonSerializableFlightManager;


/**
 * Represents a storage for {@link Flight}s.
 */
public class JsonFlightManagerStorage
        extends JsonItemStorage<Flight, JsonAdaptedFlight, JsonSerializableFlightManager> {
    /**
     * Creates a new JsonIdentifiableStorage object.
     *
     * @param filePath the path to the file to be read from and written to.
     */
    public JsonFlightManagerStorage(Path filePath) {
        super(filePath);
    }

    @Override
    protected Class<JsonSerializableFlightManager> getManagerClass() {
        return JsonSerializableFlightManager.class;
    }

    @Override
    protected JsonSerializableFlightManager createManager(
            ReadOnlyItemManager<Flight> modelManager
    ) {
        return JsonSerializableFlightManager.from(modelManager);
    }
}
