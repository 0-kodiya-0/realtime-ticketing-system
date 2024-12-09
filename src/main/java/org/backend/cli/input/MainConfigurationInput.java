package org.backend.cli.input;

import org.backend.cli.output.JsonReader;
import org.backend.dto.MainConfigurationDto;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainConfigurationInput extends UserInputAbstract {

    private static boolean loadMainConfiguration() {
        File file = new File("./config.json");
        if (!file.exists()) {
            return false;
        }
        String loadConfiguration = getValidStringInput("Load the existing configuration (Y/N) : ", "Y", "N");
        if (loadConfiguration == null) {
            return false;
        }
        return loadConfiguration.equals("Y");
    }

    public static MainConfigurationDto getMainConfiguration() {
        MainConfigurationDto mainConfigurationDto = new MainConfigurationDto();
        mainConfigurationDto.setTotalNumberOfTickets(getValidIntegerInput("Enter total number of tickets", 10, 100));
        mainConfigurationDto.setTicketReleaseRate(getValidIntegerInput("Enter ticket release rate (tickets per minute)", 1000, (int) TimeUnit.MINUTES.toMillis(1)));
        mainConfigurationDto.setTicketRetrievalRate(getValidIntegerInput("Enter ticket retrieval rate (tickets per minute)", 500, (int) TimeUnit.MINUTES.toMillis(1)));
        mainConfigurationDto.setMaximumTicketCapacity(getValidIntegerInput("Enter maximum ticket capacity", 10, 500));
        mainConfigurationDto.setTotalNumberOfCustomers(getValidIntegerInput("Enter total number of customers", 1, 200));
        mainConfigurationDto.setTotalNumberOfVendors(getValidIntegerInput("Enter total number of vendors", 1, 200));
        return mainConfigurationDto;
    }

    public static MainConfigurationDto getInput() throws IOException {
        if (loadMainConfiguration()) {
            return JsonReader.jsonToMap("./config.json", MainConfigurationDto.class);
        }
        return getMainConfiguration();
    }
}