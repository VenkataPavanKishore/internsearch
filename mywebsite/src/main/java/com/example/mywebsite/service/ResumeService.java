package com.example.mywebsite.service;

import com.example.mywebsite.model.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeService {
    private static final String RESUME_FILE = "resume.txt";

    private final FileStorageService fileStorageService;
    private final Student student;

    public ResumeService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.student = new Student("Candidate");
    }

    @PostConstruct
    public void init() {
        loadSkills();
    }

    public synchronized void loadSkills() {
        List<String> lines = fileStorageService.readLines(RESUME_FILE);
        List<String> parsed = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = line.split(",");
            for (String t : tokens) {
                String clean = t.trim();
                if (!clean.isEmpty()) {
                    parsed.add(clean);
                }
            }
        }
        student.setSkills(parsed);
    }

    public synchronized boolean saveSkills() {
        List<String> lines = new ArrayList<>();
        if (!student.getSkills().isEmpty()) {
            lines.add(String.join(", ", student.getSkills()));
        }
        return fileStorageService.writeLines(RESUME_FILE, lines);
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

    public synchronized int addSkills(String rawInput) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return 0;
        }

        String[] tokens = rawInput.split(",");
        int added = 0;
        for (String t : tokens) {
            String s = t.trim();
            if (!s.isEmpty() && student.addSkill(s)) {
                added++;
            }
        }

        if (added > 0) {
            saveSkills();
        }
        return added;
    }

    public synchronized boolean removeSkill(String skillName) {
        if (student.removeSkill(skillName)) {
            saveSkills();
            return true;
        }
        return false;
    }

    public synchronized boolean editSkill(String oldSkill, String newSkill) {
        if (student.editSkill(oldSkill, newSkill)) {
            saveSkills();
            return true;
        }
        return false;
    }

    public synchronized void clearSkills() {
        student.clearSkills();
        saveSkills();
    }
}
