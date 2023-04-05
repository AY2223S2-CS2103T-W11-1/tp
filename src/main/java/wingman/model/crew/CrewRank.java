package wingman.model.crew;

import wingman.model.crew.exceptions.InvalidCrewRankException;
import wingman.model.pilot.exceptions.InvalidPilotRankException;

/**
 * Represents the rank of a crew.
 */
public enum CrewRank {
    SENIOR_CREW_MEMBER,
    CREW_MEMBER,
    JUNIOR_CREW_MEMBER,
    TRAINEE;

    public static final String SENIOR_CREW_MEMBER_STRING = "Senior Crew Member";
    public static final String CREW_MEMBER_STRING = "Crew Member";
    public static final String JUNIOR_CREW_MEMBER_STRING = "Junior Crew Member";
    public static final String TRAINEE_STRING = "Trainee";

    /**
     * Returns the string representation of the crew rank.
     *
     * @return the string representation of the crew rank
     */
    public String toString() {
        switch (this) {
        case SENIOR_CREW_MEMBER:
            return SENIOR_CREW_MEMBER_STRING;
        case CREW_MEMBER:
            return CREW_MEMBER_STRING;
        case JUNIOR_CREW_MEMBER:
            return JUNIOR_CREW_MEMBER_STRING;
        case TRAINEE:
            return TRAINEE_STRING;
        default:
            throw new InvalidPilotRankException(this);
        }
    }

    /**
     * Returns the crew rank from the string representation.
     *
     * @param rank the string representation of the crew rank
     * @return the crew rank
     */
    public static CrewRank fromString(String rank) {
        switch (rank) {
        case SENIOR_CREW_MEMBER_STRING:
            return SENIOR_CREW_MEMBER;
        case CREW_MEMBER_STRING:
            return CREW_MEMBER;
        case JUNIOR_CREW_MEMBER_STRING:
            return JUNIOR_CREW_MEMBER;
        case TRAINEE_STRING:
            return TRAINEE;
        default:
            throw new InvalidCrewRankException(rank);
        }
    }

    /**
     * Returns the index of the crew rank in the enum.
     *
     * @return the index of the crew rank in the enum
     */
    public int toIndex() {
        switch (this) {
        case SENIOR_CREW_MEMBER:
            return 0;
        case CREW_MEMBER:
            return 1;
        case JUNIOR_CREW_MEMBER:
            return 2;
        case TRAINEE:
            return 3;
        default:
            throw new InvalidCrewRankException(this);
        }
    }

    /**
     * Returns the crew rank from the index.
     *
     * @param index the index of the crew rank in the enum
     * @return the crew rank
     */
    public static CrewRank fromIndex(int index) {
        switch (index) {
        case 0:
            return SENIOR_CREW_MEMBER;
        case 1:
            return CREW_MEMBER;
        case 2:
            return JUNIOR_CREW_MEMBER;
        case 3:
            return TRAINEE;
        default:
            throw new InvalidCrewRankException(index);
        }
    }
}
