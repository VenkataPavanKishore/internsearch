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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class dedicated to file persistence, defensive I/O handling,
 * and reliable parsing of resume, internship, and application data files.
 */
public class FileManager {
    public static final String RESUME_FILE = "resume.txt";
    public static final String INTERNSHIPS_FILE = "internships.txt";
    public static final String SAVED_FILE = "saved_internships.txt";

    /**
     * Ensures that a file exists on disk. Creates it if missing.
     */
    public static void ensureFileExists(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("[Warning] Unable to create file: " + filename + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Loads student skills from resume.txt.
     * Supports both comma-delimited strings and newline-separated entries.
     */
    public static List<String> loadResumeSkills(String filename) {
        List<String> skills = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return skills;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Split line by comma in case multiple skills are stored on one line
                String[] tokens = line.split(",");
                for (String token : tokens) {
                    String clean = token.trim();
                    if (!clean.isEmpty()) {
                        skills.add(clean);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Failed to read " + filename + ": " + e.getMessage());
        }
        return skills;
    }

    /**
     * Saves student skills into resume.txt formatted as comma-separated values.
     */
    public static boolean saveResumeSkills(String filename, Collection<String> skills) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, false), StandardCharsets.UTF_8))) {
            if (skills != null && !skills.isEmpty()) {
                String content = String.join(", ", skills);
                writer.write(content);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[Error] Failed to save skills to " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads all internship records from internships.txt.
     */
    public static List<Internship> loadInternships(String filename) {
        List<Internship> list = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                Internship in = Internship.fromFileRecord(line);
                if (in != null && in.getId() != null && !in.getId().isEmpty()) {
                    list.add(in);
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Failed to read " + filename + ": " + e.getMessage());
        }
        return list;
    }

    /**
     * Saves a list of internships to internships.txt.
     */
    public static boolean saveInternships(String filename, List<Internship> list) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, false), StandardCharsets.UTF_8))) {
            for (Internship in : list) {
                writer.write(in.toFileRecord());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[Error] Failed to write internships to " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Represents a saved record containing the internship and the student's match percentage when saved.
     */
    public static class SavedEntry {
        public Internship internship;
        public double matchPercentage;

        public SavedEntry(Internship internship, double matchPercentage) {
            this.internship = internship;
            this.matchPercentage = matchPercentage;
        }
    }

    /**
     * Loads saved internships and their recorded match percentages from saved_internships.txt.
     */
    public static List<SavedEntry> loadSavedInternships(String filename) {
        List<SavedEntry> list = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Format: <internship record>|<savedMatchPercentage>
                int lastPipe = line.lastIndexOf('|');
                double percentage = 0.0;
                String internshipData = line;

                if (lastPipe != -1) {
                    String pctStr = line.substring(lastPipe + 1).trim();
                    try {
                        percentage = Double.parseDouble(pctStr);
                        internshipData = line.substring(0, lastPipe);
                    } catch (NumberFormatException nfe) {
                        // If no percentage was attached, parse the whole line as internship
                        internshipData = line;
                    }
                }

                Internship in = Internship.fromFileRecord(internshipData);
                if (in != null) {
                    list.add(new SavedEntry(in, percentage));
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Failed to load saved internships from " + filename + ": " + e.getMessage());
        }
        return list;
    }

    /**
     * Saves the list of saved entries to saved_internships.txt.
     */
    public static boolean saveSavedInternships(String filename, List<SavedEntry> entries) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, false), StandardCharsets.UTF_8))) {
            for (SavedEntry entry : entries) {
                String line = entry.internship.toFileRecord() + "|" + entry.matchPercentage;
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[Error] Failed to save entries to " + filename + ": " + e.getMessage());
            return false;
        }
    }
}
