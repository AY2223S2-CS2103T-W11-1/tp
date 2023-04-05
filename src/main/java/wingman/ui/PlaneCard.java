package wingman.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wingman.model.plane.Plane;

/**
 * A generic view for a plane.
 */
public class PlaneCard extends UiPart<VBox> {
    private static final String FXML = "PlaneCard.fxml";
    private final Plane plane;
    private final int displayedIndex;

    @FXML
    private VBox cardPane;

    @FXML
    private Label id;

    /**
     * Creates a view for the given plane. The plane is an identifiable object
     * that can be displayed in a list.
     *
     * @param plane The plane to be displayed.
     */
    public PlaneCard(Plane plane, int displayedIndex) {
        super(FXML);
        this.plane = plane;
        this.displayedIndex = displayedIndex;
        id.setText(displayedIndex + ". ");
        for (String line : plane.getDisplayList()) {
            Label label = new Label(line);
            label.setWrapText(true);
            cardPane.getChildren().add(label);
        }
    }

    /**
     * Returns the plane that is being displayed.
     *
     * @return The plane that is being displayed.
     */
    public Plane getPlane() {
        return plane;
    }
}
