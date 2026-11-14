package com.example.mywebsite.model;

/**
 * Model class for bookmarked / saved internships in the web application.
 */
public class SavedEntry {
    private Internship internship;
    private double matchPercentage;

    public SavedEntry() {}

    public SavedEntry(Internship internship, double matchPercentage) {
        this.internship = internship;
        this.matchPercentage = matchPercentage;
    }

    public Internship getInternship() { return internship; }
    public void setInternship(Internship internship) { this.internship = internship; }

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }

    public String getBadgeClass() {
        if (matchPercentage >= 75.0) return "badge-success";
        if (matchPercentage >= 50.0) return "badge-primary";
        if (matchPercentage >= 25.0) return "badge-warning";
        return "badge-secondary";
    }
}
