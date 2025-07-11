package org.backend.io.input;

import java.util.Arrays;
import java.util.Scanner;

/**
 * An abstract class for user input validation.
 *
 * This class provides constants and methods for validating user input.
 * Subclasses can inherit and use these methods to validate integer and string inputs.
 */
public abstract class UserInputAbstract {
    private static final int MAX_ATTEMPTS = 3;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the user for a valid integer input within the specified range.
     *
     * @param prompt    the prompt message to display to the user
     * @param minValue  the minimum allowed value (inclusive)
     * @param maxValue  the maximum allowed value (inclusive)
     * @return the valid integer input entered by the user, or -1 if the maximum number of attempts is reached
     */
    protected static int getValidIntegerInput(String prompt, int minValue, int maxValue) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.print(prompt + " (only between " + minValue + " and " + maxValue + "): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty");
                }
                // Checks the input
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
        System.out.println("Maximum number of attempts reached");
        return -1;
    }

    /**
     * Prompts the user for a valid string input that matches one of the specified regular expressions.
     *
     * @param prompt       the prompt message to display to the user
     * @param matchValues  the regular expressions to match against the user input
     * @return the valid string input entered by the user, or null if the maximum number of attempts is reached
     */
    protected static String getValidStringInput(String prompt, String... matchValues) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.print(prompt);
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
        System.out.println("Maximum number of attempts reached");
        return null;
    }

    protected static boolean isEnterKeyPressed(String prompt) {
        System.out.println(prompt);
        scanner.nextLine();
        return true;
    }
}
