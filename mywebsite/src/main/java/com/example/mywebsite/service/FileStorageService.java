package com.example.mywebsite.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    /**
     * Resolves an accessible file path, checking project directory and parent workspace.
     */
    public File resolveFile(String fileName) {
        // Try current working dir
        File f1 = new File(fileName);
        if (f1.exists()) return f1;

        // Try data/
        File f2 = new File("src/main/resources/data/" + fileName);
        if (f2.exists()) return f2;

        // Try parent directory
        File f3 = new File("../InternSearch/" + fileName);
        if (f3.exists()) return f3;

        File f4 = new File("../" + fileName);
        if (f4.exists()) return f4;

        // Default to local file
        return f1;
    }

    public List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = resolveFile(fileName);
        if (!file.exists() || file.length() == 0) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("[FileStorageService] Error reading " + fileName + ": " + e.getMessage());
        }
        return lines;
    }

    public boolean writeLines(String fileName, List<String> lines) {
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[FileStorageService] Error writing " + fileName + ": " + e.getMessage());
            return false;
        }
    }
}
