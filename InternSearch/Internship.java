import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class representing an Internship opportunity.
 * Encapsulates all attributes of an internship post including required skills,
 * stipend, location, work mode, eligibility, and application details.
 */
public class Internship {
    private String id;
    private String companyName;
    private String role;
    private List<String> requiredSkills;
    private String description;
    private String location;
    private String duration;
    private String stipend;
    private String workMode;
    private String eligibility;
    private String applicationDeadline;
    private String applicationUrl;

    public Internship() {
        this.requiredSkills = new ArrayList<>();
    }

    public Internship(String id, String companyName, String role, List<String> requiredSkills,
                      String description, String location, String duration, String stipend,
                      String workMode, String eligibility, String applicationDeadline, String applicationUrl) {
        this.id = id != null ? id.trim() : "";
        this.companyName = companyName != null ? companyName.trim() : "Unknown Company";
        this.role = role != null ? role.trim() : "General Intern";
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.description = description != null ? description.trim() : "No description provided.";
        this.location = location != null ? location.trim() : "Not specified";
        this.duration = duration != null ? duration.trim() : "Flexible";
        this.stipend = stipend != null ? stipend.trim() : "Unpaid / Not Disclosed";
        this.workMode = workMode != null ? workMode.trim() : "Hybrid";
        this.eligibility = eligibility != null ? eligibility.trim() : "Open to all students";
        this.applicationDeadline = applicationDeadline != null ? applicationDeadline.trim() : "Rolling";
        this.applicationUrl = applicationUrl != null ? applicationUrl.trim() : "N/A";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }

    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public String getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(String applicationDeadline) { this.applicationDeadline = applicationDeadline; }

    public String getApplicationUrl() { return applicationUrl; }
    public void setApplicationUrl(String applicationUrl) { this.applicationUrl = applicationUrl; }

    /**
     * Converts the internship object to a pipe-delimited string record for file storage.
     */
    public String toFileRecord() {
        String skillsJoined = String.join(",", requiredSkills);
        return String.join("|",
                escapeDelimiter(id),
                escapeDelimiter(companyName),
                escapeDelimiter(role),
                escapeDelimiter(skillsJoined),
                escapeDelimiter(description),
                escapeDelimiter(location),
                escapeDelimiter(duration),
                escapeDelimiter(stipend),
                escapeDelimiter(workMode),
                escapeDelimiter(eligibility),
                escapeDelimiter(applicationDeadline),
                escapeDelimiter(applicationUrl)
        );
    }

    /**
     * Parses a pipe-delimited record string from internships.txt into an Internship object.
     * Gracefully handles missing fields.
     */
    public static Internship fromFileRecord(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);
        String id = parts.length > 0 ? parts[0].trim() : "INT-000";
        String company = parts.length > 1 ? parts[1].trim() : "Unknown Company";
        String role = parts.length > 2 ? parts[2].trim() : "Intern";

        List<String> skills = new ArrayList<>();
        if (parts.length > 3 && !parts[3].trim().isEmpty()) {
            String[] skillTokens = parts[3].split(",");
            for (String s : skillTokens) {
                if (!s.trim().isEmpty()) {
                    skills.add(s.trim());
                }
            }
        }

        String desc = parts.length > 4 ? parts[4].trim() : "Opportunity details available upon request.";
        String loc = parts.length > 5 ? parts[5].trim() : "Pan-India";
        String dur = parts.length > 6 ? parts[6].trim() : "3 Months";
        String stipend = parts.length > 7 ? parts[7].trim() : "Disclosed after interview";
        String mode = parts.length > 8 ? parts[8].trim() : "Hybrid";
        String elig = parts.length > 9 ? parts[9].trim() : "College Students & Freshers";
        String deadline = parts.length > 10 ? parts[10].trim() : "Open until filled";
        String url = parts.length > 11 ? parts[11].trim() : "https://careers.internsearch.org/apply";

        return new Internship(id, company, role, skills, desc, loc, dur, stipend, mode, elig, deadline, url);
    }

    private static String escapeDelimiter(String value) {
        if (value == null) return "";
        return value.replace("|", "/");
    }

    /**
     * Displays a clean, concise card summary for recommendation and search listings.
     */
    public void displaySummary(int number, double matchPercentage) {
        System.out.printf("[%d] %s — %s\n", number, companyName, role);
        System.out.printf("    Match: %.1f%%  |  Location: %s  |  Mode: %s\n", matchPercentage, location, workMode);
        System.out.printf("    Duration: %s  |  Stipend: %s\n", duration, stipend);
        System.out.printf("    Overview: %s\n", description);
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Displays full details of the internship opportunity.
     */
    public void displayFullDetails(List<String> matchedSkills, List<String> missingSkills, double matchPercentage) {
        System.out.println("\n================================================================================");
        System.out.println("                         INTERNSHIP FULL SPECIFICATION                          ");
        System.out.println("================================================================================");
        System.out.printf("Company Name         : %s\n", companyName);
        System.out.printf("Internship Role      : %s\n", role);
        System.out.printf("Company Description  : %s\n", description);
        System.out.printf("Location             : %s\n", location);
        System.out.printf("Work Mode            : %s\n", workMode);
        System.out.printf("Duration             : %s\n", duration);
        System.out.printf("Stipend              : %s\n", stipend);
        System.out.printf("Eligibility Criteria : %s\n", eligibility);
        System.out.printf("Application Deadline : %s\n", applicationDeadline);
        System.out.printf("Application Portal   : %s\n", applicationUrl);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Total Required Skills: %s\n", String.join(", ", requiredSkills));
        if (matchedSkills != null) {
            String matchedStr = matchedSkills.isEmpty() ? "None" : String.join(", ", matchedSkills);
            System.out.printf("Matched Skills (%d)   : %s\n", matchedSkills.size(), matchedStr);
        }
        if (missingSkills != null) {
            String missingStr = missingSkills.isEmpty() ? "None (Perfect Match!)" : String.join(", ", missingSkills);
            System.out.printf("Missing Skills (%d)   : %s\n", missingSkills.size(), missingStr);
        }
        if (matchPercentage >= 0) {
            System.out.printf("Skill Match Score    : %.1f%%\n", matchPercentage);
        }
        System.out.println("================================================================================\n");
    }
}
