package seedu.address.model.pilot;

/**
 * The two types of the pilot for a flight.
 */
public enum FlightPilotType {
    /**
     * The pilot flying.
     */
    PILOT_FLYING,
    /**
     * The pilot monitoring.
     */
    PILOT_MONITORING;

    @Override
    public String toString() {
        switch (this) {
        case PILOT_FLYING:
            return "Pilot Flying";
        case PILOT_MONITORING:
            return "Pilot Monitoring";
        default:
            return "Unknown";
        }
    }
}
