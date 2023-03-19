package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Utility methods related to Collections
 */
public class CollectionUtil {

    /**
     * @see #requireAllNonNull(Collection)
     */
    public static void requireAllNonNull(Object... items) {
        requireNonNull(items);
        Stream.of(items).forEach(Objects::requireNonNull);
    }

    /**
     * Throws NullPointerException if {@code items} or any element of {@code items} is null.
     */
    public static void requireAllNonNull(Collection<?> items) {
        requireNonNull(items);
        items.forEach(Objects::requireNonNull);
    }

    /**
     * Returns true if {@code items} contain any elements that are non-null.
     */
    public static boolean isAnyNonNull(Object... items) {
        return items != null && Arrays.stream(items).anyMatch(Objects::nonNull);
    }

    /**
     * Creates a deep copy of the source, which is a {@link Map} whose value
     * is a {@link Deque}.
     *
     * @param source the source from which the deep copy is made.
     * @param <K>    the type of the key.
     * @param <V>    the type of the value contained by the {@link Deque}.
     * @return the deep copy of the source.
     */
    public static <K, V> Map<K, Deque<V>> deepCopyMapDq(
        Map<? extends K, ? extends Deque<? extends V>> source) {
        HashMap<K, Deque<V>> map = new HashMap<>();
        for (K key : source.keySet()) {
            map.put(key, new ArrayDeque<>(source.get(key)));
        }
        return map;
    }

    /**
     * @param source the source from which the deep copy is made
     * @param <K>    the type of the key.
     * @param <V>    the type of the value.
     * @return the deep copy of the source.
     */
    public static <K, V> Map<K, V> deepCopy(Map<K, V> source) {
        HashMap<K, V> map = new HashMap<>();
        for (K key : source.keySet()) {
            map.put(key, source.get(key));
        }
        return map;
    }

    /**
     * Returns an unmodifiable view of the {@link Map} whose value is a
     * {@link Deque}.
     *
     * @param source the source file.
     * @param <K>    the type of the key.
     * @param <V>    the type of value contained by the {@link Deque}
     * @return the deep copy of the source.
     */
    public static <K, V> Map<K, Collection<V>> unmodifiableMapDq(
        Map<? extends K, ? extends Collection<? extends V>> source) {
        HashMap<K, Collection<V>> result = new HashMap<>();
        for (K key : source.keySet()) {
            result.put(key, Collections.unmodifiableCollection(source.get(key)));
        }
        return Collections.unmodifiableMap(result);
    }
}
