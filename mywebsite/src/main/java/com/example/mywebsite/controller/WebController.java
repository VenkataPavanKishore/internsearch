package com.example.mywebsite.controller;

import com.example.mywebsite.model.Internship;
import com.example.mywebsite.model.MatchResult;
import com.example.mywebsite.service.BookmarkService;
import com.example.mywebsite.service.InternshipService;
import com.example.mywebsite.service.MatchingService;
import com.example.mywebsite.service.ResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebController {

    private final InternshipService internshipService;
    private final ResumeService resumeService;
    private final MatchingService matchingService;
    private final BookmarkService bookmarkService;

    public WebController(InternshipService internshipService,
                         ResumeService resumeService,
                         MatchingService matchingService,
                         BookmarkService bookmarkService) {
        this.internshipService = internshipService;
        this.resumeService = resumeService;
        this.matchingService = matchingService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Internship> all = internshipService.getAllInternships();
        List<MatchResult> ranked = matchingService.rankInternships(resumeService.getStudent(), all);

        double avgScore = 0.0;
        if (!ranked.isEmpty() && resumeService.hasSkills()) {
            avgScore = ranked.stream().mapToDouble(MatchResult::getMatchPercentage).average().orElse(0.0);
            avgScore = Math.round(avgScore * 10.0) / 10.0;
        }

        model.addAttribute("totalInternships", all.size());
        model.addAttribute("skillsCount", resumeService.getStudent().getSkillCount());
        model.addAttribute("skillsList", resumeService.getSkills());
        model.addAttribute("savedCount", bookmarkService.getSavedCount());
        model.addAttribute("avgMatchScore", avgScore);
        model.addAttribute("topRecommendations", ranked.stream().limit(6).collect(Collectors.toList()));
        model.addAttribute("hasSkills", resumeService.hasSkills());
        model.addAttribute("savedIds", bookmarkService.getSavedIds());

        return "index";
    }

    @GetMapping("/internships")
    public String internships(@RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "mode", required = false) String mode,
                              @RequestParam(value = "location", required = false) String location,
                              @RequestParam(value = "minMatch", required = false, defaultValue = "0") int minMatch,
                              Model model) {
        List<Internship> filtered = internshipService.filterInternships(search, mode, location);
        List<MatchResult> ranked = matchingService.rankInternships(resumeService.getStudent(), filtered);

        if (minMatch > 0) {
            ranked = ranked.stream()
                    .filter(m -> m.getMatchPercentage() >= minMatch)
                    .collect(Collectors.toList());
        }

        // Build return URL for bookmarks to return cleanly
        StringBuilder currentUrl = new StringBuilder("/internships?");
        if (search != null && !search.trim().isEmpty()) {
            currentUrl.append("search=").append(URLEncoder.encode(search.trim(), StandardCharsets.UTF_8)).append("&");
        }
        if (mode != null && !mode.trim().isEmpty()) {
            currentUrl.append("mode=").append(URLEncoder.encode(mode.trim(), StandardCharsets.UTF_8)).append("&");
        }
        if (location != null && !location.trim().isEmpty()) {
            currentUrl.append("location=").append(URLEncoder.encode(location.trim(), StandardCharsets.UTF_8)).append("&");
        }
        if (minMatch > 0) {
            currentUrl.append("minMatch=").append(minMatch).append("&");
        }

        model.addAttribute("recommendations", ranked);
        model.addAttribute("totalCount", ranked.size());
        model.addAttribute("hasSkills", resumeService.hasSkills());
        model.addAttribute("studentSkills", resumeService.getSkills());
        model.addAttribute("searchKeyword", search != null ? search : "");
        model.addAttribute("selectedMode", mode != null ? mode : "");
        model.addAttribute("selectedLocation", location != null ? location : "");
        model.addAttribute("selectedMinMatch", minMatch);
        model.addAttribute("currentUrl", currentUrl.toString());
        model.addAttribute("savedIds", bookmarkService.getSavedIds());

        return "internships";
    }

    @GetMapping("/internships/{id}")
    public String internshipDetails(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Internship in = internshipService.getInternshipById(id);
        if (in == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Internship not found with ID: " + id);
            return "redirect:/internships";
        }

        MatchResult match = matchingService.calculateMatch(resumeService.getStudent(), in);
        boolean isBookmarked = bookmarkService.isSaved(id);

        model.addAttribute("internship", in);
        model.addAttribute("match", match);
        model.addAttribute("isBookmarked", isBookmarked);
        model.addAttribute("hasSkills", resumeService.hasSkills());
        model.addAttribute("bookmarkService", bookmarkService);

        return "internship-details";
    }

    @GetMapping("/skills")
    public String skills(Model model) {
        model.addAttribute("skills", resumeService.getSkills());
        model.addAttribute("totalSkills", resumeService.getStudent().getSkillCount());
        return "skills";
    }

    @PostMapping("/skills/add")
    public String addSkills(@RequestParam("skillsInput") String input, RedirectAttributes redirectAttributes) {
        if (input == null || input.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please enter at least one skill.");
            return "redirect:/skills";
        }

        int added = resumeService.addSkills(input);
        if (added > 0) {
            redirectAttributes.addFlashAttribute("successMessage", added + " new skill(s) added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "Entered skill(s) are already in your profile or invalid.");
        }
        return "redirect:/skills";
    }

    @PostMapping("/skills/delete")
    public String deleteSkill(@RequestParam("skill") String skill, RedirectAttributes redirectAttributes) {
        if (resumeService.removeSkill(skill)) {
            redirectAttributes.addFlashAttribute("successMessage", "Skill '" + skill + "' removed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Skill not found.");
        }
        return "redirect:/skills";
    }

    @PostMapping("/skills/edit")
    public String editSkill(@RequestParam("oldSkill") String oldSkill,
                            @RequestParam("newSkill") String newSkill,
                            RedirectAttributes redirectAttributes) {
        if (newSkill == null || newSkill.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "New skill name cannot be empty.");
            return "redirect:/skills";
        }

        if (resumeService.editSkill(oldSkill, newSkill)) {
            redirectAttributes.addFlashAttribute("successMessage", "Skill updated to '" + newSkill + "' successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not rename skill. A skill with that name may already exist.");
        }
        return "redirect:/skills";
    }

    @GetMapping("/companies")
    public String companies(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Internship> results = internshipService.searchByCompany(query);

        String currentUrl = "/companies";
        if (query != null && !query.trim().isEmpty()) {
            currentUrl += "?query=" + URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
        }

        model.addAttribute("companies", results);
        model.addAttribute("searchQuery", query != null ? query : "");
        model.addAttribute("totalFound", results.size());
        model.addAttribute("hasSkills", resumeService.hasSkills());
        model.addAttribute("studentSkills", resumeService.getSkills());
        model.addAttribute("matchingService", matchingService);
        model.addAttribute("student", resumeService.getStudent());
        model.addAttribute("currentUrl", currentUrl);
        model.addAttribute("savedIds", bookmarkService.getSavedIds());

        return "companies";
    }

    @GetMapping("/saved")
    public String savedInternships(Model model) {
        model.addAttribute("savedEntries", bookmarkService.getSavedList());
        model.addAttribute("totalSaved", bookmarkService.getSavedCount());
        return "saved";
    }

    @PostMapping("/saved/add")
    public String bookmarkInternship(@RequestParam("id") String id,
                                     @RequestParam(value = "redirectUrl", defaultValue = "/internships") String redirectUrl,
                                     RedirectAttributes redirectAttributes) {
        Internship in = internshipService.getInternshipById(id);
        if (in != null) {
            MatchResult mr = matchingService.calculateMatch(resumeService.getStudent(), in);
            if (bookmarkService.saveInternship(in, mr.getMatchPercentage())) {
                redirectAttributes.addFlashAttribute("successMessage", "Saved '" + in.getCompanyName() + " - " + in.getRole() + "' to bookmarks!");
            } else {
                redirectAttributes.addFlashAttribute("infoMessage", "'" + in.getCompanyName() + " - " + in.getRole() + "' is already in your saved bookmarks.");
            }
        }
        return "redirect:" + (redirectUrl.isEmpty() ? "/internships" : redirectUrl);
    }

    @PostMapping("/saved/remove")
    public String removeBookmark(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        if (bookmarkService.removeSaved(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Internship removed from bookmarks.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Bookmark not found.");
        }
        return "redirect:/saved";
    }
}
