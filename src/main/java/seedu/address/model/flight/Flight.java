package seedu.address.model.flight;

import java.util.List;
import java.util.UUID;

import seedu.address.commons.util.GetUtil;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.crew.Crew;
import seedu.address.model.crew.FlightCrewType;
import seedu.address.model.item.Item;
import seedu.address.model.link.Link;
import seedu.address.model.link.exceptions.LinkException;
import seedu.address.model.location.FlightLocationType;
import seedu.address.model.location.Location;
import seedu.address.model.pilot.FlightPilotType;
import seedu.address.model.pilot.Pilot;
import seedu.address.model.plane.FlightPlaneType;
import seedu.address.model.plane.Plane;

/**
 * Represents a flight object in wingman
 */
public class Flight implements Item {
    private static final String UUID_STRING = "UUID";
    private static final String CODE_STRING = "Code";
    public final Link<FlightPilotType, Pilot, ReadOnlyItemManager<Pilot>> pilotLink;
    public final Link<FlightCrewType, Crew, ReadOnlyItemManager<Crew>> crewLink;
    public final Link<FlightPlaneType, Plane, ReadOnlyItemManager<Plane>> planeLink;
    public final Link<FlightLocationType, Location, ReadOnlyItemManager<Location>> locationLink;
    private final String code;
    private final String id;

    //TODO: Add exceptions to ensure departure and arrival locations are distinct

    /**
     * Creates a new flight
     *
     * @param id                the id of the  flight
     * @param code              the code
     * @param pilotLink         the link to the pilot
     * @param crewLink          the link to the crew
     * @param planeLink         the link to the plane
     * @param locationLink      the link to the location
     * @throws LinkException if the link cannot be created
     */
    public Flight(
            String id,
            String code,
            Link<FlightPilotType, Pilot, ReadOnlyItemManager<Pilot>> pilotLink,
            Link<FlightCrewType, Crew, ReadOnlyItemManager<Crew>> crewLink,
            Link<FlightPlaneType, Plane, ReadOnlyItemManager<Plane>> planeLink,
            Link<FlightLocationType, Location, ReadOnlyItemManager<Location>> locationLink
    ) throws LinkException {
        this.id = id;
        this.code = code;
        this.pilotLink = pilotLink;
        this.crewLink = crewLink;
        this.planeLink = planeLink;
        this.locationLink = locationLink;
    }

    /**
     * Creates a flight with a random UUID as its id
     *
     * @param code the code of the flight
     */
    public Flight(String code) throws LinkException {
        this(UUID.randomUUID().toString(), code,
                new Link<>(
                Pilot.SHAPE,
                GetUtil.getLazy(Model.class).map(Model::getPilotManager)
                ), new Link<>(
                        Crew.SHAPE,
                        GetUtil.getLazy(Model.class).map(Model::getCrewManager)
                ), new Link<>(
                        Plane.SHAPE,
                        GetUtil.getLazy(Model.class).map(Model::getPlaneManager)
                ), new Link<>(
                        Location.SHAPE,
                        GetUtil.getLazy(Model.class).map(Model::getLocationManager)
                ));
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public List<String> getDisplayList() {
        return List.of(
                String.format("%s: %s", UUID_STRING, id),
                String.format("%s: %s", CODE_STRING, code),
                String.format("%s: %s", "Pilots", pilotLink.toString()),
                String.format("%s: %s", "Crews", crewLink.toString()),
                String.format("%s: %s", "Plane", planeLink.toString()),
                String.format("%s: %s", "Locations", locationLink.toString())
        );
    }

    @Override
    public String getId() {
        return this.id;
    }

}
