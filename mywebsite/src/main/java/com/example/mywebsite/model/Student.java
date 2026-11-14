package com.example.mywebsite.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Model class representing the student profile in the web application.
 */
public class Student {
    private String name;
    private final Set<String> skills;

    public Student() {
        this.name = "Candidate";
        this.skills = new LinkedHashSet<>();
    }

    public Student(String name) {
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : "Candidate";
        this.skills = new LinkedHashSet<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getSkills() {
        return new ArrayList<>(skills);
    }

    public boolean containsSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        String normalized = skill.trim().toLowerCase();
        for (String s : skills) {
            if (s.equalsIgnoreCase(normalized)) {
                return true;
            }
        }
        return false;
    }

    public boolean addSkill(String skill) {
        if (skill == null) return false;
        String cleanSkill = skill.trim();
        if (cleanSkill.isEmpty() || containsSkill(cleanSkill)) {
            return false;
        }
        return skills.add(cleanSkill);
    }

    public int addSkills(Collection<String> newSkills) {
        if (newSkills == null) return 0;
        int count = 0;
        for (String s : newSkills) {
            if (addSkill(s)) {
                count++;
            }
        }
        return count;
    }

    public boolean removeSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return false;
        String target = skill.trim();
        for (String s : skills) {
            if (s.equalsIgnoreCase(target)) {
                return skills.remove(s);
            }
        }
        return false;
    }

    public boolean editSkill(String oldSkill, String newSkill) {
        if (oldSkill == null || newSkill == null) return false;
        String cleanOld = oldSkill.trim();
        String cleanNew = newSkill.trim();

        if (cleanNew.isEmpty() || !containsSkill(cleanOld)) {
            return false;
        }

        if (!cleanOld.equalsIgnoreCase(cleanNew) && containsSkill(cleanNew)) {
            return false;
        }

        List<String> list = new ArrayList<>(skills);
        skills.clear();
        for (String item : list) {
            if (item.equalsIgnoreCase(cleanOld)) {
                skills.add(cleanNew);
            } else {
                skills.add(item);
            }
        }
        return true;
    }

    public void clearSkills() {
        skills.clear();
    }

    public void setSkills(Collection<String> newSkills) {
        skills.clear();
        if (newSkills != null) {
            for (String s : newSkills) {
                addSkill(s);
            }
        }
    }

    public int getSkillCount() {
        return skills.size();
    }
}
