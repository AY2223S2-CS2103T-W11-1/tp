package wingman.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wingman.model.flight.Flight;

/**
 * A generic view for a flight.
 */
public class FlightCard extends UiPart<VBox> {
    private static final String FXML = "FlightCard.fxml";
    private final Flight flight;
    private final int displayedIndex;

    @FXML
    private VBox cardPane;

    @FXML
    private Label id;

    /**
     * Creates a view for the given flight. The flight is an identifiable object
     * that can be displayed in a list.
     *
     * @param flight The flight to be displayed.
     */
    public FlightCard(Flight flight, int displayedIndex) {
        super(FXML);
        this.flight = flight;
        this.displayedIndex = displayedIndex - 1;
        id.setText(displayedIndex + ". ");
        for (String line : flight.getDisplayList()) {
            Label label = new Label(line);
            label.setWrapText(true);
            cardPane.getChildren().add(label);
        }
    }

    /**
     * Returns the flight that is being displayed.
     *
     * @return The flight that is being displayed.
     */
    public Flight getFlight() {
        return flight;
    }
}
