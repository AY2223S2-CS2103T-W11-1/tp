package seedu.address.model.location;

/**
 * The types of locations that a crew member
 * can have in a link relation.
 */
public enum CrewLocationType {
    /**
     * The location where the crew stays
     */
    LOCATION_USING;

    @Override
    public String toString() {
        switch (this) {
        case LOCATION_USING:
            return "Location being used";
        default:
            return "Unknown";
        }
    }
}
