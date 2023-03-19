package seedu.address.commons.fp;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A lazily evaluated value.
 *
 * @param <T> the type of the value that's to be lazily evaluated.
 */
public class Lazy<T> {
    private Either<Supplier<T>, T> supplierOrValue;

    private Lazy(Either<Supplier<T>, T> supplierOrValue) {
        this.supplierOrValue = supplierOrValue;
    }

    /**
     * Creates a new lazy instance.
     *
     * @param supplier the supplier for the value.
     * @param <T>      the type of the supplier value.
     * @return the new lazy instance.
     */
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(Either.left(supplier));
    }

    /**
     * Creates a new lazy instance that's evaluated.
     *
     * @param value the value which this either instance contains.
     * @param <T>   the type of the value.
     * @return the lazy instance created.
     */
    public static <T> Lazy<T> of(T value) {
        return new Lazy<>(Either.right(value));
    }

    /**
     * Gets the value stored inside this lazy instance.
     *
     * @return the value stored inside this lazy instance.
     */
    public T get() {
        if (supplierOrValue.isLeft()) {
            supplierOrValue = Either.right(supplierOrValue.getLeft().get());
        }
        return supplierOrValue.getRight();
    }

    /**
     * Maps the lazy. If the lazy value is already evaluated, then the new
     * lazy function would also be evaluated.
     *
     * @param func the map function
     * @param <R>  the type of the new value contained by the lazy.
     * @return the result.
     */
    public <R> Lazy<R> map(Function<? super T, ? extends R> func) {
        return new Lazy<>(
            this.supplierOrValue
                .<Supplier<R>>mapLeft((f) -> () -> func.apply(f.get()))
                .mapRight(func)
        );
    }
}
