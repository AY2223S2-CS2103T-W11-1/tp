package seedu.address.model.flight;

import java.util.List;
import java.util.UUID;

import seedu.address.model.flight.exceptions.LinkedPlaneNotFoundException;
import seedu.address.model.item.Item;
import seedu.address.model.location.Location;
import seedu.address.model.plane.Plane;

/**
 * Represents a flight object in wingman
 */
public class Flight implements Item {
    private static final String UUID_STRING = "UUID";
    private static final String CODE_STRING = "Code";
    private static final String DEPARTURE_STRING = "Depart from";
    private static final String ARRIVE_STRING = "Arrive at";

    private final String code;
    private final String id;
    private Plane plane;

    //TODO: Add departure and arrival locations
    private Location departureLocation;
    private Location arrivalLocation;

    //TODO: Add exceptions to ensure departure and arrival locations are distinct

    /**
     * Creates a flight with a random UUID as its id
     *
     * @param code the code of the flight
     */
    public Flight(String code) {
        this.code = code;
        this.id = UUID.randomUUID().toString();
        this.plane = null;
    }

    /**
     * Creates a flight with a given id as its id
     *
     * @param id the id for the flight
     * @param code the code of the flight
     */
    public Flight(String id, String code) {
        this.code = code;
        this.id = id;
        this.plane = null;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public List<String> getDisplayList() {
        return List.of(
                String.format("%s: %s", UUID_STRING, id),
                String.format("%s: %s", CODE_STRING, code),
                String.format("%s: %s", DEPARTURE_STRING, getDepartureLocationName()),
                String.format("%s: %s", ARRIVE_STRING, getArrivalLocationName())
        );
    }

    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Links a plane to this flight.
     * @param plane The plane to be linked.
     */
    public void linkPlane(Plane plane) {
        this.plane = plane;
    }

    /**
     * Unlinks any plane from this flight.
     */
    public void unlinkPlane() {
        this.plane = null;
    }

    public boolean hasLinkedPlane() {
        return this.plane != null;
    }

    /**
     * If there's a linked plane, it returns it.
     * @return The linked plane.
     * @throws LinkedPlaneNotFoundException If this flight doesn't have a linked plane.
     */
    public Plane getLinkedPlane() throws LinkedPlaneNotFoundException {
        if (this.hasLinkedPlane()) {
            return this.plane;
        } else {
            throw new LinkedPlaneNotFoundException();
        }
    }

    /**
     * Links the flight to a departure location.
     * @param departureLocation the departure location to link to
     */
    public void linkDepartureLocation(Location departureLocation) {
        this.departureLocation = departureLocation;
    }

    /**
     * Links the flight to an arrival location.
     * @param arrivalLocation the arrival location to link to
     */
    public void linkArrivalLocation(Location arrivalLocation) {
        this.arrivalLocation = arrivalLocation; 
    }

    /**
     * Unlinks the departure location.
     */
    public void unLinkDepartureLocation() {
        this.departureLocation = null;
    }

    /**
     * Unlinks the arrival location.
     */
    public void unLinkArrivalLocation() {
        this.arrivalLocation = null;
    }

    /**
     * Returns the departure location of the plane.
     * @return the departure location.
     */
    public Location getDepartureLocation() {
        return departureLocation;
    }

    /**
     * Returns the arrival location of the plane.
     * @return the arrival location
     */
    public Location getArrivalLocation() {
        return arrivalLocation;
    }

    private String getDepartureLocationName() {
        if (departureLocation == null) {
            return "null";
        } else {
            return departureLocation.getName();
        }
    }

    private String getArrivalLocationName() {
        if (arrivalLocation == null) {
            return "null";
        } else {
            return arrivalLocation.getName();
        }
    }
}
