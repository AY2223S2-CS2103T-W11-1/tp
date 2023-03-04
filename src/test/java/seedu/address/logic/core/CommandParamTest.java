package seedu.address.logic.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.core.exceptions.ParseException;


/**
 * TODO: write more tests for CommandParam.
 */
public class CommandParamTest {
    private static final String PREFIX_A = "/a";
    private static final String PREFIX_B = "/b";
    private static final String PREFIX_C = "/c";
    private static final String PREFIX_D = "/d";

    private final Set<String> prefixes = Set.of(PREFIX_A, PREFIX_B, PREFIX_C, PREFIX_D);

    private Deque<String> tokens;

    @BeforeEach
    void setUp() {
        tokens = new ArrayDeque<>();
    }

    @Test
    void from_emptyTokenEmptyPrefixes_returnsEmpty() {
        // execute
        CommandParam param = CommandParam.from(tokens, Optional.empty());

        // verify
        assertFalse(param.getUnnamedValue().isPresent());
        assertFalse(param.getNamedValues().isPresent());
    }

    @Test
    void from_emptyTokenNonEmptyPrefixes_returnsEmpty() {
        // execute
        CommandParam param = CommandParam.from(tokens, Optional.of(prefixes));

        // verify
        assertFalse(param.getUnnamedValue().isPresent());
        assertFalse(param.getNamedValues().isPresent());
    }

    @Test
    void from_nonEmptyTokenEmptyPrefixes_returnsUnnamedValue() {
        // setup
        tokens.addAll(List.of("token", "token2"));

        // execute
        CommandParam param = CommandParam.from(tokens, Optional.empty());

        // verify
        assertTrue(param.getUnnamedValue().isPresent());
        assertEquals("token token2", param.getUnnamedValue().get());
        assertFalse(param.getNamedValues().isPresent());
    }

    @Test
    void from_nonEmptyTokenNonEmptyPrefixes_returnsNamedValues() {
        // setup
        tokens.addAll(List.of("token1", "token2", PREFIX_A, "value1", "value2",
            PREFIX_B, "value2", PREFIX_C, "value3"));
        // execute
        CommandParam param = CommandParam.from(tokens, Optional.of(prefixes));
        // verify
        assertTrue(param.getUnnamedValue().isPresent());
        assertEquals("token1 token2", param.getUnnamedValue().get());
        assertTrue(param.getNamedValues().isPresent());
        Map<String, Optional<String>> namedValues = param.getNamedValues().get();
        assertEquals(4, namedValues.size());
        for (String prefix : prefixes) {
            assertTrue(namedValues.containsKey(prefix));
        }
        assertEquals(Optional.of("value1 value2"), namedValues.get(PREFIX_A));
        assertEquals(Optional.of("value2"), namedValues.get(PREFIX_B));
        assertEquals(Optional.of("value3"), namedValues.get(PREFIX_C));
        assertEquals(Optional.empty(), namedValues.get(PREFIX_D));
    }

    @Test
    void parseUnnamedValue_emptyToken_returnEmptyResult() {
        // execute
        Optional<String> result = CommandParam.parseUnnamedValue(tokens, prefixes);

        // verify
        assertFalse(result.isPresent());
    }

    @Test
    void parseUnnamedValue_nonEmptyTokenNoPrefix_returnFullString() {
        // setup
        final String expected = "hello world how are you";
        tokens.addAll(Arrays.asList(expected.split(" ")));

        // execute
        Optional<String> result = CommandParam.parseUnnamedValue(tokens, prefixes);

        // verify
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void parseUnnamedValue_nonEmptyTokenWithPrefix_returnStringBeforePrefix() {
        // setup
        final String expected = "hello world how are you";
        tokens.addAll(Arrays.asList(expected.split(" ")));
        tokens.addAll(List.of(PREFIX_A, "value1", "value2"));

        // execute
        Optional<String> result = CommandParam.parseUnnamedValue(tokens, prefixes);

        // verify
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void parseNamedValues_emptyTokenNonEmptyPrefixes_returnResultOfSizePrefixes() {
        // execute
        Map<String, Optional<String>> result = CommandParam.parseNamedValues(tokens, prefixes);

        // verify
        assertFalse(result.isEmpty());
        assertEquals(prefixes.size(), result.size());
        for (String prefix : prefixes) {
            assertTrue(result.containsKey(prefix));
            assertFalse(result.get(prefix).isPresent());
        }
    }

    @Test
    void parseNamedValues_nonEmptyTokenNonEmptyPrefixes_returnResultOfSizePrefixes() {
        // setup
        tokens.addAll(List.of(PREFIX_A, "value1", "value2", PREFIX_B, "value2", PREFIX_C, "value3"));
        // execute
        Map<String, Optional<String>> result = CommandParam.parseNamedValues(tokens, prefixes);
        // verify
        assertFalse(result.isEmpty());
        assertEquals(prefixes.size(), result.size());
        for (String prefix : prefixes) {
            assertTrue(result.containsKey(prefix));
        }
        assertEquals(Optional.of("value1 value2"), result.get(PREFIX_A));
        assertEquals(Optional.of("value2"), result.get(PREFIX_B));
        assertEquals(Optional.of("value3"), result.get(PREFIX_C));
        assertEquals(Optional.empty(), result.get(PREFIX_D));
    }

    @Test
    void parseNamedValues_unnamedNotRemoved_shouldRemoveUnnamedAndReturnCorrectValues() {
        tokens.addAll(List.of("hello", "world", PREFIX_A, "value1", "value2", PREFIX_B, "value2", PREFIX_C, "value3"));
        // execute
        Map<String, Optional<String>> result = CommandParam.parseNamedValues(tokens, prefixes);

        // verify
        assertFalse(result.isEmpty());
        assertEquals(prefixes.size(), result.size());
        for (String prefix : prefixes) {
            assertTrue(result.containsKey(prefix));
        }
        assertEquals(Optional.of("value1 value2"), result.get(PREFIX_A));
        assertEquals(Optional.of("value2"), result.get(PREFIX_B));
        assertEquals(Optional.of("value3"), result.get(PREFIX_C));
        assertEquals(Optional.empty(), result.get(PREFIX_D));
    }

    @Test
    void getUnnamedValue_emptyToken_returnEmpty() {
        // setup
        CommandParam param = new CommandParam(Optional.empty(), Optional.empty());
        // execute
        Optional<String> result = param.getUnnamedValue();

        // verify
        assertFalse(result.isPresent());
    }

    @Test
    void getUnnamedValue_nonEmptyToken_returnToken() {
        // setup
        Optional<String> expected = Optional.of("token");
        CommandParam param = new CommandParam(expected, Optional.empty());
        // execute
        Optional<String> result = param.getUnnamedValue();

        // verify
        assertTrue(result.isPresent());
        assertEquals(expected, result);
    }

    @Test
    void getUnnamedValueOrThrow_emptyToken_throwsException() {
        // setup
        CommandParam param = new CommandParam(Optional.empty(), Optional.empty());
        // execute
        assertThrows(ParseException.class, param::getUnnamedValueOrThrow);
    }

    @Test
    void getNamedValues_emptyToken_returnEmpty() {
        // setup
        CommandParam param = new CommandParam(Optional.empty(), Optional.empty());
        // execute
        Optional<Map<String, Optional<String>>> result = param.getNamedValues();

        // verify
        assertFalse(result.isPresent());
    }

    @Test
    void getNamedValues_nonEmptyToken_returnToken() {
        // setup
        Optional<Map<String, Optional<String>>> expected = Optional.of(new HashMap<>());
        CommandParam param = new CommandParam(Optional.empty(), expected);
        // execute
        Optional<Map<String, Optional<String>>> result = param.getNamedValues();

        // verify
        assertTrue(result.isPresent());
        assertEquals(expected, result);
    }

    @Test
    void getNamedValuesOrThrow_emptyToken_throwsException() {
        // setup
        CommandParam param = new CommandParam(Optional.empty(), Optional.empty());
        CommandParam param2 = new CommandParam(Optional.of("token"),
            Optional.of(Map.of(PREFIX_A, Optional.empty())));
        // execute
        assertThrows(ParseException.class,
            () -> param.getNamedValuesOrThrow(PREFIX_A));
        assertThrows(ParseException.class,
            () -> param2.getNamedValuesOrThrow(PREFIX_A));
    }

    @Test
    void getUnnamedIntOrThrow_emptyToken_throwsException() {
        // setup
        CommandParam param = new CommandParam(Optional.empty(), Optional.empty());
        // execute
        assertThrows(ParseException.class, param::getUnnamedIntOrThrow);
    }

    @Test
    void getUnnamedIntOrThrow_nonEmptyTokenNonInt_throwsException() {
        // setup
        CommandParam param = new CommandParam(Optional.of("token"), Optional.empty());
        // execute
        assertThrows(ParseException.class, param::getUnnamedIntOrThrow);
    }

    @Test
    void getUnnamedIntOrThrow_nonEmptyTokenInt_returnInt() throws ParseException {
        // setup
        CommandParam param = new CommandParam(Optional.of("1"), Optional.empty());
        // execute
        int result = param.getUnnamedIntOrThrow();
        // verify
        assertEquals(1, result);
    }

    @Test
    void getNamedIntOrThrow_emptyToken_throwsException() {
        // setup
        CommandParam param = new CommandParam(Optional.empty(), Optional.empty());
        // execute
        assertThrows(ParseException.class,
            () -> param.getNamedIntOrThrow(PREFIX_A));
    }

    @Test
    void getNamedIntOrThrow_nonEmptyTokenNonInt_throwsException() {
        // setup
        CommandParam param = new CommandParam(Optional.empty(),
            Optional.of(Map.of(PREFIX_A, Optional.of("token"))));
        // execute
        assertThrows(ParseException.class,
            () -> param.getNamedIntOrThrow(PREFIX_A));
    }

    @Test
    void getNamedIntOrThrow_nonEmptyTokenInt_returnInt() throws ParseException {
        // setup
        CommandParam param = new CommandParam(Optional.empty(),
            Optional.of(Map.of(PREFIX_A, Optional.of("1"))));
        // execute
        int result = param.getNamedIntOrThrow(PREFIX_A);
        // verify
        assertEquals(1, result);
    }
}
