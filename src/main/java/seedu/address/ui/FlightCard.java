package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seedu.address.model.flight.Flight;
import seedu.address.ui.UiPart;

/**
 * A generic view for a flight.
 */
public class FlightCard extends UiPart<VBox> {
    private static final String FXML = "FlightCard.fxml";
    private final Flight flight;

    @FXML
    private VBox cardPane;

    /**
     * Creates a view for the given flight. The flight is an identifiable object
     * that can be displayed in a list.
     *
     * @param flight The flight to be displayed.
     */
    public FlightCard(Flight flight) {
        super(FXML);
        this.flight = flight;
        for (String line : flight.getDisplayList()) {
            Label label = new Label(line);
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
