package seedu.address.model.pilot;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import seedu.address.model.item.Item;

/**
 * Represents a Pilot in the Wingman app.
 */
public class Pilot implements Item {
    public static final Map<FlightPilotType, Integer> SHAPE =
            Map.of(FlightPilotType.PILOT_FLYING, 1,
                    FlightPilotType.PILOT_MONITORING, 1
            );
    private static final String UUID_STRING = "UUID";

    private static final String NAME_STRING = "Name";

    private static final String GENDER_STRING = "Gender";

    private static final String AGE_STRING = "Age";

    private static final String RANK_STRING = "Rank";

    private static final String FLIGHT_HR_STRING = "Flight Hour";
    /**
     * The shape of the link to the pilot.
     */
    private final String name;

    private final int age;

    private final Gender gender;

    private final int flightHour;

    private final PilotRank rank;

    private final String id;

    /**
     * Creates a pilot with a random UUID as its id.
     *
     * @param name       the name of the pilot.
     * @param age        the age of the pilot.
     * @param gender     the gender of the pilot.
     * @param rank       the rank of the pilot.
     * @param flightHour the flight hour of the pilot.
     */
    public Pilot(
            String name,
            int age,
            Gender gender,
            PilotRank rank,
            int flightHour
    ) {
        this(UUID.randomUUID().toString(), name, age, gender, rank, flightHour);
    }

    /**
     * Creates a pilot.
     *
     * @param id         the id of the pilot.
     * @param name       the name of the pilot.
     * @param age        the age of the pilot.
     * @param gender     the gender of the pilot.
     * @param rank       the rank of the pilot.
     * @param flightHour the flight hour of the pilot.
     */
    public Pilot(
            String id,
            String name,
            int age,
            Gender gender,
            PilotRank rank,
            int flightHour
    ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.rank = rank;
        this.flightHour = flightHour;
    }

    /**
     * Returns the name of the pilot.
     *
     * @return the name of the pilot.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the rank of the pilot.
     *
     * @return the rank of the pilot.
     */
    public PilotRank getRank() {
        return rank;
    }

    /**
     * Returns the age of the pilot.
     *
     * @return the age of the pilot.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the gender of the pilot.
     *
     * @return the gender of the pilot.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the flight hour of the pilot.
     *
     * @return the flight hour of the pilot.
     */
    public int getFlightHour() {
        return flightHour;
    }

    @Override
    public List<String> getDisplayList() {
        return List.of(
                String.format("%s: %s", UUID_STRING, id),
                String.format("%s: %s", NAME_STRING, name),
                String.format("%s: %s", GENDER_STRING, gender),
                String.format("%s: %s", AGE_STRING, age),
                String.format("%s: %s", RANK_STRING, rank),
                String.format("%s: %s", FLIGHT_HR_STRING, flightHour)
        );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s %s (age: %s)", rank, name, age);
    }
}
