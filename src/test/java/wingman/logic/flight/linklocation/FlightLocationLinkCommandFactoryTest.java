package wingman.logic.flight.linklocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wingman.commons.fp.Lazy;
import wingman.logic.core.CommandParam;
import wingman.logic.core.exceptions.CommandException;
import wingman.logic.core.exceptions.ParseException;
import wingman.model.Model;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FlightLocationLinkCommandFactoryTest {

    private FlightLocationLinkCommandFactory<LinkFlightToLocationCommand> linkFactory;
    private FlightLocationLinkCommandFactory<UnlinkFlightToLocationCommand> unlinkFactory;
    private Lazy<Model> modelLazy;

    @Mock
    Model model;

    @BeforeEach
    public void setUp() {
        modelLazy = Lazy.of(model);
        linkFactory = new FlightLocationLinkCommandFactory<>(
                modelLazy,
                LinkFlightToLocationCommand::new,
                "linklocation"
        );
        unlinkFactory = new FlightLocationLinkCommandFactory<>(
                modelLazy,
                UnlinkFlightToLocationCommand::new,
                "unlinklocation"
        );
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(
                Optional.of(Set.of("/fl", "/from", "/to")),
                linkFactory.getPrefixes()
        );
        assertEquals(
                Optional.of(Set.of("/fl", "/from", "/to")),
                unlinkFactory.getPrefixes()
        );
    }

    @Test
    public void testGetCommandWord() {
        assertEquals("linklocation", linkFactory.getCommandWord());
        assertEquals("unlinklocation", unlinkFactory.getCommandWord());
    }

    @Test
    public void testCreateCommand_linkFactory() throws ParseException, CommandException {
        CommandParam param = new CommandParam(
                Optional.empty(),
                Optional.of(
                        Map.of(
                                "/fl", Optional.of("1"),
                                "/from", Optional.of("1"),
                                "/to", Optional.of("2"))));
        LinkFlightToLocationCommand command = linkFactory.createCommand(param);
        assertNotNull(command);
    }
}