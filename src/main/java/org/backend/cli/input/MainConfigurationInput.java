package org.backend.cli.input;

import org.backend.cli.output.JsonReader;
import org.backend.dto.MainConfigurationDto;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainConfigurationInput {
    private static final int MAX_ATTEMPTS = 3;
    private static final Scanner scanner = new Scanner(System.in);

    private static int getValidIntegerInput(String prompt, int minValue, int maxValue) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.println(prompt + " (between " + minValue + " and " + maxValue + "): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty");
                }
                int value = Integer.parseInt(input);
                if (value < minValue || value > maxValue) {
                    throw new IllegalArgumentException(String.format("Value must be between %d and %d", minValue, maxValue));
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
                attempts++;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                attempts++;
            }
            if (attempts < MAX_ATTEMPTS) {
                System.out.println("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
            }
        }
        System.out.println("Maximum number of attempts reached. Program will now exit.");
        return -1;
    }

    private static String getValidStringInput(String prompt, String... matchValues) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty");
                }
                for (String matchValue : matchValues) {
                    if (input.matches(matchValue)) {
                        return input;
                    }
                }
                throw new IllegalArgumentException("Value need to be one of the following: " + Arrays.toString(matchValues));
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
                attempts++;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                attempts++;
            }
            if (attempts < MAX_ATTEMPTS) {
                System.out.println("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
            }
        }
        System.out.println("Maximum number of attempts reached. Program will now exit.");
        return null;
    }

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