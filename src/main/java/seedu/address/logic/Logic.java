package seedu.address.logic;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.core.CommandResult;
import seedu.address.logic.core.exceptions.CommandException;
import seedu.address.logic.core.exceptions.ParseException;
import seedu.address.model.OperationMode;
import seedu.address.model.flight.Flight;
import seedu.address.model.item.Item;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException   If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Gets the filtered list of items.
     *
     * @return the filtered list of items.
     */
    ObservableList<Item> getFilteredItemList();

    /**
     * Gets the current operation mode.
     *
     * @return the current operation mode.
     */
    OperationMode getOperationMode();

    /**
     * Gets the filtered list of flights.
     *
     * @return the filtered list of flights.
     */
    ObservableList<Flight> getFilteredFlightList();
}
