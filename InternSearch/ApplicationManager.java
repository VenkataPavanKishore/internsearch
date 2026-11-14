import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controller class for managing saved internships and bookmarks.
 * Handles persistence to saved_internships.txt, prevents duplicate bookmarks,
 * and allows users to review application URLs, eligibility, and remove items.
 */
public class ApplicationManager {
    private final String savedFilePath;
    private final List<FileManager.SavedEntry> savedEntries;

    public ApplicationManager(String savedFilePath) {
        this.savedFilePath = savedFilePath;
        this.savedEntries = new ArrayList<>();
        loadSavedEntries();
    }

    /**
     * Loads saved entries from disk.
     */
    public void loadSavedEntries() {
        savedEntries.clear();
        savedEntries.addAll(FileManager.loadSavedInternships(savedFilePath));
    }

    /**
     * Persists saved entries to saved_internships.txt.
     */
    public boolean persistSavedEntries() {
        return FileManager.saveSavedInternships(savedFilePath, savedEntries);
    }

    /**
     * Checks if an internship is already saved using its unique ID.
     */
    public boolean isAlreadySaved(String internshipId) {
        if (internshipId == null) return false;
        for (FileManager.SavedEntry entry : savedEntries) {
            if (entry.internship != null && entry.internship.getId().equalsIgnoreCase(internshipId.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves an internship opportunity with its associated match percentage.
     * Prevents duplicate entries.
     */
    public boolean saveInternship(Internship internship, double matchPercentage) {
        if (internship == null) return false;

        if (isAlreadySaved(internship.getId())) {
            System.out.printf("\n[Notice] '%s - %s' is already in your saved internships list.\n",
                    internship.getCompanyName(), internship.getRole());
            return false;
        }

        savedEntries.add(new FileManager.SavedEntry(internship, matchPercentage));
        boolean success = persistSavedEntries();
        if (success) {
            System.out.printf("\n[Success] '%s - %s' successfully saved to your bookmarks!\n",
                    internship.getCompanyName(), internship.getRole());
        }
        return success;
    }

    /**
     * Removes an internship from saved records by its index or ID.
     */
    public boolean removeSavedInternship(int index) {
        if (index < 0 || index >= savedEntries.size()) {
            return false;
        }
        FileManager.SavedEntry removed = savedEntries.remove(index);
        persistSavedEntries();
        System.out.printf("\n[Success] Removed '%s - %s' from saved internships.\n",
                removed.internship.getCompanyName(), removed.internship.getRole());
        return true;
    }

    public List<FileManager.SavedEntry> getSavedEntries() {
        return savedEntries;
    }

    public int getSavedCount() {
        return savedEntries.size();
    }

    /**
     * Feature 4: Interactive menu to view, inspect, and manage saved internships.
     */
    public void viewSavedInternshipsInteractive(Scanner scanner, Student currentStudent) {
        while (true) {
            System.out.println("\n================================================================================");
            System.out.println("                         SAVED INTERNSHIPS & BOOKMARKS                          ");
            System.out.println("================================================================================");

            if (savedEntries.isEmpty()) {
                System.out.println("You have not saved any internships yet.");
                System.out.println("Tip: Use Option 2 (Find Internships) or Option 5 (Search by Company) to discover");
                System.out.println("and bookmark opportunities.");
                System.out.println("================================================================================\n");
                return;
            }

            System.out.printf("Total Bookmarked Opportunities: %d\n\n", savedEntries.size());
            for (int i = 0; i < savedEntries.size(); i++) {
                FileManager.SavedEntry entry = savedEntries.get(i);
                Internship in = entry.internship;
                System.out.printf("[%d] %s — %s\n", (i + 1), in.getCompanyName(), in.getRole());
                System.out.printf("    Saved Match Score: %.1f%%  |  Location: %s  |  Duration: %s\n",
                        entry.matchPercentage, in.getLocation(), in.getDuration());
                System.out.printf("    Stipend: %s  |  Mode: %s\n", in.getStipend(), in.getWorkMode());
                System.out.println("--------------------------------------------------------------------------------");
            }

            System.out.println("Options:");
            System.out.println("  - Enter an internship number (1-" + savedEntries.size() + ") to view full details & apply URL");
            System.out.println("  - Enter 'D <number>' (e.g., 'D 1') to delete an internship from saved records");
            System.out.println("  - Enter '0' to return to Main Menu");
            System.out.print("\nYour choice: ");

            String input = scanner.nextLine().trim();
            if (input.equals("0") || input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("back")) {
                System.out.println("Returning to Main Menu...");
                break;
            }

            // Check if user requested deletion
            if (input.toUpperCase().startsWith("D ") || input.toUpperCase().startsWith("DELETE ")) {
                String[] parts = input.split("\\s+");
                if (parts.length >= 2) {
                    try {
                        int delIdx = Integer.parseInt(parts[1]) - 1;
                        if (delIdx >= 0 && delIdx < savedEntries.size()) {
                            removeSavedInternship(delIdx);
                        } else {
                            System.out.println("[Error] Invalid internship number to delete.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[Error] Please enter a valid number after 'D'. Example: D 1");
                    }
                } else {
                    System.out.println("[Error] Please specify the number to delete. Example: D 1");
                }
                continue;
            }

            // Check if viewing details
            try {
                int selection = Integer.parseInt(input);
                if (selection >= 1 && selection <= savedEntries.size()) {
                    FileManager.SavedEntry entry = savedEntries.get(selection - 1);
                    Internship in = entry.internship;

                    // Compute fresh match details if candidate has skills
                    MatchResult liveMatch = MatchingEngine.calculateMatch(currentStudent, in);

                    in.displayFullDetails(liveMatch.getMatchedSkills(), liveMatch.getMissingSkills(), entry.matchPercentage);
                    System.out.println(">>> Direct Application URL: " + in.getApplicationUrl() + " <<<");

                    System.out.print("\nDo you want to remove this internship from your saved list? (y/N): ");
                    String delChoice = scanner.nextLine().trim();
                    if (delChoice.equalsIgnoreCase("y") || delChoice.equalsIgnoreCase("yes")) {
                        removeSavedInternship(selection - 1);
                    }
                } else {
                    System.out.printf("[Error] Please enter a number between 1 and %d (or 0 to exit).\n", savedEntries.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Unrecognized choice. Enter a number, 'D <number>', or '0'.");
            }
        }
    }
}
