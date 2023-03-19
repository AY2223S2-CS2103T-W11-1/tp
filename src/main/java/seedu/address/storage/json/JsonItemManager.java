package seedu.address.storage.json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ItemManager;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.item.Item;

/**
 * Represents a manager that manages a list of {@code JsonAdaptedModel}
 * objects. Implement this class and add Jackson annotations to allow
 * Jackson to convert the manager into a JSON file.
 *
 * @param <T> The type of the model.
 * @param <F> The type of the JsonAdaptedModel.
 */
public abstract class JsonItemManager<T extends Item, F extends JsonAdaptedModel<T>>
        implements JsonAdaptedModel<ItemManager<T>> {

    public static final String MESSAGE_DUPLICATE_ITEMS = "Items list contains duplicate item(s).";

    /**
     * The list of items to be stored.
     */
    protected final List<F> items = new ArrayList<>();

    /**
     * The function to convert the item into a JsonAdaptedModel.
     *
     * @param item The item to convert.
     * @return The JsonAdaptedModel.
     */
    protected abstract F getJsonAdaptedModel(T item);

    /**
     * Reads the items from the manager and stores them in the list.
     *
     * @param manager The manager to read from.
     */
    protected void readFromManager(ReadOnlyItemManager<T> manager) {
        items.addAll(manager.getItemList().stream()
                            .map(this::getJsonAdaptedModel)
                            .collect(Collectors.toList()));
    }

    @Override
    public ItemManager<T> toModelType() throws IllegalValueException {
        ItemManager<T> manager = new ItemManager<>();
        for (F item : items) {
            T modelItem = item.toModelType();
            if (manager.hasItem(modelItem)) {
                throw new IllegalValueException(
                        MESSAGE_DUPLICATE_ITEMS + ": " + modelItem
                );
            }
            manager.addItem(modelItem);
        }
        return manager;
    }
}
