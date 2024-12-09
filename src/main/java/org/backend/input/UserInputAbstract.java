package org.backend.input;

import java.util.Arrays;
import java.util.Scanner;

public abstract class UserInputAbstract {
    private static final int MAX_ATTEMPTS = 3;
    private static final Scanner scanner = new Scanner(System.in);

    protected static int getValidIntegerInput(String prompt, int minValue, int maxValue) {
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

    protected static String getValidStringInput(String prompt, String... matchValues) {
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
}
