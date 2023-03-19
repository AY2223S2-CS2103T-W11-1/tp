package seedu.address.storage.json.adapted;

import java.util.Deque;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.GetUtil;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.flight.Flight;
import seedu.address.model.link.Link;
import seedu.address.model.link.exceptions.LinkException;
import seedu.address.model.location.Location;
import seedu.address.model.pilot.FlightPilotType;
import seedu.address.model.pilot.Pilot;
import seedu.address.storage.json.JsonAdaptedModel;

/**
 * Represents a Jackson-friendly version of a Flight
 */
public class JsonAdaptedFlight implements JsonAdaptedModel<Flight> {
    public static final String MISSING_FIELD_MESSAGE_FORMAT =
            "Flight's %s field is missing!";

    /**
     * The id of the flight.
     */
    private final String id;

    /**
     * The code of the flight.
     */
    private final String code;

    /**
     * The plane linked to the flight.
     */
    private String plane;

    /**
     * Location related attributes.
     */
    private boolean hasLinkedLocations;
    private String departureLocationId;
    private String departureLocationName;
    private String arrivalLocationId;
    private String arrivalLocationName;

    private Map<FlightPilotType, Deque<String>> pilotLink;

    /**
     * Constructs a {@code JsonAdaptedFlight} with the given flight details.
     * This is intended for Jackson to use.
     *
     * @param id                    The id of the flight.
     * @param code                  The name of the flight.
     * @param hasLinkedLocations    Whether the flight is linked to locations.
     * @param departureLocationId   The id of the departure location.
     * @param departureLocationName The name of the departure location.
     * @param arrivalLocationId     The id of the arrival location.
     * @param arrivalLocationName   The name of the arrival location.
     */
    @JsonCreator
    public JsonAdaptedFlight(
            @JsonProperty("id") String id,
            @JsonProperty("code") String code,
            @JsonProperty("plane") String plane,
            @JsonProperty("hasLinkedLocations") boolean hasLinkedLocations,
            @JsonProperty("departureLocationId") String departureLocationId,
            @JsonProperty("departureLocationName") String departureLocationName,
            @JsonProperty("arrivalLocationId") String arrivalLocationId,
            @JsonProperty("arrivalLocationName") String arrivalLocationName,
            @JsonProperty("pilotLink") Map<FlightPilotType, Deque<String>> pilotLink
    ) {
        this.id = id;
        this.code = code;
        this.plane = plane;
        this.hasLinkedLocations = hasLinkedLocations;
        this.departureLocationId = departureLocationId;
        this.departureLocationName = departureLocationName;
        this.arrivalLocationId = arrivalLocationId;
        this.arrivalLocationName = arrivalLocationName;
        this.pilotLink = pilotLink;
    }

    /**
     * Converts a given {@code Flight} into this class for Jackson use.
     *
     * @param flight The flight to be converted.
     */
    public JsonAdaptedFlight(Flight flight) {
        this.id = flight.getId();
        this.code = flight.getCode();
        this.pilotLink = flight.pilotLink.getCopiedContents();

        if (flight.hasLinkedPlane()) {
            this.plane = flight.getLinkedPlane().getId();
        } else {
            this.plane = "";
        }

        if (flight.hasLinkedLocations()) {
            this.hasLinkedLocations = true;
            this.departureLocationName = flight.getDepartureLocationName();
            this.departureLocationId = flight.getDepartureLocationId();
            this.arrivalLocationName = flight.getDepartureLocationName();
            this.arrivalLocationId = flight.getArrivalLocationId();
        } else {
            this.hasLinkedLocations = false;
        }
    }

    @Override
    public Flight toModelType() throws IllegalValueException {
        if (id == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "id")
            );
        }
        if (code == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "code")
            );
        }

        Flight flight;
        try {
            Link<FlightPilotType, Pilot, ReadOnlyItemManager<Pilot>> link =
                    new Link<>(Pilot.SHAPE, pilotLink,
                            GetUtil
                                    .getLazy(Model.class)
                                    .map(Model::getPilotManager)
                    );
            flight = new Flight(id, code, link);
        } catch (LinkException e) {
            throw new IllegalValueException(e.getMessage());
        }
        if (hasLinkedLocations) {
            if (departureLocationId == null || departureLocationName == null
                        || arrivalLocationId == null || arrivalLocationName == null) {
                throw new IllegalValueException(
                        String.format(MISSING_FIELD_MESSAGE_FORMAT, "locations")
                );
            }
            Location departureLocation = new Location(
                    departureLocationId,
                    departureLocationName
            );
            Location arrivalLocation = new Location(
                    arrivalLocationId,
                    arrivalLocationName
            );
            flight.linkDepartureLocation(departureLocation);
            flight.linkArrivalLocation(arrivalLocation);
        }

        return flight;
    }
}
