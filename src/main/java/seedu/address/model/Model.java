package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.crew.Crew;
import seedu.address.model.flight.Flight;
import seedu.address.model.item.Item;
import seedu.address.model.location.Location;
import seedu.address.model.pilot.Pilot;
import seedu.address.model.plane.Plane;

/**
 * The API of the Model component.
 */
public interface Model {

    /**
     * {@code Predicate} that always evaluate to false
     */
    Predicate<Pilot> PREDICATE_SHOW_ALL_PILOTS = unused -> false;

    /**
     * {@code Predicate} that always evaluate to false
     */
    Predicate<Crew> PREDICATE_SHOW_ALL_CREW = unused -> false;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' operation mode.
     */
    OperationMode getOperationMode();

    /**
     * Sets the user prefs' operation mode to {@code operationMode}.
     *
     * @param operationMode The new operation mode.
     */
    void setOperationMode(OperationMode operationMode);

    /**
     * Returns the list of items.
     */
    ObservableList<Item> getItemsList();

    /**
     * Returns the list of flights.
     */
    ObservableList<Flight> getFlightList();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);


    // ================ Pilot methods ==============================

    /**
     * Returns the person manager.
     *
     * @return the person manager.
     */
    ReadOnlyItemManager<Pilot> getPilotManager();

    /**
     * Returns the pilot manager file path.
     *
     * @return the pilot manager file path.
     */
    Path getPilotManagerFilePath();

    /**
     * Sets the pilot manager file path.
     *
     * @param pilotManagerFilePath the pilot manager file path.
     */
    void setPilotManagerFilePath(Path pilotManagerFilePath);

    /**
     * Replaces pilot manager data with the data in {@code pilotManager}.
     *
     * @param pilotManager the pilot manager to replace with.
     */
    void setPilotManager(ReadOnlyItemManager<Pilot> pilotManager);

    /**
     * Returns true if a pilot with the same identity as {@code pilot} exists
     * in the address book.
     *
     * @param pilot the pilot to check.
     * @return true if the pilot exists, false otherwise.
     */
    boolean hasPilot(Pilot pilot);

    /**
     * Deletes the given pilot.
     * The pilot must exist in the address book.
     *
     * @param target the pilot to delete.
     */
    void deletePilot(Pilot target);

    /**
     * Deletes the pilot with the given id.
     */
    void deletePilot(String id);

    /**
     * Adds the given pilot.
     *
     * @param pilot the pilot to add.
     */
    void addPilot(Pilot pilot);

    /**
     * Replaces the given pilot {@code target} with {@code editedPilot}.
     * {@code target} must exist in the address book.
     * The pilot identity of {@code editedPilot} must not be the same as another existing pilot in the address book.
     *
     * @param target      the pilot to replace.
     * @param editedPilot the pilot to replace with.
     */
    void setPilot(Pilot target, Pilot editedPilot);

    /**
     * Returns an unmodifiable view of the filtered pilot list
     */
    ObservableList<Pilot> getFilteredPilotList();

    /**
     * Updates the filter of the filtered pilot list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPilotList(Predicate<Pilot> predicate);


    // ================ Location methods ==============================

    /**
     * Returns location object by location id
     * @param id the location id
     * @return the location object with the id
     */
    Location getLocationById(String id);

    /**
     * Returns true if the location is in the location list
     *
     * @param location a location object to be checked
     * @return true if the location has already been in the list
     */
    boolean hasLocation(Location location);

    /**
     * Delete a location from the location list
     *
     * @param uuid the id of the location to be deleted
     */
    void deleteLocation(String uuid);

    /**
     * Add a location to the location list
     *
     * @param location the location to be added
     */
    void addLocation(Location location);

    void setLocationManagerFilePath(Path pilotManagerFilePath);

    ReadOnlyItemManager<Location> getLocationManager();

    Path getLocationManagerFilePath();

    void setLocationManager(ReadOnlyItemManager<Location> locationManager);

    void setLocation(Location target, Location editedLocation);

    ObservableList<Location> getFilteredLocationList();

    void updateFilteredLocationList(Predicate<Location> predicate);

    void linkFlightToLocations(Flight flight, Location departureLocation, Location arrivalLocation);
    void unlinkFlightToLocations(Flight flight);

    // ================ Crew methods ==============================

    /**
     * Returns the crew manager file path.
     *
     * @return the crew manager file path.
     */
    Path getCrewManagerFilePath();

    /**
     * Sets the crew manager file path.
     *
     * @param crewManagerFilePath the crew manager file path.
     */
    void setCrewManagerFilePath(Path crewManagerFilePath);

    void setCrewManager(ReadOnlyItemManager<Crew> manager);

    /**
     * Returns the crew manager.
     *
     * @return the crew manager.
     */
    ReadOnlyItemManager<Crew> getCrewManager();

    boolean hasCrew(Crew crew);

    boolean hasCrew(String id);

    /**
     * Add a crew to the crew list.
     *
     * @param crew the crew to be added.
     */
    void addCrew(Crew crew);

    /**
     * Deletes a crew to the crew list.
     *
     * @param crew the crew to be deleted.
     */
    void deleteCrew(Crew crew);

    /**
     * Deletes a crew to the crew list.
     *
     * @param id the id of the crew to be deleted.
     */
    void deleteCrew(String id);

    void setCrew(Crew target, Crew editedCrew);

    void updateFilteredCrewList(Predicate<Crew> predicate);

    ObservableList<Crew> getFilteredCrewList();


    // ================ Plane methods ==============================

    void setPlaneManager(ReadOnlyItemManager<Plane> manager);
    ReadOnlyItemManager<Plane> getPlaneManager();
    void addPlane(Plane plane);
    void deletePlane(Plane plane);
    void deletePlane(String id);
    boolean hasPlane(Plane plane);
    boolean hasPlane(String id);
    void setPlane(Plane target, Plane editedPlane);
    ObservableList<Plane> getFilteredPlaneList();
    void updateFilteredPlaneList(Predicate<Plane> predicate);


    // ================ Flight methods ==============================

    /**
     * Returns flight object by flight id
     * @param id the flight id
     * @return the flight object with the id
     */
    Flight getFlightById(String id);

    /**
     * Returns the flight manager
     *
     * @return flight manager
     */
    ReadOnlyItemManager<Flight> getFlightManager();

    /**
     * Returns the flight manager file path
     *
     * @return flight manager file path
     */
    Path getFlightManagerFilePath();

    /**
     * Sets the flight manager file path
     *
     * @param flightManagerFilePath flight manager file path
     */
    void setFlightManagerFilePath(Path flightManagerFilePath);

    /**
     * Replaces the current flight manager's data with data in {@code flightManager}
     *
     * @param flightManager the flight manager to replace with
     */
    void setFlightManager(ReadOnlyItemManager<Flight> flightManager);

    /**
     * Returns true if a flight with the same identity as {@code flight} exists in Wingman
     *
     * @param flight
     * @return true if flight exists, false otherwise
     */
    boolean hasFlight(Flight flight);

    /**
     * Deletes the given flight, if the flight exists in Wingman
     *
     * @param target flight to be deleted
     */
    void deleteFlight(Flight target);

    /**
     * Deletes the flight with the given id
     *
     * @param id identifier of flight to be deleted
     */
    void deleteFlight(String id);

    /**
     * Adds the given flight
     *
     * @param flight flight to be added
     */
    void addFlight(Flight flight);

    /**
     * Replaces the given flight {@code target} with {@code editedFlight}
     * {@code target} must exist in Wingman
     * The flight identity of {@code editedFlight} must not be the same as another existing flight in Wingman
     *
     * @param target the flight to be replaced
     * @param editedFlight the flight to replace with
     */
    void setFlight(Flight target, Flight editedFlight);

    /**
     * Links a plane to a flight.
     * @param flight The flight to be linked to.
     * @param plane  The plane to be linked to a flight.
     */
    void linkPlane(Flight flight, Plane plane);

    /**
     * Unlinks a flight from any plane.
     * @param flight The flight to be unlinked from.
     */
    void unlinkPlane(Flight flight);

    /**
     * Returns an unmodifiable view of the filtered flight list
     * @return unmodifiable view of the filtered flight list
     */
    ObservableList<Flight> getFilteredFlightList();

    /**
     * Updates the filter of the filtered flight list to filter by the given {@code predicate}
     *
     * @param predicate the new predicate to use
     * @throws NullPointerException if {@code predicate} is null
     */
    void updateFilteredFlightList(Predicate<Flight> predicate);
}
