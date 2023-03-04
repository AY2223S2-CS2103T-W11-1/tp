package seedu.address.ui.core;

import javafx.scene.control.ListCell;
import seedu.address.model.item.Identifiable;

public class ItemListCell<T extends Identifiable> extends ListCell<T> {
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