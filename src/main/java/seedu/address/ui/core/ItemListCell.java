package seedu.address.ui.core;

import javafx.scene.control.ListCell;
import seedu.address.model.item.Item;

/**
 * The cell that displays the item in the list.
 *
 * @param <T> The type of item to display.
 */
public class ItemListCell<T extends Item> extends ListCell<T> {

    private static final String FXML = "ItemListCard.fxml";

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            ItemCard card = new ItemCard(item);
            setGraphic(card.getRoot());
        }
    }
}
