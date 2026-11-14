import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controller class responsible for managing student resume skills,
 * user interactions for skills entry/updates, input validation, and disk synchronization.
 */
public class SkillManager {
    private final Student student;
    private final String resumeFilePath;

    public SkillManager(Student student, String resumeFilePath) {
        this.student = student;
        this.resumeFilePath = resumeFilePath;
        loadSkillsFromDisk();
    }

    /**
     * Initializes skills from disk on application startup.
     */
    public void loadSkillsFromDisk() {
        List<String> loaded = FileManager.loadResumeSkills(resumeFilePath);
        student.setSkills(loaded);
    }

    /**
     * Persists current skills to resume.txt.
     */
    public boolean saveSkillsToDisk() {
        return FileManager.saveResumeSkills(resumeFilePath, student.getSkills());
    }

    public Student getStudent() {
        return student;
    }

    public List<String> getSkills() {
        return student.getSkills();
    }

    public boolean hasSkills() {
        return student.getSkillCount() > 0;
    }

    /**
     * Displays all currently stored resume skills.
     */
    public void displayAllSkills() {
        List<String> current = student.getSkills();
        System.out.println("\n-----------------------------------------------");
        System.out.println("            CURRENT RESUME SKILLS              ");
        System.out.println("-----------------------------------------------");
        if (current.isEmpty()) {
            System.out.println("No skills currently added to your profile.");
        } else {
            System.out.printf("Total Skills: %d\n\n", current.size());
            for (int i = 0; i < current.size(); i++) {
                System.out.printf("  %2d. %s\n", (i + 1), current.get(i));
            }
        }
        System.out.println("-----------------------------------------------\n");
    }

    /**
     * Feature 1: Allows student to input multiple comma-separated skills.
     */
    public void enterResumeSkillsInteractive(Scanner scanner) {
        System.out.println("\n===============================================");
        System.out.println("            ENTER RESUME SKILLS                ");
        System.out.println("===============================================");
        System.out.println("Enter your technical skills separated by commas.");
        System.out.println("Examples: Java, Python, Spring Boot, MySQL, Docker, React, Git, AWS");
        System.out.print("\nSkills: ");

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("[Validation Error] Input cannot be empty. No skills were added.");
            return;
        }

        String[] tokens = input.split(",");
        int addedCount = 0;
        int duplicateCount = 0;
        List<String> addedList = new ArrayList<>();

        for (String token : tokens) {
            String skill = token.trim();
            if (skill.isEmpty()) continue;

            if (student.containsSkill(skill)) {
                duplicateCount++;
            } else {
                student.addSkill(skill);
                addedList.add(skill);
                addedCount++;
            }
        }

        if (addedCount > 0) {
            saveSkillsToDisk();
            System.out.printf("\n[Success] %d new skill(s) successfully added!\n", addedCount);
            if (duplicateCount > 0) {
                System.out.printf("[Note] %d duplicate skill(s) were ignored.\n", duplicateCount);
            }
        } else if (duplicateCount > 0) {
            System.out.println("\n[Info] All entered skills are already present in your profile.");
        } else {
            System.out.println("\n[Validation Error] No valid technical skills found in input.");
        }

        displayAllSkills();
    }

    /**
     * Feature 3: Submenu for updating resume skills (Add, Delete, Edit, View, Back).
     */
    public void handleUpdateMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n===============================================");
            System.out.println("             UPDATE RESUME SKILLS              ");
            System.out.println("===============================================");
            System.out.println("1. Add a Skill");
            System.out.println("2. Delete a Skill");
            System.out.println("3. Edit a Skill");
            System.out.println("4. View All Skills");
            System.out.println("5. Back to Main Menu");
            System.out.print("\nEnter your choice: ");

            String choiceInput = scanner.nextLine().trim();
            switch (choiceInput) {
                case "1":
                    handleAddSkill(scanner);
                    break;
                case "2":
                    handleDeleteSkill(scanner);
                    break;
                case "3":
                    handleEditSkill(scanner);
                    break;
                case "4":
                    displayAllSkills();
                    break;
                case "5":
                    back = true;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("[Invalid Choice] Please select an option between 1 and 5.");
            }
        }
    }

    private void handleAddSkill(Scanner scanner) {
        System.out.print("\nEnter skill(s) to add (separate multiple skills with commas): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("[Validation Error] Skill name cannot be empty.");
            return;
        }

        String[] tokens = input.split(",");
        int added = 0;
        int dup = 0;
        for (String t : tokens) {
            String s = t.trim();
            if (s.isEmpty()) continue;
            if (student.containsSkill(s)) {
                dup++;
            } else {
                student.addSkill(s);
                added++;
            }
        }

        if (added > 0) {
            saveSkillsToDisk();
            System.out.printf("[Success] %d skill(s) added successfully.\n", added);
        }
        if (dup > 0) {
            System.out.printf("[Notice] %d duplicate skill(s) were skipped.\n", dup);
        }
    }

    private void handleDeleteSkill(Scanner scanner) {
        List<String> current = student.getSkills();
        if (current.isEmpty()) {
            System.out.println("\n[Notice] You have no skills to delete. Please add skills first.");
            return;
        }

        displayAllSkills();
        System.out.print("Enter the number or the exact name of the skill to delete: ");
        String target = scanner.nextLine().trim();

        if (target.isEmpty()) {
            System.out.println("[Validation Error] Input cannot be blank.");
            return;
        }

        String skillToRemove = null;
        // Check if numeric index
        try {
            int index = Integer.parseInt(target);
            if (index >= 1 && index <= current.size()) {
                skillToRemove = current.get(index - 1);
            }
        } catch (NumberFormatException ignored) {
            // Not a number, will search by name
        }

        if (skillToRemove == null) {
            for (String s : current) {
                if (s.equalsIgnoreCase(target)) {
                    skillToRemove = s;
                    break;
                }
            }
        }

        if (skillToRemove != null && student.removeSkill(skillToRemove)) {
            saveSkillsToDisk();
            System.out.printf("[Success] Skill '%s' was removed from your profile.\n", skillToRemove);
        } else {
            System.out.printf("[Error] Skill '%s' was not found in your resume.\n", target);
        }
    }

    private void handleEditSkill(Scanner scanner) {
        List<String> current = student.getSkills();
        if (current.isEmpty()) {
            System.out.println("\n[Notice] You have no skills to edit. Please add skills first.");
            return;
        }

        displayAllSkills();
        System.out.print("Enter the number or the exact name of the skill you want to edit: ");
        String target = scanner.nextLine().trim();

        if (target.isEmpty()) {
            System.out.println("[Validation Error] Input cannot be blank.");
            return;
        }

        String oldSkill = null;
        try {
            int index = Integer.parseInt(target);
            if (index >= 1 && index <= current.size()) {
                oldSkill = current.get(index - 1);
            }
        } catch (NumberFormatException ignored) {
        }

        if (oldSkill == null) {
            for (String s : current) {
                if (s.equalsIgnoreCase(target)) {
                    oldSkill = s;
                    break;
                }
            }
        }

        if (oldSkill == null) {
            System.out.printf("[Error] Skill '%s' was not found in your resume.\n", target);
            return;
        }

        System.out.printf("Current skill name: %s\n", oldSkill);
        System.out.print("Enter new skill name: ");
        String newSkill = scanner.nextLine().trim();

        if (newSkill.isEmpty()) {
            System.out.println("[Validation Error] New skill name cannot be blank.");
            return;
        }

        if (student.editSkill(oldSkill, newSkill)) {
            saveSkillsToDisk();
            System.out.printf("[Success] Updated '%s' to '%s'.\n", oldSkill, newSkill);
        } else {
            System.out.println("[Error] Could not update skill. A skill with that name may already exist.");
        }
    }
}
