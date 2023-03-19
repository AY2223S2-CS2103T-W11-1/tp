package seedu.address.storage.json;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * A model that has been adapted to be stored in a JSON file.
 *
 * @param <T> The type of the model.
 */
public interface JsonAdaptedModel<T> {
    /**
     * Converts this JsonAdaptedModel into the model's {@code Identifiable}
     * object.
     *
     * @throws IllegalValueException if there were any data constraints
     *                               violated in the adapted item.
     */
    T toModelType() throws IllegalValueException;
}
