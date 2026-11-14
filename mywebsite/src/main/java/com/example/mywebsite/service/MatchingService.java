package com.example.mywebsite.service;

import com.example.mywebsite.model.Internship;
import com.example.mywebsite.model.MatchResult;
import com.example.mywebsite.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MatchingService {

    public MatchResult calculateMatch(Student student, Internship internship) {
        if (internship == null) {
            return new MatchResult(null, new ArrayList<>(), new ArrayList<>(), 0.0);
        }

        List<String> studentSkills = (student != null) ? student.getSkills() : new ArrayList<>();
        List<String> requiredSkills = internship.getRequiredSkills();

        Set<String> normalizedStudentSkills = new HashSet<>();
        for (String s : studentSkills) {
            if (s != null && !s.trim().isEmpty()) {
                normalizedStudentSkills.add(s.trim().toLowerCase());
            }
        }

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String req : requiredSkills) {
            if (req == null || req.trim().isEmpty()) continue;
            String cleanReq = req.trim();
            if (normalizedStudentSkills.contains(cleanReq.toLowerCase())) {
                matched.add(cleanReq);
            } else {
                missing.add(cleanReq);
            }
        }

        int total = requiredSkills.size();
        double pct = 0.0;
        if (total > 0) {
            pct = ((double) matched.size() / total) * 100.0;
            pct = Math.round(pct * 10.0) / 10.0;
        }

        return new MatchResult(internship, matched, missing, pct);
    }

    public List<MatchResult> rankInternships(Student student, List<Internship> list) {
        List<MatchResult> results = new ArrayList<>();
        if (list == null) return results;

        for (Internship in : list) {
            results.add(calculateMatch(student, in));
        }

        Collections.sort(results);
        return results;
    }
}
