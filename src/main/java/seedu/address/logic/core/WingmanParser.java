package seedu.address.logic.core;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.core.exceptions.ParseException;
import seedu.address.logic.crew.addcrew.AddCrewCommandFactory;
import seedu.address.logic.crew.deletecrew.DeleteCrewCommandFactory;
import seedu.address.logic.flight.addflight.AddFlightCommandFactory;
import seedu.address.logic.flight.deleteflight.DeleteFlightCommandFactory;
import seedu.address.logic.location.addlocation.AddLocationCommandFactory;
import seedu.address.logic.location.deletelocation.DeleteLocationCommandFactory;
import seedu.address.logic.pilot.addpilot.AddPilotCommandFactory;
import seedu.address.logic.pilot.deletepilot.DeletePilotCommandFactory;
import seedu.address.logic.pilot.linkpilot.LinkPilotCommandFactory;
import seedu.address.logic.plane.addplane.AddPlaneCommandFactory;
import seedu.address.logic.plane.deleteplane.DeletePlaneCommandFactory;
import seedu.address.logic.toplevel.changemode.ChangeModeCommandFactory;
import seedu.address.model.OperationMode;

/**
 * The parser that's responsible for parsing the user input and handling the
 * commands.
 */
public class WingmanParser extends FactoryParser {
    /**
     * The command groups that are available in the application.
     */
    private static final List<CommandGroup> COMMAND_GROUPS = List.of(
        new CommandGroup(OperationMode.PILOT, List.of(
            new AddPilotCommandFactory(),
            new DeletePilotCommandFactory(),
            new LinkPilotCommandFactory()
        )),
        new CommandGroup(OperationMode.CREW, List.of(
            new AddCrewCommandFactory(),
            new DeleteCrewCommandFactory()
        )),
        new CommandGroup(OperationMode.PLANE, List.of(
            new AddPlaneCommandFactory(),
            new DeletePlaneCommandFactory(),
            new LinkPlaneCommandFactory()
        )),
        new CommandGroup(OperationMode.LOCATION, List.of(
            new AddLocationCommandFactory(),
            new DeleteLocationCommandFactory(),
            new LinkLocationCommandFactory(),
            new UnlinkLocationCommandFactory()
        )),
        new CommandGroup(OperationMode.FLIGHT, List.of(
            new AddFlightCommandFactory(),
            new DeleteFlightCommandFactory(),
            new LinkPlaneCommandFactory(),
            new UnlinkPlaneCommandFactory()
        ))
    );

    /**
     * The top level command factories that are available in the application.
     */
    private static final List<CommandFactory<?>> COMMAND_FACTORIES = List.of(
        new ChangeModeCommandFactory()
    );

    /**
     * The command groups that are available in the application.
     */
    private final List<CommandGroup> groups;

    /**
     * The list of top-level factories that produces commands.
     */
    private final List<CommandFactory<?>> factories;

    /**
     * Constructs a {@code WingmanParser} with the command groups given.
     *
     * @param commandGroups the command groups that are available in the
     *                      application.
     * @param factories     the top-level factories that are responsible
     *                      for parsing of top-level features.
     */
    public WingmanParser(List<CommandGroup> commandGroups,
        List<CommandFactory<?>> factories) {
        this.groups = commandGroups;
        this.factories = factories;
    }

    /**
     * Constructs a {@code WingmanParser} with the default {@code
     * COMMAND_GROUPS} and {@code  COMMAND_FACTORIES}
     */
    public WingmanParser() {
        this(COMMAND_GROUPS, COMMAND_FACTORIES);
    }

    /**
     * Tokenizes the user input into a {@code Deque} of {@code String}s. It
     * splits the user input by whitespace. This may result in the token
     * being unable to recover back to the original user input, i.e. if the
     * user used different whitespace characters other than spaces. However,
     * for the purpose of this application, this is acceptable.
     *
     * @param userInput the user input to tokenize.
     * @return a {@code Deque} of {@code String}s that contains the tokens.
     */
    private static Deque<String> tokenize(String userInput) {
        return new ArrayDeque<>(Arrays.asList(userInput.split("\\s+")));
    }

    /**
     * Parses the user input and returns the corresponding command.
     *
     * @param operationMode the operation mode at which the command is
     *                      executed.
     * @param userInput     the user input to parse.
     * @return the command that corresponds to the user input.
     * @throws ParseException if a parsing error exists, or if no command is
     *                        found in the parser.
     */
    public Command parse(OperationMode operationMode, String userInput) throws ParseException {
        final Deque<String> tokens = tokenize(userInput);

        final Optional<Command> topCommand = this.parseFactory(tokens);

        if (topCommand.isPresent()) {
            return topCommand.get();
        }

        final Optional<Command> groupedCommand =
            this.parseGroup(operationMode, tokens);

        if (groupedCommand.isPresent()) {
            return groupedCommand.get();
        }

        throw new ParseException("No command group found for operation mode "
                                     + operationMode);
    }

    /**
     * Helps to delegate the parsing to each command group. This is
     * abstracted out so that we can have single level of abstraction in
     * the {@code parse} method.
     *
     * @param operationMode the operation mode at which the command is
     * @param tokens        the tokens to parse.
     * @return the command that corresponds to the user input.
     * @throws ParseException if a parsing error exists. Note that if no
     *                        group is found, this method will simply return an
     *                        empty {@code Optional}.
     */
    private Optional<Command> parseGroup(OperationMode operationMode,
        Deque<String> tokens) throws ParseException {
        for (CommandGroup commandGroup : this.groups) {
            if (!commandGroup.getOperationMode().equals(operationMode)) {
                continue;
            }
            return Optional.of(commandGroup.parse(tokens));
        }
        return Optional.empty();
    }

    @Override
    protected List<CommandFactory<?>> getFactories() {
        return this.factories;
    }
}
