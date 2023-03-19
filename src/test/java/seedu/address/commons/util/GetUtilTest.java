package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the service locator class {@link GetUtil}.
 */
public class GetUtilTest {
    @BeforeEach
    void setUp() {
        GetUtil.clear();
    }

    @Test
    void get_nullSupplied_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> GetUtil.get(null));
    }

    @Test
    void get_notRegistered_shouldThrowGetException() {
        assertThrows(GetUtil.GetException.class, () -> GetUtil.get(Stub.class));
    }

    @Test
    void put_nullSupplied_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> GetUtil.put(null, new Stub()));
        assertThrows(NullPointerException.class, () -> GetUtil.put(Stub.class, null));
    }

    @Test
    void put_alreadyPut_shouldThrowGetException() {
        GetUtil.put(Stub.class, new Stub());
        assertThrows(GetUtil.GetException.class, () -> GetUtil.put(Stub.class, new Stub()));
        GetUtil.clear();
        GetUtil.putLazy(Stub.class, Stub::new);
        assertThrows(GetUtil.GetException.class, () -> GetUtil.put(Stub.class, new Stub()));
    }

    @Test
    void putForce_alreadyPut_shouldReplaceOriginal() {
        Stub stub1 = new Stub();
        Stub stub2 = new Stub();
        assertNotEquals(stub1, stub2);
        GetUtil.put(Stub.class, stub1);
        assertEquals(stub1, GetUtil.get(Stub.class));
        assertNotEquals(stub2, GetUtil.get(Stub.class));

        assertDoesNotThrow(() -> GetUtil.putForce(Stub.class, stub2));

        assertNotEquals(stub1, GetUtil.get(Stub.class));
        assertEquals(stub2, GetUtil.get(Stub.class));
    }

    @Test
    void putLazy_nullSupplied_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> GetUtil.putLazy(null, Stub::new));
        assertThrows(NullPointerException.class, () -> GetUtil.putLazy(Stub.class, null));
    }

    @Test
    void putLazy_thenGet_shouldReturnNewStub() {
        GetUtil.putLazy(Stub.class, Stub::new);
        assertDoesNotThrow(() -> GetUtil.get(Stub.class));
        assertNotNull(GetUtil.get(Stub.class));
    }

    @Test
    void putLazy_alreadyPut_shouldThrowGetException() {
        GetUtil.putLazy(Stub.class, Stub::new);
        assertThrows(GetUtil.GetException.class, () -> GetUtil.putLazy(Stub.class, Stub::new));
        GetUtil.clear();
        GetUtil.put(Stub.class, new Stub());
        assertThrows(GetUtil.GetException.class, () -> GetUtil.putLazy(Stub.class, Stub::new));
    }

    @Test
    void putLazyForce_alreadyPut_shouldReplacePrevious() {
        Stub stub = new Stub();
        GetUtil.put(Stub.class, stub);

        assertDoesNotThrow(() -> GetUtil.putLazyForce(Stub.class, Stub::new));
        assertNotEquals(stub, GetUtil.get(Stub.class));
    }

    private static class Stub {
        public Stub() {
        }
    }
}
