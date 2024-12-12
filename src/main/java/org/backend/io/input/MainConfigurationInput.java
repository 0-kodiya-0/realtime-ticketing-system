package org.backend.io.input;

import org.backend.dto.MainConfigurationDto;
import org.backend.enums.FilePaths;
import org.backend.io.file.JsonReader;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Define the logic that get the system configuration input from the user
 */
public class MainConfigurationInput extends UserInputAbstract {

    /**
     * Prompts for permission to load the main configuration saved previously
     * @return Return the permission
     */
    private static boolean loadMainConfiguration() {
        File file = new File(FilePaths.CONFIG.toString());
        if (!file.exists()) {
            return false;
        }
        // Ask for the user input
        String loadConfiguration = getValidStringInput("Load the existing configuration (Y/N) : ", "Y", "N");
        if (loadConfiguration == null) {
            return false;
        }
        return loadConfiguration.equals("Y");
    }

    /**
     * Get the input for the configuration parameters for the user
     * @return Creates and return the Dto for configuration input from the user
     */
    public static MainConfigurationDto getMainConfiguration() {
        MainConfigurationDto mainConfigurationDto = new MainConfigurationDto();
        mainConfigurationDto.setTotalNumberOfTickets(getValidIntegerInput("Enter total number of tickets", 10, 100));
        mainConfigurationDto.setTicketReleaseRate(getValidIntegerInput("Enter ticket release rate (tickets per minute)", 1000, (int) TimeUnit.MINUTES.toMillis(1)));
        mainConfigurationDto.setTicketRetrievalRate(getValidIntegerInput("Enter ticket retrieval rate (tickets per minute)", 500, (int) TimeUnit.MINUTES.toMillis(1)));
        mainConfigurationDto.setMaximumTicketCapacity(getValidIntegerInput("Enter maximum ticket capacity", 10, 500));
        return mainConfigurationDto;
    }

    /**
     * Loads the main configuration data from a JSON file or prompts the user for input.
     *
     * @return the main configuration data as a {@link MainConfigurationDto} object
     * @throws IOException if an I/O error occurs while reading the JSON file
     */
    public static MainConfigurationDto getInput() throws IOException {
        if (loadMainConfiguration()) {
            return JsonReader.jsonToMap(FilePaths.CONFIG.toString(), MainConfigurationDto.class);
        }
        return getMainConfiguration();
    }
}