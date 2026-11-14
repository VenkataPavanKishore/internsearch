package com.example.mywebsite.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object encapsulating the comparison outcome for web views.
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

    public Internship getInternship() { return internship; }
    public List<String> getMatchedSkills() { return matchedSkills; }
    public List<String> getMissingSkills() { return missingSkills; }
    public double getMatchPercentage() { return matchPercentage; }

    public String getBadgeClass() {
        if (matchPercentage >= 75.0) return "badge-success";
        if (matchPercentage >= 50.0) return "badge-primary";
        if (matchPercentage >= 25.0) return "badge-warning";
        return "badge-secondary";
    }

    public String getProgressColor() {
        if (matchPercentage >= 75.0) return "#10b981"; // Emerald green
        if (matchPercentage >= 50.0) return "#3b82f6"; // Royal blue
        if (matchPercentage >= 25.0) return "#f59e0b"; // Amber
        return "#94a3b8"; // Slate
    }

    @Override
    public int compareTo(MatchResult other) {
        int cmp = Double.compare(other.matchPercentage, this.matchPercentage);
        if (cmp != 0) {
            return cmp;
        }
        return this.internship.getCompanyName().compareToIgnoreCase(other.internship.getCompanyName());
    }
}
