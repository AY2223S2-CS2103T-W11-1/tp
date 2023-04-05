package wingman.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wingman.model.crew.Crew;

/**
 * A generic view for crew.
 */
public class CrewCard extends UiPart<VBox> {
    private static final String FXML = "CrewCard.fxml";
    private final Crew crew;
    private final int displayedIndex;

    @FXML
    private VBox cardPane;

    @FXML
    private Label id;

    /**
     * Creates a view for the given crew. The crew is an identifiable object
     * that can be displayed in a list.
     * @param crew The crew to be displayed.
     */
    public CrewCard(Crew crew, int displayedIndex) {
        super(FXML);
        this.crew = crew;
        this.displayedIndex = displayedIndex - 1;
        id.setText(displayedIndex + ". ");
        for (String line : crew.getDisplayList()) {
            Label label = new Label(line);
            label.setWrapText(true);
            cardPane.getChildren().add(label);
        }
    }

    /**
     * Returns the crew that is being displayed.
     *
     * @return The crew that is being displayed.
     */
    public Crew getCrew() {
        return crew;
    }
}
