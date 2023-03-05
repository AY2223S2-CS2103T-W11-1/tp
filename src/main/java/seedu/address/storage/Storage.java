package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyIdentifiableManager;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.pilot.Pilot;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    /**
     * Returns the path to the pilot manager file.
     *
     * @return the path to the pilot manager file.
     */
    Path getPilotManagerFilePath();

    /**
     * Reads the pilot manager from the {@code Storage::getPilotManagerFilePath}
     *
     * @return the pilot manager.
     */
    Optional<? extends ReadOnlyIdentifiableManager<Pilot>> readPilotManager() throws DataConversionException,
                                                         IOException;

    /**
     * Saves the pilot manager to the {@code Storage::getPiliotManagerFilePath}
     */
    void savePilotManager(ReadOnlyIdentifiableManager<Pilot> pilotManager) throws IOException;
}
