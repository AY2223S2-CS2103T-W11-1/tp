package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.fp.Lazy;

/**
 * The service locator for locating already instantiated services. This can
 * be used to help with dependency injection and create effective isolation
 * between different classes.
 */
public class GetUtil {
    private static final String SERVICE_NOT_REGISTERED_MESSAGE =
        "Service %s not registered.";
    private static final String SERVICE_ALREADY_REGISTERED_MESSAGE =
        "Service %s has already been registered.";
    private static final String CAST_FAILED_MESSAGE = "Casting failed: %s";
    private static final String DELETE_IF_CONTAINS_MESSAGE =
        "Deletes the service %s for %s.";
    private static final String INSTANTIATE_LAZY_VARIABLE_MESSAGE =
        "Instantiating lazy service %s for %s.";
    private static final String CLEAR_MESSAGE =
        "Clearing the contents of GetUtil.";

    private static final Map<Class<?>, Lazy<?>> services = new HashMap<>();

    private static final Logger logger = LogsCenter.getLogger(GetUtil.class);

    /**
     * Clears the services stored in this service locator.
     */
    public static void clear() {
        logger.info(CLEAR_MESSAGE);
        services.clear();
    }

    /**
     * Gets the instance of the corresponding class
     *
     * @param clazz the class of the service that's registered.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz) {
        requireNonNull(clazz);
        if (services.containsKey(clazz)) {
            try {
                return (T) services.get(clazz).get();
            } catch (ClassCastException e) {
                throw new GetException(String.format(CAST_FAILED_MESSAGE,
                    e.getMessage()));
            }
        }
        throw new GetException(String.format(SERVICE_NOT_REGISTERED_MESSAGE,
            clazz.getName()));
    }

    /**
     * Gets the value corresponding to the class clazz, in a lazy manner.
     *
     * @param clazz the class that contains the key
     * @param <T>   the type of the value contained by the lazy instance.
     * @return the lazy instance
     */
    public static <T> Lazy<T> getLazy(Class<T> clazz) {
        return Lazy.of(() -> get(clazz));
    }

    /**
     * Throws the {@link GetException} if contains the key already.
     *
     * @param key the key.
     * @param <T> the type of the value.
     */
    private static <T> void throwIfContains(Class<T> key) {
        if (!services.containsKey(key)) {
            return;
        }
        throw new GetException(
            String.format(SERVICE_ALREADY_REGISTERED_MESSAGE,
                services.get(key).toString())
        );
    }

    /**
     * Deletes the existing value if it is already contained.
     *
     * @param key the key.
     * @param <T> the type of the value.
     */
    public static <T> void delete(Class<T> key) {
        if (!services.containsKey(key)) {
            return;
        }
        logger.info(String.format(DELETE_IF_CONTAINS_MESSAGE,
            services.get(key).toString(), key.getName()));
        services.remove(key);
    }

    /**
     * Puts the value of the key to the service locator. This method will
     * throw if the service is already registered. In most cases, allowing
     * throwing would be the desired behavior. However, if you do not want
     * this behavior, use {@link GetUtil#putForce(Class, Object)} instead.
     *
     * @param key   the class of the value
     * @param value the value
     * @param <T>   the type of the value
     */
    public static <T> void put(Class<T> key, T value) {
        requireNonNull(key);
        requireNonNull(value);
        throwIfContains(key);
        services.put(key, Lazy.of(value));
    }

    /**
     * Puts the value into the services, overriding all previous entries.
     *
     * @param key   the class of the service.
     * @param value the value of the service.
     * @param <T>   the type of the service.
     */
    public static <T> void putForce(Class<T> key, T value) {
        delete(key);
        services.put(key, Lazy.of(value));
    }

    /**
     * Puts a lazy instantiation of the service. The service would only be
     * instantiated when it is used.
     *
     * @param key      the key.
     * @param supplier the supplier for the value.
     * @param <T>      the type of the value.
     */
    public static <T> void putLazy(Class<T> key, Supplier<T> supplier) {
        requireNonNull(key);
        requireNonNull(supplier);
        throwIfContains(key);
        services.put(key, Lazy.of(supplier));
    }

    /**
     * Puts a lazy instantiation of the service and replace the existing ones
     * with the one provided by the supplier.
     *
     * @param key      the key.
     * @param supplier the supplier for the value.
     * @param <T>      the type of the value.
     */
    public static <T> void putLazyForce(Class<T> key, Supplier<T> supplier) {
        requireNonNull(key);
        requireNonNull(supplier);
        delete(key);
        services.put(key, Lazy.of(supplier));
    }

    /**
     * The exception thrown when trying to get something
     */
    public static class GetException extends RuntimeException {
        /**
         * Creates a get exception with the given message
         */
        public GetException(String message) {
            super(message);
        }
    }
}
