package seedu.address.model.location;

import java.util.UUID;

import seedu.address.model.item.Identifiable;

/**
 * Location is a unit place that the flight can travel to or
 *  arrive at.
 */
public class Location implements Identifiable {
    private final String name;
    private final String id;

    /**
     * Creates a Location object with the given name.
     * @param name: name of the location
     */
    public Location(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    /**
     * Creates a Location object from the given id and name
     * @param id a unique id assigned to the location
     * @param name the name of the location
     */
    public Location(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the name of the location in string.
     * @return the name of the location
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns true if both locations have the same name.
     * This defines a weaker notion of equality between two locations.
     */
    public boolean isSameLocation(Location otherLocation) {
        if (otherLocation == this) {
            return true;
        }

        return otherLocation != null
                && otherLocation.getName().equals(getName());
    }
}
