package wingman.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wingman.model.location.Location;

/**
 * A generic view for a location.
 */
public class LocationCard extends UiPart<VBox> {
    private static final String FXML = "LocationCard.fxml";
    private final Location location;
    private final int displayedIndex;

    @FXML
    private VBox cardPane;

    @FXML
    private Label id;

    /**
     * Creates a view for the given location. The location is an identifiable object
     * that can be displayed in a list.
     *
     * @param location The location to be displayed.
     */
    public LocationCard(Location location, int displayedIndex) {
        super(FXML);
        this.location = location;
        this.displayedIndex = displayedIndex - 1;
        id.setText(displayedIndex + ". ");
        for (String line : location.getDisplayList()) {
            Label label = new Label(line);
            label.setWrapText(true);
            cardPane.getChildren().add(label);
        }
    }

    /**
     * Returns the location that is being displayed.
     *
     * @return The location that is being displayed.
     */
    public Location getLocation() {
        return location;
    }
}
