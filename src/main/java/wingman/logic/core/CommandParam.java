package wingman.logic.core;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import wingman.logic.core.exceptions.CommandException;
import wingman.logic.core.exceptions.ParseException;

/**
 * Represents the parameters of a command.
 */
public class CommandParam {
    /**
     * The unnamed token of the command, which is the part right after
     * command word and before the first prefix.
     */
    private final Optional<String> unnamedValue;

    /**
     * The named token of the command, which is the part right after
     * one prefix and before the next prefix.
     */
    private final Optional<Map<String, Optional<String>>> namedValues;

    /**
     * Creates a command parameter from the given tokens. Note that this
     * function will not throw parser exception, but will instead return a
     * {@code CommandParam} with empty optional fields. It is the caller's
     * responsibility to check if the returned command parameter is valid,
     * because this class does not have enough context to provide a
     * meaningful exception message.
     *
     * @param unnamedValue the unnamed token of the command.
     * @param namedValues  the named token of the command.
     */
    public CommandParam(
            Optional<String> unnamedValue,
            Optional<Map<String, Optional<String>>> namedValues
    ) {
        this.unnamedValue = unnamedValue;
        this.namedValues = namedValues;
    }

    /**
     * Creates a command parameter from the given tokens. Note that this
     * function will not throw parser exception, but will instead return a
     * {@code CommandParam} with empty optional fields. It is the caller's
     * responsibility to check if the returned command parameter is valid,
     * because this class does not have enough context to provide a
     * meaningful exception message.
     *
     * @param tokens   the tokens of the command.
     * @param prefixes the prefixes of the command.
     * @return the command parameter created.
     */
    public static CommandParam from(
            Deque<String> tokens,
            Optional<Set<String>> prefixes
    ) throws ParseException {
        // special cases
        if (tokens.size() == 0) {
            return new CommandParam(
                    Optional.empty(),
                    Optional.empty()
            );
        }
        if (prefixes.isEmpty()) {
            return new CommandParam(
                    Optional.of(String.join(" ", tokens)),
                    Optional.empty()
            );
        }
        // set up
        Set<String> prefixMap = prefixes.get();
        // handle the unnamed token
        Optional<String> unnamedValue =
                parseUnnamedValue(tokens, prefixMap);
        // handle the named tokens
        Map<String, Optional<String>> namedValues =
                parseNamedValues(tokens, prefixMap);
        return new CommandParam(unnamedValue, Optional.of(namedValues));
    }

    /**
     * Parses the unnamed value from the given tokens. The unnamed value is
     * the part right after the command word and before the first prefix.
     * Note that it is assumed that the tokens already have the command word
     * removed. The prefix set is used to determine when the unnamed value
     * ends.
     *
     * @param tokens   the tokens of the command, with the command word
     *                 removed.
     * @param prefixes the prefixes of the command.
     * @return the unnamed value parsed.
     */
    private static Optional<String> parseUnnamedValue(
            Deque<String> tokens,
            Set<String> prefixes
    ) throws ParseException {
        if (tokens.size() == 0) {
            return Optional.empty();
        }
        final StringBuilder builder = new StringBuilder();
        while (tokens.size() > 0) {
            if (prefixes.contains(tokens.peek())) {
                break;
            }
            builder.append(tokens.pop()).append(" ");
        }
        if (builder.length() == 0) {
            return Optional.empty();
        }
        return Optional.of(builder.toString().trim());
    }

    /**
     * Parses the named values from the given tokens. The named values are
     * the parts right after the prefixes and before the next prefix. Note
     * that it is assumed that the tokens already have the command word
     * and the unnamed value removed.
     *
     * @param tokens   the tokens of the command, with the command word and
     *                 the unnamed value removed.
     * @param prefixes the prefixes of the command.
     * @return the named values parsed.
     */
    private static Map<String, Optional<String>> parseNamedValues(
            Deque<String> tokens,
            Set<String> prefixes
    ) throws ParseException {
        if (tokens.size() == 0) {
            return padNamedValues(new HashMap<>(), prefixes);
        }
        Map<String, Optional<String>> namedValues = new HashMap<>();
        String prefix = tokens.pop();
        // just to be defensive, despite the assumption
        while (!prefixes.contains(prefix)) {
            prefix = tokens.pop();
        }
        final StringBuilder builder = new StringBuilder();
        while (tokens.size() > 0) {
            if (!prefixes.contains(tokens.peek())) {
                builder.append(tokens.pop()).append(" ");
                continue;
            }
            // check if it is already in there.
            if (namedValues.containsKey(prefix)
                        && namedValues.get(prefix).isPresent()) {
                throw ParseException.formatted(
                        "Found duplicated keys: %s",
                        prefix
                );
            }
            namedValues.put(prefix, Optional.of(builder.toString().trim()));
            prefix = tokens.pop();
            builder.setLength(0);
        }
        if (builder.length() > 0) {
            if (namedValues.containsKey(prefix)
                        && namedValues.get(prefix).isPresent()) {
                throw ParseException.formatted(
                        "Found duplicated keys: %s",
                        prefix
                );
            }
            namedValues.put(prefix, Optional.of(builder.toString().trim()));
        }
        return padNamedValues(namedValues, prefixes);
    }

    /**
     * Pad the named values with empty values for the prefixes that are not
     * present in the named values.
     *
     * @param namedValues the named values to pad.
     * @param prefixes    the prefixes to pad.
     * @return the named values padded.
     */
    private static Map<String, Optional<String>> padNamedValues(
            Map<String, Optional<String>> namedValues, Set<String> prefixes
    ) {
        for (String p : prefixes) {
            if (!namedValues.containsKey(p)) {
                namedValues.put(p, Optional.empty());
            }
        }
        return namedValues;
    }

    /**
     * Returns the unnamed token of the command, which is the part right after
     * command word and before the first prefix.
     *
     * @return the unnamed token of the command.
     */
    public Optional<String> getUnnamedValue() {
        return unnamedValue;
    }

    /**
     * Gets the unnamed token of the command or throws an exception.
     */
    public String getUnnamedValueOrThrow(String message) throws ParseException {
        if (unnamedValue.isEmpty()) {
            throw new ParseException(message);
        }
        return unnamedValue.get();
    }

    /**
     * Gets the unnamed token of the command or throws an exception.
     *
     * @return the unnamed token of the command.
     * @throws CommandException if the unnamed token is not present.
     */
    public String getUnnamedValueOrThrow() throws ParseException {
        return getUnnamedValueOrThrow("Missing unnamed value.");
    }

    /**
     * Returns the named token of the command, which is the part right after
     * one prefix and before the next prefix.
     *
     * @return the named token of the command.
     */
    public Optional<Map<String, Optional<String>>> getNamedValues() {
        // so that the caller cannot modify the named token
        return namedValues.map(Map::copyOf);
    }

    /**
     * Gets the value of the named token with the given prefix.
     */
    public Optional<String> getNamedValues(String prefix) {
        if (namedValues.isEmpty()) {
            return Optional.empty();
        }
        return namedValues.get().get(prefix);
    }

    /**
     * Gets the value of the named token with the given prefix or throws an exception.
     *
     * @throws ParseException if there is no value following a prefix,
     *                        the input is an empty string.
     */
    public String getNamedValuesOrThrow(
            String prefix,
            String message
    ) throws ParseException {
        final Optional<String> value = getNamedValues(prefix);
        if (value.isEmpty() || value.get().isEmpty()) {
            throw new ParseException(message);
        }
        return value.get();
    }

    /**
     * Gets the value of the named token with the given prefix or throws an exception.
     *
     * @see #getNamedValuesOrThrow(String, String)
     */
    public String getNamedValuesOrThrow(String prefix) throws ParseException {
        return getNamedValuesOrThrow(
                prefix,
                String.format(
                        "Missing value for prefix %s.\n"
                                + "Please try entering a value following %s.",
                        prefix,
                        prefix
                )
        );
    }

    /**
     * Gets the integer value of the unnamed token with the given prefix,
     * else throws a {@code ParseException} with the given message.
     */
    public int getUnnamedIntOrThrow(String message) throws ParseException {
        final String value = getUnnamedValueOrThrow(message);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(
                    "%s is an invalid value.\n"
                            + "Please try using an integer instead.",
                    value
            ));
        }
    }

    /**
     * Gets the integer value of the unnamed token with the given prefix,
     * else throws a {@code ParseException} with the given message.
     */
    public int getUnnamedIntOrThrow() throws ParseException {
        return getUnnamedIntOrThrow(
                "Missing integer.\n"
                        + "Please try entering an integer.");
    }

    /**
     * Gets the integer value of the named token with the given prefix or throws an exception.
     */
    public int getNamedIntOrThrow(
            String prefix,
            String message
    ) throws ParseException {
        final String value = getNamedValuesOrThrow(prefix, message);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(
                    "%s is an invalid value for prefix %s.\n"
                            + "Please try using an integer instead.",
                    value,
                    prefix
            ));
        }
    }

    /**
     * Gets the integer value of the named token with the given prefix or throws an exception.
     *
     * @see #getNamedIntOrThrow(String, String)
     */
    public int getNamedIntOrThrow(String prefix) throws ParseException {
        return getNamedIntOrThrow(prefix, String.format(
                "Missing integer for prefix %s.\n"
                        + "Please try entering an integer following %s.",
                prefix,
                prefix
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommandParam)) {
            return false;
        }
        CommandParam otherCommandParam = (CommandParam) other;
        return unnamedValue.equals(otherCommandParam.unnamedValue)
                       && namedValues.equals(otherCommandParam.namedValues);
    }
}
