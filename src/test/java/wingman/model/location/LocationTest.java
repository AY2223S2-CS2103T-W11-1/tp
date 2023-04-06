package wingman.model.location;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class LocationTest {
    private final String name1 = " ";
    private final String name2 = "name";

    private final Location location1 = new Location(name1);
    private final Location location2 = new Location(name2);
    private final Location location3 = new Location(name1);

    @Test
    public void testLocation() {
        assertDoesNotThrow(() -> new Location("Location name"));
        assertDoesNotThrow(() -> new Location(""));
    }

    @Test
    public void testEquals() {
        // negative cases
        assertNotEquals(location1, location2);
        assertNotEquals(location2, location1);
        assertNotEquals(location2, location3);

        // positive cases
        assertEquals(location1, location1);
        assertEquals(location2, location2);
        assertEquals(location1, location3);
    }

    @Test
    public void testToString() {
        assertEquals(name1, location1.toString());
        assertEquals(name2, location2.toString());
        assertEquals(name1, location3.toString());
    }
}
