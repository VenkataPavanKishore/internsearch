import java.util.List;
import java.util.Scanner;

/**
 * InternSearch - Smart Internship Discovery & Skill Matching System
 *
 * Main application class providing console navigation, interactive menus,
 * and coordinating student profiles, skill matching, and bookmarking.
 */
public class InternSearch {
    private static final String RESUME_FILE = "resume.txt";
    private static final String INTERNSHIPS_FILE = "internships.txt";
    private static final String SAVED_FILE = "saved_internships.txt";

    private final Student student;
    private final SkillManager skillManager;
    private final InternshipManager internshipManager;
    private final ApplicationManager applicationManager;
    private final Scanner scanner;

    public InternSearch() {
        this.student = new Student("Candidate");
        this.skillManager = new SkillManager(student, RESUME_FILE);
        this.internshipManager = new InternshipManager(INTERNSHIPS_FILE);
        this.applicationManager = new ApplicationManager(SAVED_FILE);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        InternSearch app = new InternSearch();
        app.run();
    }

    /**
     * Main application execution loop.
     */
    public void run() {
        displayWelcomeHeader();

        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleEnterResumeSkills();
                    break;
                case "2":
                    handleFindInternships();
                    break;
                case "3":
                    handleUpdateResumeSkills();
                    break;
                case "4":
                    handleViewSavedInternships();
                    break;
                case "5":
                    handleSearchByCompany();
                    break;
                case "6":
                    running = false;
                    handleExit();
                    break;
                default:
                    System.out.println("\n[Invalid Selection] Please choose a valid option (1-6).\n");
                    break;
            }
        }
    }

    private void displayWelcomeHeader() {
        System.out.println("********************************************************************************");
        System.out.println("*            WELCOME TO INTERNSEARCH: DISCOVERY & SKILL MATCHING SYSTEM         *");
        System.out.println("*             Helping Students Bridge the Gap to Their Dream Careers            *");
        System.out.println("********************************************************************************\n");

        if (skillManager.hasSkills()) {
            System.out.printf("[Auto-Loaded Profile] Found %d saved skill(s) in %s.\n\n",
                    student.getSkillCount(), RESUME_FILE);
        } else {
            System.out.println("[Profile Setup] No existing resume skills found. Start by selecting Option 1.\n");
        }
    }

    private void displayMainMenu() {
        System.out.println("===============================================");
        System.out.println("                 INTERNSEARCH                  ");
        System.out.println("       SMART INTERNSHIP DISCOVERY SYSTEM       ");
        System.out.println("===============================================");
        System.out.println("1. Enter Resume Skills");
        System.out.println("2. Find Internships");
        System.out.println("3. Update Resume Skills");
        System.out.println("4. View Saved Internships");
        System.out.println("5. Search Internship by Company Name");
        System.out.println("6. Exit");
        System.out.print("\nEnter your choice: ");
    }

    /**
     * Feature 1: Enter Resume Skills
     */
    private void handleEnterResumeSkills() {
        skillManager.enterResumeSkillsInteractive(scanner);
    }

    /**
     * Feature 2: Find Internships & Skill Matching
     */
    private void handleFindInternships() {
        if (!skillManager.hasSkills()) {
            System.out.println("\n================================================================================");
            System.out.println("[Action Required] You have not added any resume skills yet.");
            System.out.println("To match and recommend internships, please enter your skills first using Option 1.");
            System.out.println("================================================================================\n");
            return;
        }

        List<Internship> allInternships = internshipManager.getAllInternships();
        if (allInternships.isEmpty()) {
            System.out.println("\n[Notice] No internships are currently loaded in the system.");
            return;
        }

        // Run Matching Engine to compute match percentage and rank descending
        List<MatchResult> ranked = MatchingEngine.rankInternships(student, allInternships);

        System.out.println("\n================================================================================");
        System.out.println("                     RECOMMENDED INTERNSHIPS (RANKED BY MATCH)                  ");
        System.out.println("================================================================================");
        System.out.printf("Candidate Profile: %s  |  Registered Skills: %d\n",
                String.join(", ", student.getSkills()), student.getSkillCount());
        System.out.println("--------------------------------------------------------------------------------");

        for (int i = 0; i < ranked.size(); i++) {
            MatchResult mr = ranked.get(i);
            mr.getInternship().displaySummary(i + 1, mr.getMatchPercentage());
        }

        while (true) {
            System.out.print("\nEnter an internship number (1-" + ranked.size() + ") to view full details (or 0 to return): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0") || input.equalsIgnoreCase("back") || input.equalsIgnoreCase("exit")) {
                System.out.println("Returning to Main Menu...\n");
                break;
            }

            try {
                int selection = Integer.parseInt(input);
                if (selection >= 1 && selection <= ranked.size()) {
                    MatchResult chosen = ranked.get(selection - 1);
                    Internship internship = chosen.getInternship();

                    // Display complete details
                    internship.displayFullDetails(
                            chosen.getMatchedSkills(),
                            chosen.getMissingSkills(),
                            chosen.getMatchPercentage()
                    );

                    // Feature Enhancement: Proactive Skill Recommendation
                    if (!chosen.getMissingSkills().isEmpty()) {
                        System.out.println("💡 [Skill Recommendation]");
                        System.out.printf("   To become a 100%% match for this role at %s, prioritize learning:\n",
                                internship.getCompanyName());
                        System.out.printf("   👉 %s\n\n", String.join(", ", chosen.getMissingSkills()));
                    }

                    // Prompt to save
                    System.out.print("Do you want to save/bookmark this internship? (y/N): ");
                    String saveChoice = scanner.nextLine().trim();
                    if (saveChoice.equalsIgnoreCase("y") || saveChoice.equalsIgnoreCase("yes")) {
                        applicationManager.saveInternship(internship, chosen.getMatchPercentage());
                    }
                    System.out.println();
                } else {
                    System.out.printf("[Invalid Index] Please enter a number between 1 and %d.\n", ranked.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Please enter a valid numerical choice or 0 to return.");
            }
        }
    }

    /**
     * Feature 3: Update Resume Skills Submenu
     */
    private void handleUpdateResumeSkills() {
        skillManager.handleUpdateMenu(scanner);
    }

    /**
     * Feature 4: View Saved Internships
     */
    private void handleViewSavedInternships() {
        applicationManager.viewSavedInternshipsInteractive(scanner, student);
    }

    /**
     * Feature 5: Search Internship by Company Name
     */
    private void handleSearchByCompany() {
        System.out.println("\n===============================================");
        System.out.println("      SEARCH INTERNSHIP BY COMPANY NAME        ");
        System.out.println("===============================================");
        System.out.println("Search company opportunities independently of your current resume skills.");
        System.out.print("Enter company name (or partial name): ");

        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("[Validation Error] Company search query cannot be empty.\n");
            return;
        }

        List<Internship> matches = CompanySearch.searchByCompanyName(query, internshipManager.getAllInternships());
        CompanySearch.displayCompanySearchResults(query, matches);

        if (matches.isEmpty()) {
            return;
        }

        while (true) {
            System.out.print("\nEnter an internship number (1-" + matches.size() + ") to view full details (or 0 to return): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0") || input.equalsIgnoreCase("back") || input.equalsIgnoreCase("exit")) {
                System.out.println("Returning to Main Menu...\n");
                break;
            }

            try {
                int selection = Integer.parseInt(input);
                if (selection >= 1 && selection <= matches.size()) {
                    Internship chosen = matches.get(selection - 1);
                    MatchResult liveMatch = MatchingEngine.calculateMatch(student, chosen);

                    chosen.displayFullDetails(
                            liveMatch.getMatchedSkills(),
                            liveMatch.getMissingSkills(),
                            skillManager.hasSkills() ? liveMatch.getMatchPercentage() : -1.0
                    );

                    // Prompt to save
                    System.out.print("Do you want to save/bookmark this internship? (y/N): ");
                    String saveChoice = scanner.nextLine().trim();
                    if (saveChoice.equalsIgnoreCase("y") || saveChoice.equalsIgnoreCase("yes")) {
                        double pct = skillManager.hasSkills() ? liveMatch.getMatchPercentage() : 0.0;
                        applicationManager.saveInternship(chosen, pct);
                    }
                    System.out.println();
                } else {
                    System.out.printf("[Invalid Index] Please enter a number between 1 and %d.\n", matches.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Please enter a valid number or 0 to return.");
            }
        }
    }

    /**
     * Feature 6: Graceful Application Shutdown
     */
    private void handleExit() {
        System.out.println("\n================================================================================");
        System.out.println("              THANK YOU FOR USING INTERNSEARCH! HAPPY LEARNING!                 ");
        System.out.println("================================================================================");
        System.out.println("All your resume skills and bookmarked internships have been preserved securely.");
        System.out.println("Data Files:");
        System.out.printf("  • Resume Skills     : %s\n", RESUME_FILE);
        System.out.printf("  • Saved Internships : %s\n", SAVED_FILE);
        System.out.printf("  • Internship Dataset: %s\n", INTERNSHIPS_FILE);
        System.out.println("Exiting application. Goodbye!\n");
    }
}
