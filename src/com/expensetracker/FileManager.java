package com.expensetracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static List<String> readLines(String fileName) {
        Path filePath = Path.of("data", fileName);

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }

            return Files.readAllLines(filePath);
        } catch (IOException exception) {
            System.out.println("Error reading file: " + fileName);
            return new ArrayList<>();
        }
    }

    public static void writeLines(String fileName, List<String> lines) {
        Path filePath = Path.of("data", fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines);
        } catch (IOException exception) {
            System.out.println("Error saving file: " + fileName);
        }
    }
}
