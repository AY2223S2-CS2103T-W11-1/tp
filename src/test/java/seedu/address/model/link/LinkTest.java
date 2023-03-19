package seedu.address.model.link;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import seedu.address.commons.fp.Lazy;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.item.Item;
import seedu.address.model.link.exceptions.LinkDuplicateException;
import seedu.address.model.link.exceptions.LinkException;
import seedu.address.model.link.exceptions.LinkItemNotFoundException;

@ExtendWith(MockitoExtension.class)
public class LinkTest {
    @Mock
    private ReadOnlyItemManager<ItemStub> manager;
    private ItemStub testItem;
    private Link<CategoryStub, ItemStub, ReadOnlyItemManager<ItemStub>> sut;
    private Map<CategoryStub, Integer> shape;
    private Map<CategoryStub, Deque<String>> contents;

    @BeforeEach
    void setUp() throws LinkException {
        testItem = new ItemStub("test-id");
        Mockito
                .lenient()
                .when(manager.getItem(any()))
                .thenReturn(Optional.of(testItem));
        contents = new HashMap<>();
        contents.put(CategoryStub.CATEGORY_A, new ArrayDeque<>());
        contents.put(CategoryStub.CATEGORY_B, new ArrayDeque<>());
        shape = Map.of(CategoryStub.CATEGORY_A, 1, CategoryStub.CATEGORY_B, 2);
        sut = new Link<>(shape, contents, Lazy.of(manager));
    }

    void setUpLinkAsFull() throws LinkException {
        contents.get(CategoryStub.CATEGORY_A).push("test-id-1");
        contents.get(CategoryStub.CATEGORY_B).push("test-id-2");
        contents.get(CategoryStub.CATEGORY_B).push("test-id-3");
        sut = new Link<>(shape, contents, Lazy.of(manager));
    }

    @Test
    void constructor_twoValidParam_shouldCreateNewInstance() throws LinkException {
        final Link<CategoryStub, ItemStub, ReadOnlyItemManager<ItemStub>> result =
                new Link<>(shape, Lazy.of(manager));
        assertNotNull(result);
        for (CategoryStub key : shape.keySet()) {
            assertTrue(result.getUnmodifiableContents().containsKey(key));
        }
    }

    @Test
    void constructor_threeValidParam_shouldCreateNewInstance() throws LinkException {
        final Link<CategoryStub, ItemStub, ReadOnlyItemManager<ItemStub>> result =
                new Link<>(shape, contents, Lazy.of(manager));
        assertNotNull(result);
        for (CategoryStub key : shape.keySet()) {
            assertTrue(result.getUnmodifiableContents().containsKey(key));
        }
    }

    @Test
    void constructor_threeParamUnspecifiedKey_shouldThrowLinkException() {
        contents.put(CategoryStub.UNSUPPORTED, new ArrayDeque<>());
        assertThrows(LinkException.class, () -> new Link<>(shape, contents,
                Lazy.of(manager)
        ));
    }

    @Test
    void constructor_threeParamInvalidSize_shouldThrowLinkException() {
        contents.get(CategoryStub.CATEGORY_A).push("test-id-1");
        contents.get(CategoryStub.CATEGORY_A).push("test-id-2");
        contents.get(CategoryStub.CATEGORY_A).push("test-id-3");
        assertThrows(LinkException.class, () -> new Link<>(shape, contents,
                Lazy.of(manager)
        ));
    }

    @Test
    void constructor_threeValidParam_shouldNotResultInAliasing() throws LinkException {
        assertNotEquals(sut.getUnmodifiableContents(), contents);
        assertNotEquals(sut.getCopiedContents(), contents);
        contents.get(CategoryStub.CATEGORY_A).push("hello");
        final Deque<String> result = sut
                                             .getCopiedContents()
                                             .get(CategoryStub.CATEGORY_A);
        assertFalse(result.contains("hello"));
    }

    @Test
    void getUnmodifiableContents_nothing_returnsMapWithCorrectKeysAndValues() throws LinkException {
        setUpLinkAsFull();

        Map<CategoryStub, Collection<String>> result = sut.getUnmodifiableContents();

        assertTrue(result.containsKey(CategoryStub.CATEGORY_A));
        assertTrue(result.containsKey(CategoryStub.CATEGORY_B));
        assertFalse(result.containsKey(CategoryStub.UNSUPPORTED));

        assertEquals(result.get(CategoryStub.CATEGORY_A).size(), 1);
        assertEquals(result.get(CategoryStub.CATEGORY_B).size(), 2);
        assertTrue(result.get(CategoryStub.CATEGORY_A).contains("test-id-1"));
        assertTrue(result.get(CategoryStub.CATEGORY_B).contains("test-id-2"));
        assertTrue(result.get(CategoryStub.CATEGORY_B).contains("test-id-3"));
    }

    @Test
    void getUnmodifiableContents_tryModify_throwsException() throws LinkException {
        setUpLinkAsFull();

        Map<CategoryStub, Collection<String>> result = sut.getUnmodifiableContents();

        assertThrows(
                UnsupportedOperationException.class, () -> {
                    result.put(CategoryStub.UNSUPPORTED, new ArrayDeque<>());
                }
        );
        assertThrows(
                UnsupportedOperationException.class, () -> {
                    result.get(CategoryStub.CATEGORY_A).add("test-id-4");
                }
        );
    }

    @Test
    void getRemainingSizeOfKey_invalidKey_throwsLinkException() {
        assertThrows(
                LinkException.class, () -> {
                    sut.getRemainingSizeOfKey(CategoryStub.UNSUPPORTED);
                }
        );
    }

    @Test
    void getRemainingSizeOfKey_empty_returnCorrectSize() throws LinkException {
        int aSize = sut.getRemainingSizeOfKey(CategoryStub.CATEGORY_A);
        assertEquals(aSize, shape.get(CategoryStub.CATEGORY_A));

        int bSize = sut.getRemainingSizeOfKey(CategoryStub.CATEGORY_B);
        assertEquals(bSize, shape.get(CategoryStub.CATEGORY_B));
    }

    @Test
    void getRemainingSizeOfKey_full_returnSizeZero() throws LinkException {
        setUpLinkAsFull();
        int aSize = sut.getRemainingSizeOfKey(CategoryStub.CATEGORY_A);
        assertEquals(aSize, 0);

        int bSize = sut.getRemainingSizeOfKey(CategoryStub.CATEGORY_B);
        assertEquals(bSize, 0);
    }

    @Test
    void put_invalidKey_throwsLinkException() {
        assertThrows(
                LinkException.class, () -> {
                    sut.put(CategoryStub.UNSUPPORTED, "hello");
                }
        );
    }

    @Test
    void put_alreadyFull_throwsLinkException() throws LinkException {
        setUpLinkAsFull();
        assertThrows(
                LinkException.class, () -> {
                    sut.put(CategoryStub.CATEGORY_A, "hello");
                }
        );
        assertThrows(
                LinkException.class, () -> {
                    sut.put(CategoryStub.CATEGORY_B, "hello");
                }
        );
    }

    @Test
    void put_hasDuplicate_throwsLinkDuplicateException() throws LinkException {
        sut.put(CategoryStub.CATEGORY_B, "hello");
        assertThrows(
                LinkDuplicateException.class, () -> {
                    sut.put(CategoryStub.CATEGORY_B, "hello");
                }
        );
    }

    @Test
    void put_validInput_putsItemIntoLink() throws LinkException {
        assertEquals(sut.get(CategoryStub.CATEGORY_A).size(), 0);
        assertEquals(sut.get(CategoryStub.CATEGORY_B).size(), 0);

        sut.put(CategoryStub.CATEGORY_A, "hello");
        sut.put(CategoryStub.CATEGORY_B, "hello");

        assertEquals(sut.get(CategoryStub.CATEGORY_A).size(), 1);
        assertEquals(sut.get(CategoryStub.CATEGORY_B).size(), 1);

        sut.put(CategoryStub.CATEGORY_B, "hello2");
        assertEquals(sut.get(CategoryStub.CATEGORY_B).size(), 2);
    }

    @Test
    void putRevolve_full_shouldRevolveInAFifoManner() throws LinkException {
        setUpLinkAsFull();
        Map<CategoryStub, Deque<String>> before = sut.getCopiedContents();

        assertDoesNotThrow(() -> sut.putRevolve(
                CategoryStub.CATEGORY_A,
                "new-id"
        ));
        assertDoesNotThrow(() -> sut.putRevolve(
                CategoryStub.CATEGORY_B,
                "new-id"
        ));

        Map<CategoryStub, Deque<String>> after = sut.getCopiedContents();

        assertNotEquals(
                after.get(CategoryStub.CATEGORY_A).getFirst(),
                before.get(CategoryStub.CATEGORY_A).getFirst()
        );
        assertEquals(
                before.get(CategoryStub.CATEGORY_B).getLast(),
                after.get(CategoryStub.CATEGORY_B).getFirst()
        );
        assertNotEquals(
                before.get(CategoryStub.CATEGORY_B).getFirst(),
                after.get(CategoryStub.CATEGORY_B).getFirst()
        );
        assertNotEquals(
                before.get(CategoryStub.CATEGORY_B).getLast(),
                after.get(CategoryStub.CATEGORY_B).getLast()
        );
    }

    @Test
    void clear_invalidKeySpecified_throwsLinkException() throws LinkException {
        setUpLinkAsFull();
        assertThrows(
                LinkException.class, () -> {
                    sut.clear(CategoryStub.UNSUPPORTED);
                }
        );
    }

    @Test
    void clear_validKeySpecified_clearsTheKey() throws LinkException {
        setUpLinkAsFull();
        assertFalse(
                sut.getCopiedContents().get(CategoryStub.CATEGORY_A).isEmpty()
        );
        assertDoesNotThrow(() -> sut.clear(CategoryStub.CATEGORY_A));
        assertTrue(
                sut.getCopiedContents().get(CategoryStub.CATEGORY_A).isEmpty()
        );


        assertFalse(
                sut.getCopiedContents().get(CategoryStub.CATEGORY_B).isEmpty()
        );
        assertDoesNotThrow(() -> sut.clear(CategoryStub.CATEGORY_B));
        assertTrue(
                sut.getCopiedContents().get(CategoryStub.CATEGORY_B).isEmpty()
        );
    }

    @Test
    void clear_noKeySpecified_clearsEverything() throws LinkException {
        setUpLinkAsFull();

        Map<CategoryStub, Deque<String>> before = sut.getCopiedContents();
        assertFalse(before.get(CategoryStub.CATEGORY_A).isEmpty());
        assertFalse(before.get(CategoryStub.CATEGORY_B).isEmpty());

        sut.clear();

        Map<CategoryStub, Deque<String>> after = sut.getCopiedContents();
        assertTrue(after.get(CategoryStub.CATEGORY_A).isEmpty());
        assertTrue(after.get(CategoryStub.CATEGORY_B).isEmpty());
    }

    @Test
    void delete_invalidKey_throwsLinkException() throws LinkException {
        setUpLinkAsFull();
        assertThrows(
                LinkException.class, () -> {
                    sut.delete(CategoryStub.UNSUPPORTED, "test-id-1");
                }
        );
    }

    @Test
    void delete_itemNotContained_throwsLinkItemNotFoundException() throws LinkException {
        setUpLinkAsFull();
        assertThrows(
                LinkItemNotFoundException.class, () -> {
                    sut.delete(CategoryStub.CATEGORY_A, "hello-world");
                }
        );
    }

    @Test
    void delete_validItem_removesTheItem() throws LinkException {
        setUpLinkAsFull();
        assertDoesNotThrow(() -> sut.delete(
                CategoryStub.CATEGORY_A,
                "test-id-1"
        ));
        assertTrue(sut
                           .getCopiedContents()
                           .get(CategoryStub.CATEGORY_A)
                           .isEmpty());
    }

    private enum CategoryStub {
        CATEGORY_A,
        CATEGORY_B,
        UNSUPPORTED,
    }

    private static class ItemStub implements Item {
        private final String id;

        public ItemStub(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
