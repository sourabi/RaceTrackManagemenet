package com.example.geektrust;

import com.example.geektrust.inputhandler.InputFormatter;
import com.example.geektrust.inputhandler.InputRequest;
import com.example.geektrust.inputhandler.Operation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(args[0]);
            final Scanner scanner = new Scanner(fileInputStream);
            final InputFormatter inputFormatter = InputFormatter.getInstance();
            while (scanner.hasNextLine()) {
                final String input = scanner.nextLine();
                final InputRequest inputRequest = inputFormatter.formatInput(input);
                final Operation inputExecutor = inputRequest.getOperation();
                inputExecutor.execute(inputRequest);
            }
            scanner.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }


    }
}
