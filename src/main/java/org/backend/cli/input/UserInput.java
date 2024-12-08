package org.backend.cli.input;

import java.util.HashMap;
import java.util.Scanner;

public interface UserInput {
    Scanner scanner = new Scanner(System.in);
    void printDescription();
}
