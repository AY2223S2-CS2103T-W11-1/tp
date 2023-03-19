package seedu.address.model.link;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.deepCopy;
import static seedu.address.commons.util.CollectionUtil.deepCopyMapDq;
import static seedu.address.commons.util.CollectionUtil.unmodifiableMapDq;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.fp.Lazy;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.item.Item;
import seedu.address.model.link.exceptions.LinkDuplicateException;
import seedu.address.model.link.exceptions.LinkException;
import seedu.address.model.link.exceptions.LinkItemNotFoundException;

/**
 * The link to a target.
 *
 * @param <T> target.
 * @param <M> the manager for the item.
 */
public class Link<K, T extends Item,
                         M extends ReadOnlyItemManager<T>> {
    private static final String KEY_NOT_FOUND_MESSAGE = "Key %s not found.";

    private static final String ILLEGAL_SIZE_MESSAGE =
            "Illegal Size: key %s can only have %d keys, but got %d keys";
    private static final String CANNOT_PUT_MESSAGE =
            "Cannot put %d values into key %s which allows a maximum of %d values"
                    + " but already contains %d keys.";

    private static final String DUPLICATE_FOUND_MESSAGE =
            "Has found a duplicate in %s for %s.";

    private static final String DELETE_BROKEN_LINK_MESSAGE =
            "Deleted broken link %s from link %s";

    private static final Logger _logger = LogsCenter.getLogger(Link.class);

    /**
     * The shape of this link, which describes: a) what keys this link has;
     * b) how many values per key can this link hold at max.
     */
    private final Map<K, Integer> shape;

    /**
     * The contents that this link contains.
     */
    private final Map<K, Deque<String>> contents;

    /**
     * The resolver that's used to resolve the links.
     */
    private final Lazy<M> managerLazy;

    /**
     * Creates a link with the given shape.
     *
     * @param shape       the shape of the link.
     * @param contents    the contents that this link contains.
     * @param managerLazy the {@link ReadOnlyItemManager} that resolves to an
     *                    item from the id.
     */
    public Link(
            Map<K, Integer> shape,
            Map<K, Deque<String>> contents,
            Lazy<M> managerLazy
    ) throws LinkException {
        fitShapeOrThrow(shape, contents);
        this.shape = deepCopy(shape);
        this.contents = deepCopyMapDq(contents);
        this.managerLazy = managerLazy;
        fill(this.shape, this.contents);
    }

    /**
     * Creates a new link with all the fields set to empty.
     *
     * @param shape the shape of the field.
     */
    public Link(Map<K, Integer> shape, Lazy<M> managerLazy) {
        this.shape = deepCopy(shape);
        this.contents = new HashMap<>();
        this.managerLazy = managerLazy;
        fill(this.shape, this.contents);
    }

    /**
     * Checks if the input contents fits the shape. Otherwise, throw.
     *
     * @param shape    the shape of the link.
     * @param contents the contents of teh link.
     * @throws LinkException if the contents does not fit the shape.
     */
    private static <K> void fitShapeOrThrow(
            Map<K, Integer> shape,
            Map<K, Deque<String>> contents
    ) throws LinkException {
        for (K key : contents.keySet()) {
            if (!shape.containsKey(key)) {
                throw new LinkException(String.format(
                        KEY_NOT_FOUND_MESSAGE,
                        key
                ));
            }
            if (contents.get(key).size() > shape.get(key)) {
                throw new LinkException(String.format(ILLEGAL_SIZE_MESSAGE,
                        key, shape.get(key), contents.get(key).size()
                ));
            }
        }
    }

    /**
     * Fills the contents with keys in the shape. If a key is in shape, but it
     * is not in contents, then we put key into contents with a new empty array.
     *
     * @param shape    the shape of the link.
     * @param contents the contents in the link
     */
    private static <K> void fill(
            Map<K, Integer> shape,
            Map<K, Deque<String>> contents
    ) {
        for (K key : shape.keySet()) {
            if (contents.containsKey(key)) {
                continue;
            }
            contents.put(key, new ArrayDeque<>());
        }
    }

    /**
     * Gets the contents of this link as an unmodifiable map.
     *
     * @return the unmodifiable view of this link content.
     */
    public Map<K, Collection<String>> getUnmodifiableContents() {
        return unmodifiableMapDq(contents);
    }

    /**
     * Gets a copy of the contents of this link.
     *
     * @return the copy of this link content.
     */
    public Map<K, Deque<String>> getCopiedContents() {
        return deepCopyMapDq(contents);
    }

    /**
     * Gets the keys of this link as an unmodifiable set.
     *
     * @return the unmodifiable set of this link content.
     */
    public Set<K> getUnmodifiableKeys() {
        return Collections.unmodifiableSet(this.shape.keySet());
    }

    /**
     * Gets the shape of this link as an unmodifiable shape.
     *
     * @return the unmodifiable shape of this link.
     */
    public Map<K, Integer> getUnmodifiableShape() {
        return Collections.unmodifiableMap(this.shape);
    }

    /**
     * Gets the remaining size, i.e. how many more values, can be hold by the
     * key.
     *
     * @param key the key
     * @return the number of items that the key can still hold.
     */
    public int getRemainingSizeOfKey(K key) throws LinkException {
        keyValidOrThrow(key);
        return shape.get(key) - contents.get(key).size();
    }

    /**
     * Checks if the key is valid, if not, throws the corresponding exception.
     *
     * @param key the key.
     * @throws LinkException if the key is not contained inside the key set.
     */
    private void keyValidOrThrow(K key) throws LinkException {
        requireNonNull(key);
        if (!shape.containsKey(key)) {
            throw new LinkException(String.format(KEY_NOT_FOUND_MESSAGE, key));
        }
    }

    /**
     * Checks if we can put size many values into the key. If cannot, then
     * throw.
     *
     * @param key  the key
     * @param size the number of values that we want to put into the key.
     * @throws LinkException if we cannot put size many values into the key.
     */
    private void canPutOrThrow(K key, int size) throws LinkException {
        if (getRemainingSizeOfKey(key) < size) {
            throw new LinkException(String.format(CANNOT_PUT_MESSAGE, size,
                    key, shape.get(key), contents.get(key).size()
            ));
        }
    }

    /**
     * Checks if we can put 1 value into the key. If not, then throw.
     *
     * @param key the key
     * @throws LinkException if we cannot put 1 value into the key.
     */
    private void canPutOrThrow(K key) throws LinkException {
        canPutOrThrow(key, 1);
    }

    /**
     * Checks if the id is already duplicated. If so, throw.
     *
     * @param key the key
     * @param id  the id
     * @throws LinkDuplicateException if the id is already duplicated under
     *                                the key.
     */
    private void noDuplicateOrThrow(
            K key,
            String id
    ) throws LinkDuplicateException {
        for (String cid : contents.get(key)) {
            if (id.equals(cid)) {
                throw new LinkDuplicateException(
                        String.format(DUPLICATE_FOUND_MESSAGE, key, cid)
                );
            }
        }
    }

    /**
     * Puts the item into the list.
     *
     * @param key  the key.
     * @param item the item that's associated with the key.
     */
    public void put(K key, T item) throws LinkException {
        put(key, item.getId());
    }

    /**
     * Puts the id related to the key into the link.
     *
     * @param key the key
     * @param id  the id
     * @throws LinkException if the id related to the key cannot be put.
     */
    public void put(K key, String id) throws LinkException {
        keyValidOrThrow(key);
        canPutOrThrow(key);
        noDuplicateOrThrow(key, id);
        this.contents.get(key).push(id);
    }

    /**
     * Puts the id related to the key into the link, in a revolving manner if
     * the key's contents is already full. For example, if the key can
     * contain 3 items, and if currently the id is ["a", "b", "c"], and if we
     * put "d" into this, then we would have ["b", "c", "d"]. If, however,
     * there are less than 3 items, then this should behave as normal.
     * <p>
     * This is very helpful if you only have one for the size of the given
     * key. For example, for a flight, we only have 1 plane. With this, we can
     * simply use putRevolve, which is similar to replace.
     *
     * @param key  the key.
     * @param item the value to be put.
     * @throws LinkException if the key cannot be put.
     */
    public void putRevolve(K key, T item) throws LinkException {
        putRevolve(key, item.getId());
    }

    /**
     * See {@link Link#putRevolve(K, T)}.
     *
     * @param key the key
     * @param id  the id to be put
     * @throws LinkException if the key cannot be put
     */
    public void putRevolve(K key, String id) throws LinkException {
        keyValidOrThrow(key);
        canPutOrThrow(key, 0);
        noDuplicateOrThrow(key, id);
        int remainingSize = getRemainingSizeOfKey(key);
        Deque<String> ids = this.contents.get(key);
        if (remainingSize == 0) {
            ids.pop();
            ids.add(id);
        } else {
            ids.add(id);
        }
    }

    /**
     * Clears all the contents for the given key.
     *
     * @param key the key.
     */
    public void clear(K key) throws LinkException {
        keyValidOrThrow(key);
        contents.get(key).clear();
    }

    /**
     * Clears all the contents.
     */
    public void clear() {
        for (K key : contents.keySet()) {
            contents.get(key).clear();
        }
    }

    /**
     * Deletes the given id of key from this link. If the item is not found
     * during the deletion, then an exception would be thrown.
     *
     * @param key  the key.
     * @param item the item to be deleted.
     */
    public void delete(K key, T item) throws LinkException {
        delete(key, item.getId());
    }

    /**
     * Deletes the given item with id of key from this link. If this item is
     * not found during the deletion, then an exception would be thrown.
     *
     * @param key the key.
     * @param id  the id of the item to be deleted.
     * @throws LinkException if the item of key and id is not found.
     */
    public void delete(K key, String id) throws LinkException {
        keyValidOrThrow(key);
        if (!contents.get(key).contains(id)) {
            throw new LinkItemNotFoundException(key.toString(), id);
        }
        contents.get(key).remove(id);
    }

    /**
     * Gets the values corresponding to the key.
     *
     * @param key the key.
     * @return the optionals of values corresponding to the key.
     * @throws LinkException if the key is not valid.
     */
    public List<Optional<T>> get(K key) throws LinkException {
        keyValidOrThrow(key);
        return contents.get(key)
                       .stream()
                       .map((id) -> managerLazy.get().getItem(id))
                       .collect(Collectors.toList());
    }

    /**
     * Gets all the links while at the same time remove the invalid ids from
     * this link class.
     *
     * @param key the key.
     * @return the list of valid items, and at the same time remove those
     *         which are not valid from this list
     * @throws LinkException if the key is not valid.
     */
    public List<T> getAndRemoveInvalid(K key) throws LinkException {
        keyValidOrThrow(key);
        final List<T> result = new ArrayList<>();
        final List<String> tbd = new ArrayList<>();
        for (String id : contents.get(key)) {
            final Optional<T> tmp = managerLazy.get().getItem(id);
            if (tmp.isPresent()) {
                result.add(tmp.get());
            } else {
                tbd.add(id);
            }
        }
        for (String id : tbd) {
            contents.get(key).remove(id);
            _logger.warning(String.format(
                            DELETE_BROKEN_LINK_MESSAGE,
                            id,
                            this
                    )
            );
        }
        return result;
    }

    public List<T> getValid(K key) throws LinkException {
        keyValidOrThrow(key);
        final List<T> result = new ArrayList<>();
        for (String id : contents.get(key)) {
            final Optional<T> tmp = managerLazy.get().getItem(id);
            tmp.ifPresent(result::add);
        }
        return result;
    }

    /**
     * Gets if the current key set contains key.
     *
     * @param key the key.
     * @return true if the current key set contains key.
     */
    public boolean containsKey(K key) {
        return shape.containsKey(key);
    }

    /**
     * Gets if the item is contained inside this link.
     *
     * @param key  the key.
     * @param item the item.
     * @return true if the item is contained inside this link corresponding
     *         to the given key.
     */
    public boolean contains(K key, T item) {
        return contains(key, item.getId());
    }

    /**
     * Gets if the id is contained inside this link.
     *
     * @param key the key.
     * @param id  the item.
     * @return true if the item is contained inside this link corresponding
     *         to the given key.
     */
    public boolean contains(K key, String id) {
        return contents.get(key).contains(id);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (K key : getUnmodifiableKeys()) {
            if (contents.get(key).isEmpty()) {
                continue;
            }
            builder.append(key).append(": ");
            try {
                final List<T> items = getValid(key);
                builder.append(items
                                       .stream()
                                       .map(Object::toString)
                                       .collect(Collectors.joining(", ")));
            } catch (LinkException e) {
                builder.append("Failed to load: ").append(e.getMessage());
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
