package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.crew.Crew;
import seedu.address.model.flight.Flight;
import seedu.address.model.flight.exceptions.FlightNotFoundException;
import seedu.address.model.item.Item;
import seedu.address.model.location.Location;
import seedu.address.model.location.exceptions.LocationNotFoundException;
import seedu.address.model.pilot.Pilot;
import seedu.address.model.plane.Plane;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final UserPrefs userPrefs;

    // pilot manager
    private final ItemManager<Pilot> pilotManager;
    private final FilteredList<Pilot> filteredPilots;
    // TODO: migrate this to the ui layer -> this probably should be there.

    // location manager
    private final ItemManager<Location> locationManager;
    private final FilteredList<Location> filteredLocations;

    // crew manager
    private final ItemManager<Crew> crewManager;
    private final FilteredList<Crew> filteredCrew;

    // plane manager
    private final ItemManager<Plane> planeManager;
    private final FilteredList<Plane> filteredPlanes;

    // flight manager
    private final ItemManager<Flight> flightManager;
    private final FilteredList<Flight> filteredFlights;

    // general utilities
    private final ObservableList<Item> itemsList;
//    private final ObservableList<Flight> flightList;
    private final ObservableList<Flight> flightList;
    private Optional<ObservableList<? extends Item>> lastBoundList = Optional.empty();

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyUserPrefs userPrefs,
                        ReadOnlyItemManager<Pilot> pilotManager,
                        ReadOnlyItemManager<Location> locationManager,
                        ReadOnlyItemManager<Crew> crewManager,
                        ReadOnlyItemManager<Plane> planeManager,
                        ReadOnlyItemManager<Flight> flightManager) {
        requireAllNonNull(userPrefs);

        logger.fine("Initializing with Wingman storage(pilot, location, crew, plane, flight)"
                + " and user prefs " + userPrefs);

        this.userPrefs = new UserPrefs(userPrefs);

        this.pilotManager = new ItemManager<>(pilotManager);
        filteredPilots = new FilteredList<>(this.pilotManager.getItemList());

        this.locationManager = new ItemManager<>(locationManager);
        filteredLocations = new FilteredList<>(this.locationManager.getItemList());

        this.crewManager = new ItemManager<>(crewManager);
        filteredCrew = new FilteredList<>(this.crewManager.getItemList());

        this.planeManager = new ItemManager<>(planeManager);
        filteredPlanes = new FilteredList<>(this.planeManager.getItemList());

        this.flightManager = new ItemManager<>(flightManager);
        filteredFlights = new FilteredList<>(this.flightManager.getItemList());

        itemsList = FXCollections.observableArrayList();
//        flightList = FXCollections.observableArrayList();
        flightList = new FilteredList<>(filteredFlights);
        setOperationMode(userPrefs.getOperationMode());
    }

    /**
     * Initializes a ModelManager
     */
    public ModelManager() {
        this(new UserPrefs(), new ItemManager<>(),
                new ItemManager<>(), new ItemManager<>(),
                new ItemManager<>(), new ItemManager<>());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public OperationMode getOperationMode() {
        return userPrefs.getOperationMode();
    }

    /**
     * Sets the operation mode of the app.
     *
     * @param mode the new operation mode
     */
    @Override
    public void setOperationMode(OperationMode mode) {
        this.userPrefs.setOperationMode(mode);
        switch (mode) {
        case PILOT:
            rebind(filteredPilots);
            break;
        case PLANE:
            rebind(filteredPlanes);
            break;
        case FLIGHT:
            rebind(filteredFlights);
            break;
        case CREW:
            rebind(filteredCrew);
            break;
        case LOCATION:
            rebind(filteredLocations);
            break;
        default:
            logger.warning("Unknown operation mode: " + mode);
            break;
        }
    }

    private void rebind(ObservableList<? extends Item> list) {
        if (lastBoundList.isPresent()) {
            final ObservableList<? extends Item> lastBound =
                    lastBoundList.get();
            Bindings.unbindContent(itemsList, lastBound);
        }
        Bindings.bindContent(itemsList, list);
        lastBoundList = Optional.of(list);
    }

    @Override
    public ObservableList<Item> getItemsList() {
        return itemsList;
    }

    @Override
    public ObservableList<Flight> getFlightList() {
        return flightList;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }


    //=========== Pilot ========================================================

    @Override
    public ReadOnlyItemManager<Pilot> getPilotManager() {
        return pilotManager;
    }

    @Override
    public Path getPilotManagerFilePath() {
        return userPrefs.getPilotManagerFilePath();
    }

    @Override
    public void setPilotManagerFilePath(Path pilotManagerFilePath) {
        requireNonNull(pilotManagerFilePath);
        userPrefs.setPilotManagerFilePath(pilotManagerFilePath);
    }

    @Override
    public void setPilotManager(ReadOnlyItemManager<Pilot> pilotManager) {
        this.pilotManager.resetData(pilotManager);
    }

    @Override
    public boolean hasPilot(Pilot pilot) {
        requireNonNull(pilot);
        return pilotManager.hasItem(pilot);
    }

    @Override
    public void deletePilot(Pilot target) {
        pilotManager.removeItem(target);
    }

    @Override
    public void deletePilot(String id) {
        pilotManager.removeItem(id);
    }

    @Override
    public void addPilot(Pilot pilot) {
        requireNonNull(pilot);
        pilotManager.addItem(pilot);
    }

    @Override
    public void setPilot(Pilot target, Pilot editedPilot) {
        requireAllNonNull(target, editedPilot);
        pilotManager.setItem(target, editedPilot);
    }

    @Override
    public ObservableList<Pilot> getFilteredPilotList() {
        return filteredPilots;
    }

    @Override
    public void updateFilteredPilotList(Predicate<Pilot> predicate) {
        requireNonNull(predicate);
        filteredPilots.setPredicate(predicate);
    }


    //=========== Location ========================================================

    @Override
    public Location getLocationById(String id) {
        Location locationToFind = null;
        for (Location location: getFilteredLocationList()) {
            if (location.getId().equals(id)) {
                locationToFind = location;
            }
        }
        if (locationToFind == null) {
            throw new LocationNotFoundException();
        }
        return locationToFind;
    }

    @Override
    public ReadOnlyItemManager<Location> getLocationManager() {
        return locationManager;
    }

    @Override
    public Path getLocationManagerFilePath() {
        return userPrefs.getPilotManagerFilePath();
    }

    @Override
    public void setLocationManagerFilePath(Path locationManagerFilePath) {
        requireNonNull(locationManagerFilePath);
        userPrefs.setPilotManagerFilePath(locationManagerFilePath);
    }

    @Override
    public void setLocationManager(ReadOnlyItemManager<Location> locationManager) {
        this.locationManager.resetData(locationManager);
    }

    @Override
    public boolean hasLocation(Location location) {
        requireNonNull(location);
        return locationManager.hasItem(location);
    }

    public void deleteLocation(Location location) {
        locationManager.removeItem(location);
    }

    @Override
    public void deleteLocation(String id) {
        locationManager.removeItem(id);
    }

    @Override
    public void addLocation(Location location) {
        requireNonNull(location);
        locationManager.addItem(location);
    }

    @Override
    public void setLocation(Location target, Location editedLocation) {
        requireAllNonNull(target, editedLocation);
        locationManager.setItem(target, editedLocation);
    }

    @Override
    public ObservableList<Location> getFilteredLocationList() {
        return filteredLocations;
    }

    @Override
    public void updateFilteredLocationList(Predicate<Location> predicate) {
        requireNonNull(predicate);
        filteredLocations.setPredicate(predicate);
    }

    @Override
    public void linkFlightToLocations(Flight flight, Location departureLocation, Location arrivalLocation) {
        requireAllNonNull(flight, departureLocation, arrivalLocation);
        flight.linkDepartureLocation(departureLocation);
        flight.linkArrivalLocation(arrivalLocation);
    }

    @Override
    public void unlinkFlightToLocations(Flight flight) {
        requireNonNull(flight);
        flight.unLinkArrivalLocation();
        flight.unLinkDepartureLocation();
    }

    //=========== Crew ========================================================

    @Override
    public void setCrewManager(ReadOnlyItemManager<Crew> crewManager) {
        requireNonNull(crewManager);
        this.crewManager.resetData(crewManager);
    }

    @Override
    public ReadOnlyItemManager<Crew> getCrewManager() {
        return crewManager;
    }

    @Override
    public Path getCrewManagerFilePath() {
        return userPrefs.getCrewManagerFilePath();
    }

    @Override
    public void setCrewManagerFilePath(Path crewManagerFilePath) {
        requireNonNull(crewManagerFilePath);
        userPrefs.setCrewManagerFilePath(crewManagerFilePath);
    }

    @Override
    public boolean hasCrew(Crew crew) {
        requireNonNull(crew);
        return this.crewManager.hasItem(crew);
    }

    @Override
    public boolean hasCrew(String id) {
        requireNonNull(id);
        return this.crewManager.hasItem(id);
    }

    @Override
    public void addCrew(Crew crew) {
        requireNonNull(crew);
        crewManager.addItem(crew);
    }

    @Override
    public void deleteCrew(Crew crew) {
        crewManager.removeItem(crew);
    }

    @Override
    public void deleteCrew(String id) {
        crewManager.removeItem(id);
    }

    @Override
    public void setCrew(Crew target, Crew editedCrew) {
        requireAllNonNull(target, editedCrew);
        crewManager.setItem(target, editedCrew);
    }

    @Override
    public ObservableList<Crew> getFilteredCrewList() {
        return filteredCrew;
    }

    @Override
    public void updateFilteredCrewList(Predicate<Crew> predicate) {
        requireNonNull(predicate);
        filteredCrew.setPredicate(predicate);
    }


    //=========== Plane ========================================================

    @Override
    public void setPlaneManager(ReadOnlyItemManager<Plane> planeManager) {
        this.planeManager.resetData(planeManager);
    }

    @Override
    public ReadOnlyItemManager<Plane> getPlaneManager() {
        return this.planeManager;
    }

    @Override
    public void addPlane(Plane plane) {
        requireAllNonNull(plane);
        planeManager.addItem(plane);
    }

    @Override
    public void deletePlane(Plane plane) {
        planeManager.removeItem(plane);
    }

    @Override
    public void deletePlane(String id) {
        planeManager.removeItem(id);
    }

    @Override
    public boolean hasPlane(Plane plane) {
        requireNonNull(plane);
        return planeManager.hasItem(plane);
    }

    @Override
    public boolean hasPlane(String id) {
        return planeManager.hasItem(id);
    }

    @Override
    public void setPlane(Plane target, Plane editedPlane) {
        requireAllNonNull(target, editedPlane);
        planeManager.setItem(target, editedPlane);
    }

    @Override
    public ObservableList<Plane> getFilteredPlaneList() {
        return filteredPlanes;
    }

    @Override
    public void updateFilteredPlaneList(Predicate<Plane> predicate) {
        requireNonNull(predicate);
        filteredPlanes.setPredicate(predicate);
    }


    //=========== Flight ========================================================

    @Override
    public Flight getFlightById(String id) {
        Flight flightToFind = null;
        for (Flight flight: getFilteredFlightList()) {
            if (flight.getId().equals(id)) {
                flightToFind = flight;
            }
        }
        if (flightToFind == null) {
            throw new FlightNotFoundException();
        }
        return flightToFind;
    }

    @Override
    public ReadOnlyItemManager<Flight> getFlightManager() {
        return flightManager;
    };
    @Override
    public Path getFlightManagerFilePath() {
        return userPrefs.getFlightManagerFilePath();
    };
    @Override
    public void setFlightManagerFilePath(Path flightManagerFilePath) {
        requireNonNull(flightManagerFilePath);
        userPrefs.setFlightManagerFilePath(flightManagerFilePath);
    };
    @Override
    public void setFlightManager(ReadOnlyItemManager<Flight> flightManager) {
        this.flightManager.resetData(flightManager);
    };
    @Override
    public boolean hasFlight(Flight flight) {
        requireNonNull(flight);
        return flightManager.hasItem(flight);
    };
    @Override
    public void deleteFlight(Flight target) {
        flightManager.removeItem(target);
    };
    @Override
    public void deleteFlight(String id) {
        flightManager.removeItem(id);
    };
    @Override
    public void addFlight(Flight flight) {
        requireNonNull(flight);
        flightManager.addItem(flight);
    };
    @Override
    public void setFlight(Flight target, Flight editedFlight) {
        requireAllNonNull(target, editedFlight);
        flightManager.setItem(target, editedFlight);
    };

    @Override
    public void linkPlane(Flight flight, Plane plane) {
        flight.linkPlane(plane);
    }

    @Override
    public void unlinkPlane(Flight flight) {
        flight.unlinkPlane();
    }

    @Override
    public ObservableList<Flight> getFilteredFlightList() {
        return filteredFlights;
    };

    @Override
    public void updateFilteredFlightList(Predicate<Flight> predicate) {
        requireNonNull(predicate);
        filteredFlights.setPredicate(predicate);
    };


    //=========== Generic ========================================================

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return userPrefs.equals(other.userPrefs)
                && pilotManager.equals(other.pilotManager)
                && crewManager.equals(other.crewManager)
                && planeManager.equals(other.planeManager)
                && flightManager.equals(other.flightManager)
                && locationManager.equals(other.locationManager);
    }
}
