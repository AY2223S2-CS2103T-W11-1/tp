package seedu.address.logic.commands.commands;

import java.util.Optional;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.pilot.Pilot;

/**
 * The command that deletes a pilot from the address book.
 */
public class DeletePilotCommand implements Command {
    /**
     * The UUID of the pilot to be deleted.
     */
    private final String uuid;

    /**
     * This can be used to undo the deletion.
     */
    private Optional<Pilot> deletedPilot;

    /**
     * Creates a command that, when executed, deletes the pilot with the
     * given uuid.
     *
     * @param uuid The index of the pilot to be deleted.
     */
    public DeletePilotCommand(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        deletedPilot = model.getPilotManager().getItem(uuid);
        model.deletePilot(uuid);
        return new CommandResult("Deleted pilot: " + uuid);
    }
}
