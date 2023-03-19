package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.GetUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.AddressBook;
import seedu.address.model.ItemManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyItemManager;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.crew.Crew;
import seedu.address.model.flight.Flight;
import seedu.address.model.item.Item;
import seedu.address.model.location.Location;
import seedu.address.model.pilot.Pilot;
import seedu.address.model.plane.Plane;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.ItemStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.storage.json.storage.JsonCrewManagerStorage;
import seedu.address.storage.json.storage.JsonFlightManagerStorage;
import seedu.address.storage.json.storage.JsonLocationManagerStorage;
import seedu.address.storage.json.storage.JsonPilotManagerStorage;
import seedu.address.storage.json.storage.JsonPlaneManagerStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 0, true);
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();
        initFields();
        configureServiceLocator();
    }

    private void initFields() {
        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        initLogging(config);
        storage = initStorage(userPrefs, userPrefsStorage);
        model = initModelManager(storage, userPrefs);
        logic = new LogicManager(model, storage);
        ui = new UiManager(logic);
    }

    private void configureServiceLocator() {
        GetUtil.put(Config.class, config);
        GetUtil.put(Storage.class, storage);
        GetUtil.put(Model.class, model);
        GetUtil.put(Logic.class, logic);
    }

    private <T extends Item> ReadOnlyItemManager<T> readManager(
        Callable<Optional<? extends ReadOnlyItemManager<T>>> managerSupplier,
        String managerName
    ) {
        Optional<? extends ReadOnlyItemManager<T>> managerOptional;
        try {
            managerOptional = managerSupplier.call();
            if (managerOptional.isPresent()) {
                return managerOptional.get();
            }
            logger.info(
                String.format("Data file for %s is not found.", managerName)
            );
            return new ItemManager<>();
        } catch (DataConversionException | IOException e) {
            logger.info(String.format(
                "Exception caught while reading data file for %s: %s",
                managerName,
                e.toString()
            ));
            return new ItemManager<>();
        } catch (Exception e) {
            logger.info(String.format("Unknown Exception: %s.", e.getMessage()));
            return new ItemManager<>();
        }
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if {@code storage}'s address book is not found,
     * or an empty address book will be used instead if errors occur when reading {@code storage}'s address book.
     */
    private Model initModelManager(Storage storage,
        ReadOnlyUserPrefs userPrefs) {
        ReadOnlyItemManager<Pilot> pilotManager =
            readManager(storage::readPilotManager, "PilotManager");
        ReadOnlyItemManager<Location> locationManager =
            readManager(storage::readLocationManager, "LocationManager");
        ReadOnlyItemManager<Crew> crewManager =
            readManager(storage::readCrewManager, "CrewManager");
        ReadOnlyItemManager<Plane> planeManager =
            readManager(storage::readPlaneManager, "PlaneManager");
        ReadOnlyItemManager<Flight> flightManager =
            readManager(storage::readFlightManager, "FlightManager");

        return new ModelManager(userPrefs, pilotManager, locationManager,
            crewManager, planeManager, flightManager);
    }

    private Storage initStorage(UserPrefs userPrefs,
        UserPrefsStorage userPrefsStorage) {
        ItemStorage<Pilot> pilotStorage =
            new JsonPilotManagerStorage(userPrefs.getPilotManagerFilePath());
        ItemStorage<Location> locationStorage =
            new JsonLocationManagerStorage(userPrefs.getLocationManagerFilePath());
        ItemStorage<Crew> crewStorage =
            new JsonCrewManagerStorage(userPrefs.getCrewManagerFilePath());
        ItemStorage<Plane> planeStorage =
            new JsonPlaneManagerStorage(userPrefs.getPlaneManagerFilePath());
        ItemStorage<Flight> flightStorage =
            new JsonFlightManagerStorage(userPrefs.getFlightManagerFilePath());
        return new StorageManager(userPrefsStorage, pilotStorage,
            locationStorage,
            crewStorage, planeStorage, flightStorage);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                               + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                               + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AddressBook");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
