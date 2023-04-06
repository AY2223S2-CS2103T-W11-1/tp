package wingman.logic.toplevel.add;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import wingman.logic.core.CommandParam;
import wingman.logic.core.exceptions.ParseException;
import wingman.logic.toplevel.syntax.CrewSyntax;
import wingman.logic.toplevel.syntax.PilotSyntax;
import wingman.model.crew.Crew;
import wingman.model.pilot.Pilot;

public class AddCommandFactoryTest {
    private final Optional<Set<String>> pilotPrefixes = Optional.of(PilotSyntax.PREFIXES);
    private final Optional<Set<String>> crewPrefixes = Optional.of(CrewSyntax.PREFIXES);
    private final AddCommandFactory<Pilot> pilotAddCommandFactory = new AddCommandFactory<>(
            "pilot",
            pilotPrefixes,
            PilotSyntax::add,
            PilotSyntax::factory
        );
    private final AddCommandFactory<Crew> crewAddCommandFactory = new AddCommandFactory<>(
            "crew",
            crewPrefixes,
            CrewSyntax::add,
            CrewSyntax::factory
    );

    @Test
    public void testGetCommandWord() {
        assertEquals("add", pilotAddCommandFactory.getCommandWord());
        assertEquals("add", crewAddCommandFactory.getCommandWord());
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(crewPrefixes, crewAddCommandFactory.getPrefixes());
        assertEquals(pilotPrefixes, pilotAddCommandFactory.getPrefixes());
    }

    @Test
    public void createCommand_invalidParam_throwsParseException() {
        CommandParam commandParam1 = new CommandParam(Optional.of("arg1 arg2"),
                Optional.empty());

        assertThrows(ParseException.class, (
            ) -> crewAddCommandFactory.createCommand(commandParam1));
        assertThrows(ParseException.class, (
            ) -> pilotAddCommandFactory.createCommand(commandParam1));

        Map<String, Optional<String>> namedValues = new HashMap<>();
        namedValues.put("/n", Optional.empty());

        CommandParam commandParam3 = new CommandParam(Optional.empty(),
                Optional.of(namedValues));

        assertThrows(ParseException.class, (
        ) -> crewAddCommandFactory.createCommand(commandParam3));
        assertThrows(ParseException.class, (
        ) -> pilotAddCommandFactory.createCommand(commandParam3));
    }

    @Test
    public void createAddCommand_validPilotParam_doesNotThrow() {
        String name = "name";

        Map<String, Optional<String>> namedValues = new HashMap<>();
        namedValues.put("/n", Optional.of(name));
        namedValues.put("/a", Optional.of("10"));
        namedValues.put("/g", Optional.of("0"));
        namedValues.put("/r", Optional.of("0"));
        namedValues.put("/fh", Optional.of("10000"));

        CommandParam commandParam = new CommandParam(Optional.empty(),
                Optional.of(namedValues));

        assertDoesNotThrow(() -> pilotAddCommandFactory.createCommand(commandParam));
    }

    @Test
    public void createAddCommand_validCrewParam_doesNotThrow() {
        String name = " ";

        Map<String, Optional<String>> namedValues = new HashMap<>();
        namedValues.put("/n", Optional.of(name));
        namedValues.put("/r", Optional.of("1"));

        CommandParam commandParam = new CommandParam(Optional.empty(),
                Optional.of(namedValues));

        assertDoesNotThrow(() -> crewAddCommandFactory.createCommand(commandParam));
    }
}
