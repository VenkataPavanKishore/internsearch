import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object encapsulating the comparison outcome between a student's
 * resume skills and a specific internship's requirements.
 */
public class MatchResult implements Comparable<MatchResult> {
    private final Internship internship;
    private final List<String> matchedSkills;
    private final List<String> missingSkills;
    private final double matchPercentage;

    public MatchResult(Internship internship, List<String> matchedSkills, List<String> missingSkills, double matchPercentage) {
        this.internship = internship;
        this.matchedSkills = matchedSkills != null ? matchedSkills : new ArrayList<>();
        this.missingSkills = missingSkills != null ? missingSkills : new ArrayList<>();
        this.matchPercentage = matchPercentage;
    }

    public Internship getInternship() {
        return internship;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    @Override
    public int compareTo(MatchResult other) {
        // Primary sort: descending match percentage
        int cmp = Double.compare(other.matchPercentage, this.matchPercentage);
        if (cmp != 0) {
            return cmp;
        }
        // Secondary sort: alphabetical company name
        return this.internship.getCompanyName().compareToIgnoreCase(other.internship.getCompanyName());
    }
}
