package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path pilotManagerFilePath = Paths.get(
            "data",
            "pilotmanager.json"
    );
    private Path locationManagerFilePath = Paths.get(
            "data",
            "locationmanager.json"
    );
    private Path crewManagerFilePath = Paths.get(
            "data", "crewmanager.json"
    );
    private Path planeManagerFilePath = Paths.get(
            "data", "planemanager.json"
    );
    private Path flightManagerFilePath = Paths.get(
            "data",
            "flightmanager.json"
    );

    private int operationModeId = 0;

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {
    }

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setAddressBookFilePath(newUserPrefs.getAddressBookFilePath());
        setPilotManagerFilePath(newUserPrefs.getPilotManagerFilePath());
        setLocationManagerFilePath(newUserPrefs.getLocationManagerFilePath());
        setCrewManagerFilePath(newUserPrefs.getCrewManagerFilePath());
        setPlaneManagerFilePath(newUserPrefs.getPlaneManagerFilePath());
        setFlightManagerFilePath(newUserPrefs.getFlightManagerFilePath());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        this.addressBookFilePath = addressBookFilePath;
    }


    // =================== OperationMode ===================

    /**
     * Returns the operation mode of the application.
     *
     * @return the operation mode of the application
     */
    public OperationMode getOperationMode() {
        return OperationMode.fromInt(operationModeId);
    }

    /**
     * Sets the operation mode of the application.
     *
     * @param operationMode the new operation mode of the application
     */
    public void setOperationMode(OperationMode operationMode) {
        requireNonNull(operationMode);
        this.operationModeId = operationMode.toInt();
    }


    // =================== PilotManager ===================

    @Override
    public Path getPilotManagerFilePath() {
        return this.pilotManagerFilePath;
    }

    @Override
    public void setPilotManagerFilePath(Path pilotManagerFilePath) {
        requireNonNull(pilotManagerFilePath);
        this.pilotManagerFilePath = pilotManagerFilePath;
    }


    // =================== LocationManager ===================

    @Override
    public Path getLocationManagerFilePath() {
        return locationManagerFilePath;
    }

    @Override
    public void setLocationManagerFilePath(Path locationManagerFilePath) {
        requireNonNull(locationManagerFilePath);
        this.locationManagerFilePath = locationManagerFilePath;
    }


    // =================== CrewManager ===================

    @Override
    public Path getCrewManagerFilePath() {
        return this.crewManagerFilePath;
    }

    /**
     * Sets the user prefs' crew manager file path.
     *
     * @param crewManagerFilePath the new crew manager file path
     */
    public void setCrewManagerFilePath(Path crewManagerFilePath) {
        requireNonNull(crewManagerFilePath);
        this.crewManagerFilePath = crewManagerFilePath;
    }


    // =================== PlaneManager ===================

    @Override
    public Path getPlaneManagerFilePath() {
        return this.planeManagerFilePath;
    }

    @Override
    public void setPlaneManagerFilePath(Path planeManagerFilePath) {
        requireNonNull(planeManagerFilePath);
        this.planeManagerFilePath = planeManagerFilePath;
    }


    // =================== FlightManager ===================

    @Override
    public Path getFlightManagerFilePath() {
        return this.flightManagerFilePath;
    }

    @Override
    public void setFlightManagerFilePath(Path flightManagerFilePath) {
        requireNonNull(flightManagerFilePath);
        this.flightManagerFilePath = flightManagerFilePath;
    }


    // =================== Generic ===================

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
                && addressBookFilePath.equals(o.addressBookFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + addressBookFilePath);
        return sb.toString();
    }
}
