package com.example.mywebsite.service;

import com.example.mywebsite.model.Internship;
import com.example.mywebsite.model.SavedEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookmarkService {
    private static final String SAVED_FILE = "saved_internships.txt";

    private final FileStorageService fileStorageService;
    private final InternshipService internshipService;
    private final List<SavedEntry> savedList = new ArrayList<>();

    public BookmarkService(FileStorageService fileStorageService, InternshipService internshipService) {
        this.fileStorageService = fileStorageService;
        this.internshipService = internshipService;
    }

    @PostConstruct
    public void init() {
        loadSaved();
    }

    public synchronized void loadSaved() {
        savedList.clear();
        List<String> lines = fileStorageService.readLines(SAVED_FILE);
        for (String line : lines) {
            int lastPipe = line.lastIndexOf('|');
            double pct = 0.0;
            String rec = line;
            if (lastPipe != -1) {
                try {
                    pct = Double.parseDouble(line.substring(lastPipe + 1).trim());
                    rec = line.substring(0, lastPipe);
                } catch (NumberFormatException ignored) {}
            }
            Internship in = Internship.fromFileRecord(rec);
            if (in != null && in.getId() != null) {
                savedList.add(new SavedEntry(in, pct));
            }
        }
    }

    public synchronized boolean persist() {
        List<String> lines = new ArrayList<>();
        for (SavedEntry entry : savedList) {
            lines.add(entry.getInternship().toFileRecord() + "|" + entry.getMatchPercentage());
        }
        return fileStorageService.writeLines(SAVED_FILE, lines);
    }

    public List<SavedEntry> getSavedList() {
        return Collections.unmodifiableList(savedList);
    }

    public int getSavedCount() {
        return savedList.size();
    }

    public boolean isSaved(String id) {
        if (id == null) return false;
        return savedList.stream().anyMatch(e -> e.getInternship().getId().equalsIgnoreCase(id.trim()));
    }

    public java.util.Set<String> getSavedIds() {
        return savedList.stream()
                .map(e -> e.getInternship().getId().toLowerCase())
                .collect(java.util.stream.Collectors.toSet());
    }

    public synchronized boolean saveInternship(Internship internship, double matchPercentage) {
        if (internship == null || isSaved(internship.getId())) {
            return false;
        }
        savedList.add(new SavedEntry(internship, matchPercentage));
        persist();
        return true;
    }

    public synchronized boolean removeSaved(String id) {
        if (id == null) return false;
        boolean removed = savedList.removeIf(e -> e.getInternship().getId().equalsIgnoreCase(id.trim()));
        if (removed) {
            persist();
        }
        return removed;
    }
}
