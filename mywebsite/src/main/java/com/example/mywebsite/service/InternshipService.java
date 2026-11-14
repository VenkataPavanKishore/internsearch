package com.example.mywebsite.service;

import com.example.mywebsite.model.Internship;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternshipService {
    private static final String INTERNSHIPS_FILE = "internships.txt";

    private final FileStorageService fileStorageService;
    private final List<Internship> internships = new ArrayList<>();

    public InternshipService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostConstruct
    public void init() {
        loadInternships();
    }

    public synchronized void loadInternships() {
        internships.clear();
        List<String> lines = fileStorageService.readLines(INTERNSHIPS_FILE);
        for (String line : lines) {
            Internship in = Internship.fromFileRecord(line);
            if (in != null && in.getId() != null && !in.getId().isEmpty()) {
                // Prevent duplicate IDs from corrupted files
                if (internships.stream().noneMatch(existing -> existing.getId().equalsIgnoreCase(in.getId()))) {
                    internships.add(in);
                }
            }
        }

        if (internships.isEmpty()) {
            List<Internship> samples = buildSampleInternships();
            internships.addAll(samples);
            List<String> records = samples.stream().map(Internship::toFileRecord).collect(Collectors.toList());
            fileStorageService.writeLines(INTERNSHIPS_FILE, records);
        }
    }

    public List<Internship> getAllInternships() {
        return Collections.unmodifiableList(internships);
    }

    public Internship getInternshipById(String id) {
        if (id == null) return null;
        return internships.stream()
                .filter(i -> i.getId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<Internship> searchByCompany(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllInternships();
        }
        String q = query.trim().toLowerCase();
        return internships.stream()
                .filter(i -> i.getCompanyName().toLowerCase().contains(q)
                        || i.getRole().toLowerCase().contains(q)
                        || i.getRequiredSkills().stream().anyMatch(s -> s.toLowerCase().contains(q)))
                .collect(Collectors.toList());
    }

    public List<Internship> filterInternships(String search, String workMode, String location) {
        return internships.stream()
                .filter(i -> {
                    if (search == null || search.trim().isEmpty()) return true;
                    String q = search.trim().toLowerCase();
                    return i.getCompanyName().toLowerCase().contains(q)
                            || i.getRole().toLowerCase().contains(q)
                            || i.getDescription().toLowerCase().contains(q)
                            || i.getRequiredSkills().stream().anyMatch(s -> s.toLowerCase().contains(q));
                })
                .filter(i -> workMode == null || workMode.isEmpty() || i.getWorkMode().equalsIgnoreCase(workMode))
                .filter(i -> location == null || location.isEmpty() || i.getLocation().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Internship> buildSampleInternships() {
        List<Internship> samples = new ArrayList<>();
        samples.add(new Internship("INT-101", "Google", "Software Engineering Intern (Summer 2027)",
                Arrays.asList("Java", "C++", "Data Structures", "Algorithms", "Git"),
                "[Sample Opportunity] Work on core backend systems and distributed services serving billions of global requests.",
                "Bengaluru, Karnataka", "3 Months", "Rs. 1,10,000 / month", "Hybrid",
                "Pre-final / Final Year B.Tech, B.E, or M.Tech in CS/IT", "2026-12-30", "https://careers.google.com/jobs/sample-swe-intern"));

        samples.add(new Internship("INT-102", "Microsoft", "Full Stack Developer Intern",
                Arrays.asList("Java", "Spring Boot", "React", "TypeScript", "SQL", "Git"),
                "[Sample Opportunity] Build enterprise cloud collaboration features for Microsoft 365 and Azure portal.",
                "Hyderabad, Telangana", "6 Months", "Rs. 85,000 / month", "Hybrid",
                "B.Tech / B.E / MCA with minimum 7.5 CGPA", "2027-01-15", "https://careers.microsoft.com/sample-fullstack-intern"));

        samples.add(new Internship("INT-103", "Amazon", "Cloud & DevOps Intern",
                Arrays.asList("Python", "Linux", "AWS", "Docker", "Git", "Bash"),
                "[Sample Opportunity] Support AWS cloud infrastructure automation, CI/CD pipelines, and server reliability.",
                "Bengaluru, Karnataka", "6 Months", "Rs. 80,000 / month", "On-site",
                "3rd or 4th Year Engineering Students", "2026-12-20", "https://amazon.jobs/en/sample-devops-intern"));

        samples.add(new Internship("INT-104", "Razorpay", "Backend Engineering Intern",
                Arrays.asList("Java", "Spring Boot", "MySQL", "Redis", "REST APIs", "Git"),
                "[Sample Opportunity] Design low-latency, high-concurrency payment gateway routing and financial ledger microservices.",
                "Bengaluru, Karnataka", "6 Months", "Rs. 50,000 / month", "Hybrid",
                "Graduating batches of 2026-2027", "2027-01-10", "https://razorpay.com/jobs/sample-backend-intern"));

        samples.add(new Internship("INT-105", "Adobe", "Frontend Development Intern",
                Arrays.asList("JavaScript", "TypeScript", "React", "HTML", "CSS", "Git"),
                "[Sample Opportunity] Develop fluid, interactive UI components for Adobe Creative Cloud web applications.",
                "Noida, Uttar Pradesh", "3 Months", "Rs. 70,000 / month", "Remote",
                "B.Tech / BCA / MCA students passionate about UI/UX and web tech", "2026-12-28", "https://adobe.wd5.myworkdayjobs.com/sample-frontend-intern"));

        samples.add(new Internship("INT-106", "Swiggy", "Data Science & Analytics Intern",
                Arrays.asList("Python", "SQL", "Pandas", "Machine Learning", "Tableau"),
                "[Sample Opportunity] Analyze customer order patterns, delivery routing algorithms, and promotional performance.",
                "Bengaluru, Karnataka", "4 Months", "Rs. 45,000 / month", "Hybrid",
                "Degree in Computer Science, Statistics, Mathematics, or Data Science", "2027-02-01", "https://careers.swiggy.com/sample-data-intern"));

        samples.add(new Internship("INT-107", "Tata Consultancy Services", "Java Graduate Trainee Intern",
                Arrays.asList("Java", "SQL", "OOP", "HTML", "CSS"),
                "[Sample Opportunity] Participate in enterprise digital transformation projects and software verification.",
                "Pune, Maharashtra", "6 Months", "Rs. 22,000 / month", "On-site",
                "All B.E / B.Tech / BCA / MCA graduates with 60%+ throughout academics", "2027-03-15", "https://www.tcs.com/careers/sample-java-intern"));

        samples.add(new Internship("INT-108", "Infosys", "Systems Engineer Intern",
                Arrays.asList("Python", "Java", "SQL", "Data Structures"),
                "[Sample Opportunity] Learn and build automated enterprise software modules on Infosys Cobalt Cloud platform.",
                "Mysuru, Karnataka", "4 Months", "Rs. 25,000 / month", "On-site",
                "Final Year B.Tech / MCA Students", "2027-02-28", "https://career.infosys.com/sample-sys-intern"));

        samples.add(new Internship("INT-109", "Cisco", "Network & Cybersecurity Intern",
                Arrays.asList("Python", "Linux", "Computer Networks", "Cybersecurity", "Wireshark"),
                "[Sample Opportunity] Analyze enterprise network packets, security vulnerabilities, and threat intelligence feeds.",
                "Bengaluru, Karnataka", "6 Months", "Rs. 65,000 / month", "Hybrid",
                "B.Tech in CS/IT/ECE with strong computer networking fundamentals", "2027-01-20", "https://jobs.cisco.com/sample-cyber-intern"));

        samples.add(new Internship("INT-110", "Uber", "Mobile App Development Intern (Android)",
                Arrays.asList("Kotlin", "Java", "Android SDK", "Git", "REST APIs"),
                "[Sample Opportunity] Optimize driver and rider mobile interfaces with real-time GPS tracking and offline state syncing.",
                "Hyderabad, Telangana", "3 Months", "Rs. 90,000 / month", "Hybrid",
                "Students proficient in mobile application development (Android/iOS)", "2027-01-05", "https://uber.com/careers/sample-android-intern"));

        samples.add(new Internship("INT-111", "Oracle", "Database & Cloud Solutions Intern",
                Arrays.asList("SQL", "Java", "Oracle DB", "Linux", "Cloud Computing"),
                "[Sample Opportunity] Work with Autonomous Database engineering teams on query optimization and cloud storage elasticity.",
                "Bengaluru, Karnataka", "6 Months", "Rs. 60,000 / month", "Hybrid",
                "B.Tech / M.Tech in Computer Science or Information Technology", "2027-02-15", "https://oracle.com/corporate/careers/sample-db-intern"));

        samples.add(new Internship("INT-112", "Flipkart", "Backend SDE Intern",
                Arrays.asList("Java", "Data Structures", "Algorithms", "Kafka", "MySQL"),
                "[Sample Opportunity] Scale order processing and inventory streaming pipelines ahead of Big Billion Days.",
                "Bengaluru, Karnataka", "6 Months", "Rs. 65,000 / month", "Hybrid",
                "Passionate problem solvers from all engineering disciplines", "2027-01-30", "https://flipkartcareers.com/sample-sde-intern"));

        samples.add(new Internship("INT-113", "Zomato", "React Native Mobile Intern",
                Arrays.asList("JavaScript", "React Native", "Redux", "REST APIs", "Git"),
                "[Sample Opportunity] Enhance live food tracking, dining reservations, and restaurant partner discovery screens.",
                "Gurugram, Haryana", "3 Months", "Rs. 40,000 / month", "Remote",
                "College students with demonstrable React Native personal projects", "2027-01-18", "https://zomato.com/careers/sample-mobile-intern"));

        samples.add(new Internship("INT-114", "Paytm", "Fintech QA Automation Intern",
                Arrays.asList("Java", "Selenium", "TestNG", "Postman", "Git"),
                "[Sample Opportunity] Build automated end-to-end regression test suites for UPI payments and wallet transactions.",
                "Noida, Uttar Pradesh", "6 Months", "Rs. 30,000 / month", "On-site",
                "Freshers and 2026/2027 graduates in CS/IT/BCA", "2027-02-10", "https://paytm.com/careers/sample-qa-intern"));

        samples.add(new Internship("INT-115", "JPMorgan Chase", "Software Engineer Analyst Intern",
                Arrays.asList("Java", "Spring Boot", "Python", "SQL", "Docker"),
                "[Sample Opportunity] Develop algorithmic financial risk modeling systems and secure cloud transaction APIs.",
                "Mumbai, Maharashtra", "4 Months", "Rs. 75,000 / month", "Hybrid",
                "Students pursuing Bachelor's or Master's in STEM fields", "2027-01-25", "https://jpmorganchase.com/careers/sample-analyst-intern"));

        return samples;
    }
}
