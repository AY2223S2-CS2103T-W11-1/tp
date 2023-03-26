package seedu.address.model.crew;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import seedu.address.model.item.Item;
import seedu.address.model.location.CrewLocationType;

/**
 * Represents a Crew in the Wingman app.
 */
public class Crew implements Item {

    /**
     * The shape of the link between a flight and its crew
     */
    public static final Map<FlightCrewType, Integer> SHAPE =
            Map.of(FlightCrewType.CABIN_SERVICE_DIRECTOR, 1,
                    FlightCrewType.SENIOR_FLIGHT_ATTENDANT, 1,
                    FlightCrewType.FLIGHT_ATTENDANT, 12,
                    FlightCrewType.TRAINEE, 4
            );
    public static final Map<CrewLocationType, Integer> SHAPE_FOR_LOCATION =
            Map.of(
                    CrewLocationType.LOCATION_USING, 1000000
            );
    private static final String ID_STRING = "ID";
    private static final String NAME_STRING = "Name";
    private static final String RANK_STRING = "Rank";
    private static final String AVAILABILITY_STRING = "Status";
    private final String id;
    private final String name;
    private final CrewRank rank;
    private boolean isAvailable;

    /**
     * Creates a crew with a random UUID as its id.
     *
     * @param name the name of the crew.
     * @param rank the rank of the crew.
     */
    public Crew(String name, CrewRank rank) {
        this(UUID.randomUUID().toString(), name, rank);
    }

    /**
     * Creates a crew.
     *
     * @param id   the id of the crew.
     * @param name the name of the crew.
     * @param rank the rank of the crew.
     */
    public Crew(String id, String name, CrewRank rank) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.isAvailable = true;
    }

    /**
     * Returns the name of the crew.
     *
     * @return the name of the crew.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the rank of the crew.
     *
     * @return the rank of the crew.
     */
    public CrewRank getRank() {
        return rank;
    }

    /**
     * Returns the id of the crew.
     *
     * @return the id of the crew.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the availability of the crew.
     *
     * @return the availability of the crew.
     */
    public boolean isAvailable() {
        return this.isAvailable;
    }

    /**
     * Sets the availability of the crew to unavailable.
     */
    public void setUnavailable() {
        this.isAvailable = false;
    }

    /**
     * Sets the availability of the crew to available.
     */
    public void setAvailable() {
        this.isAvailable = true;
    }

    /**
     * Returns a String corresponding to the availability of the crew.
     *
     * @return the availability of the crew as a String
     */
    public String getAvailabilityString() {
        return (this.isAvailable)
                ? "Available"
                : "Unavailable";
    }

    @Override
    public List<String> getDisplayList() {
        return List.of(
                String.format("%s: %s", NAME_STRING, name),
                String.format("%s: %s", RANK_STRING, rank),
                String.format("%s: %s", AVAILABILITY_STRING, getAvailabilityString())
        );
    }

    @Override
    public String toString() {
        return String.format("%s %s", rank, name);
    }
}
