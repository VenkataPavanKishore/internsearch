import java.util.ArrayList;
import java.util.List;

/**
 * Service class for company-driven search.
 * Operates independently of student resume skills to allow exploratory discovery
 * of internship roles and skill expectations across employers.
 */
public class CompanySearch {

    /**
     * Finds all internships whose company name contains the query keyword (case-insensitive).
     */
    public static List<Internship> searchByCompanyName(String query, List<Internship> dataset) {
        List<Internship> matches = new ArrayList<>();
        if (query == null || dataset == null) {
            return matches;
        }

        String normalizedQuery = query.trim().toLowerCase();
        if (normalizedQuery.isEmpty()) {
            return matches;
        }

        for (Internship in : dataset) {
            if (in.getCompanyName().toLowerCase().contains(normalizedQuery)) {
                matches.add(in);
            }
        }
        return matches;
    }

    /**
     * Displays company search results showing available roles, required skills, and key terms.
     */
    public static void displayCompanySearchResults(String query, List<Internship> results) {
        System.out.println("\n================================================================================");
        System.out.printf("           SEARCH RESULTS FOR COMPANY: \"%s\" (%d Found)\n", query.trim(), results.size());
        System.out.println("================================================================================");

        if (results.isEmpty()) {
            System.out.printf("No internships found for company matching \"%s\".\n", query.trim());
            System.out.println("Tip: Try searching for a partial name like 'Google', 'Micro', 'Amazon', or 'Infosys'.");
            System.out.println("================================================================================\n");
            return;
        }

        for (int i = 0; i < results.size(); i++) {
            Internship in = results.get(i);
            System.out.printf("[%d] %s — %s\n", (i + 1), in.getCompanyName(), in.getRole());
            System.out.printf("    Location: %s  |  Mode: %s  |  Duration: %s\n", in.getLocation(), in.getWorkMode(), in.getDuration());
            System.out.printf("    Stipend: %s  |  Eligibility: %s\n", in.getStipend(), in.getEligibility());
            System.out.printf("    Required Skills: %s\n", String.join(", ", in.getRequiredSkills()));
            System.out.printf("    Description: %s\n", in.getDescription());
            System.out.println("--------------------------------------------------------------------------------");
        }
    }
}
