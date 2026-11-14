package com.example.mywebsite.controller;

import com.example.mywebsite.model.Internship;
import com.example.mywebsite.model.MatchResult;
import com.example.mywebsite.service.BookmarkService;
import com.example.mywebsite.service.InternshipService;
import com.example.mywebsite.service.MatchingService;
import com.example.mywebsite.service.ResumeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final InternshipService internshipService;
    private final ResumeService resumeService;
    private final MatchingService matchingService;
    private final BookmarkService bookmarkService;

    public ApiController(InternshipService internshipService,
                         ResumeService resumeService,
                         MatchingService matchingService,
                         BookmarkService bookmarkService) {
        this.internshipService = internshipService;
        this.resumeService = resumeService;
        this.matchingService = matchingService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/skills")
    public List<String> getSkills() {
        return resumeService.getSkills();
    }

    @GetMapping("/internships")
    public List<MatchResult> getRankedInternships() {
        return matchingService.rankInternships(resumeService.getStudent(), internshipService.getAllInternships());
    }

    @GetMapping("/internships/{id}")
    public Map<String, Object> getInternshipDetails(@PathVariable("id") String id) {
        Map<String, Object> res = new HashMap<>();
        Internship in = internshipService.getInternshipById(id);
        if (in != null) {
            MatchResult mr = matchingService.calculateMatch(resumeService.getStudent(), in);
            res.put("internship", in);
            res.put("match", mr);
            res.put("isSaved", bookmarkService.isSaved(id));
        } else {
            res.put("error", "Internship not found");
        }
        return res;
    }

    @GetMapping("/companies")
    public List<Internship> searchCompanies(@RequestParam(value = "query", required = false) String query) {
        return internshipService.searchByCompany(query);
    }
}
