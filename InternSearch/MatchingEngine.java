import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MatchingEngine responsible for skill comparison, mathematical percentage computation,
 * and ranking internship opportunities based on candidate skill alignment.
 */
public class MatchingEngine {

    /**
     * Compares a student's skills with a single internship's requirements.
     * Computes matched skills, missing skills, and match percentage.
     *
     * Match Percentage = (Matched Skills / Total Required Skills) * 100
     */
    public static MatchResult calculateMatch(Student student, Internship internship) {
        if (internship == null) {
            return new MatchResult(null, new ArrayList<>(), new ArrayList<>(), 0.0);
        }

        List<String> studentSkills = (student != null) ? student.getSkills() : new ArrayList<>();
        List<String> requiredSkills = internship.getRequiredSkills();

        // Create a lookup set of normalized (lowercase, trimmed) student skills
        Set<String> normalizedStudentSkills = new HashSet<>();
        for (String s : studentSkills) {
            if (s != null && !s.trim().isEmpty()) {
                normalizedStudentSkills.add(s.trim().toLowerCase());
            }
        }

        List<String> matchedList = new ArrayList<>();
        List<String> missingList = new ArrayList<>();

        for (String req : requiredSkills) {
            if (req == null || req.trim().isEmpty()) continue;
            String cleanReq = req.trim();
            if (normalizedStudentSkills.contains(cleanReq.toLowerCase())) {
                matchedList.add(cleanReq);
            } else {
                missingList.add(cleanReq);
            }
        }

        int totalRequired = requiredSkills.size();
        double percentage = 0.0;
        if (totalRequired > 0) {
            percentage = ((double) matchedList.size() / totalRequired) * 100.0;
            // Round to 1 decimal place
            percentage = Math.round(percentage * 10.0) / 10.0;
        }

        return new MatchResult(internship, matchedList, missingList, percentage);
    }

    /**
     * Evaluates a collection of internships against the student profile,
     * sorts them in descending order of match percentage, and returns the ranked list.
     */
    public static List<MatchResult> rankInternships(Student student, List<Internship> internships) {
        List<MatchResult> results = new ArrayList<>();
        if (internships == null || internships.isEmpty()) {
            return results;
        }

        for (Internship in : internships) {
            results.add(calculateMatch(student, in));
        }

        // Sort descending by match percentage (natural order implemented in MatchResult)
        Collections.sort(results);
        return results;
    }
}
