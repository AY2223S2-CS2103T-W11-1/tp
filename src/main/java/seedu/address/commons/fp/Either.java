package seedu.address.commons.fp;

import java.util.function.Function;

/**
 * Represents an instance that can be either left or right.
 *
 * @param <L> the type of the left instance.
 * @param <R> the type of the right instance.
 */
public abstract class Either<L, R> {
    /**
     * Returns an Either instance that contains the left value.
     *
     * @param leftValue the value on the left side.
     * @param <L>       the type of the value on the left.
     * @param <R>       the type of the value on the right.
     * @return the {@link Either} instance holding the value on the left.
     */
    public static <L, R> Either<L, R> left(L leftValue) {
        return new Left<>(leftValue);
    }

    /**
     * Returns an Either instance that contains the right value.
     *
     * @param rightValue the value on the right side.
     * @param <L>        the type of the value on the left.
     * @param <R>        the type of the value on the right.
     * @return the {@link Either} instance holding
     */
    public static <L, R> Either<L, R> right(R rightValue) {
        return new Right<>(rightValue);
    }

    /**
     * Gets the left part of this either.
     *
     * @return the left part of this either instance.
     */
    public abstract L getLeft();

    /**
     * Gets the right part of this either.
     *
     * @return the right part of this either instance.
     */
    public abstract R getRight();

    /**
     * Gets whether if the {@code Either} instance is the left.
     *
     * @return true if the {@code Either} instance is the left.
     */
    public abstract boolean isLeft();

    /**
     * Gets whether if the {@code Either} instance is the right.
     *
     * @return true if the {@code Either} instance is the right.
     */
    public abstract boolean isRight();

    public abstract <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mp);

    public abstract <T> Either<L, T> mapRight(Function<? super R, ? extends T> mp);

    /**
     * The left instance.
     *
     * @param <L> the type of the value on the left.
     * @param <R> the type of the value on the right.
     */
    private static class Left<L, R> extends Either<L, R> {

        private final L value;

        public Left(L value) {
            this.value = value;
        }

        @Override
        public L getLeft() {
            return this.value;
        }

        @Override
        public R getRight() {
            throw new IllegalStateException(
                "Cannot get the right instance inside a left instance."
            );
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mp) {
            return Either.left(mp.apply(this.value));
        }

        @Override
        public <T> Either<L, T> mapRight(Function<? super R, ? extends T> mp) {
            return Either.left(this.value);
        }
    }

    /**
     * The right instance.
     *
     * @param <L> the type of the value on the left.
     * @param <R> the type of the value on the right.
     */
    private static class Right<L, R> extends Either<L, R> {

        private final R value;

        public Right(R value) {
            this.value = value;
        }

        @Override
        public L getLeft() {
            throw new IllegalStateException(
                "Cannot get the left value inside a right instance."
            );
        }

        @Override
        public R getRight() {
            return value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mp) {
            return Either.right(this.value);
        }

        @Override
        public <T> Either<L, T> mapRight(Function<? super R, ? extends T> mp) {
            return Either.right(mp.apply(this.value));
        }
    }
}
