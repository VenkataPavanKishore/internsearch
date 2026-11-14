import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Manager class responsible for maintaining the internship dataset,
 * loading records from disk, and seeding initial sample opportunities if absent.
 */
public class InternshipManager {
    private final String datasetFilePath;
    private final List<Internship> internshipList;

    public InternshipManager(String datasetFilePath) {
        this.datasetFilePath = datasetFilePath;
        this.internshipList = new ArrayList<>();
        initializeDataset();
    }

    /**
     * Loads internship opportunities from disk. If the file is missing or empty,
     * seeds standard sample internship records.
     */
    public void initializeDataset() {
        internshipList.clear();
        File file = new File(datasetFilePath);
        if (!file.exists() || file.length() == 0) {
            System.out.println("[System Notice] Dataset file missing or empty. Initializing sample internship records...");
            List<Internship> defaults = buildSampleInternships();
            FileManager.saveInternships(datasetFilePath, defaults);
            internshipList.addAll(defaults);
        } else {
            List<Internship> loaded = FileManager.loadInternships(datasetFilePath);
            if (loaded.isEmpty()) {
                List<Internship> defaults = buildSampleInternships();
                FileManager.saveInternships(datasetFilePath, defaults);
                internshipList.addAll(defaults);
            } else {
                internshipList.addAll(loaded);
            }
        }
    }

    public List<Internship> getAllInternships() {
        return Collections.unmodifiableList(internshipList);
    }

    public Internship findById(String id) {
        if (id == null) return null;
        for (Internship in : internshipList) {
            if (in.getId().equalsIgnoreCase(id.trim())) {
                return in;
            }
        }
        return null;
    }

    public int getCount() {
        return internshipList.size();
    }

    /**
     * Builds a comprehensive set of curated sample internship postings.
     * Note: In compliance with project guidelines, these are clearly identified educational sample opportunities.
     */
    public static List<Internship> buildSampleInternships() {
        List<Internship> samples = new ArrayList<>();

        samples.add(new Internship(
                "INT-101",
                "Google",
                "Software Engineering Intern (Summer 2027)",
                Arrays.asList("Java", "C++", "Data Structures", "Algorithms", "Git"),
                "[Sample Opportunity] Work on core backend systems and distributed services serving billions of global requests.",
                "Bengaluru, Karnataka",
                "3 Months",
                "Rs. 1,10,000 / month",
                "Hybrid",
                "Pre-final / Final Year B.Tech, B.E, or M.Tech in CS/IT",
                "2026-12-30",
                "https://careers.google.com/jobs/sample-swe-intern"
        ));

        samples.add(new Internship(
                "INT-102",
                "Microsoft",
                "Full Stack Developer Intern",
                Arrays.asList("Java", "Spring Boot", "React", "TypeScript", "SQL", "Git"),
                "[Sample Opportunity] Build enterprise cloud collaboration features for Microsoft 365 and Azure portal.",
                "Hyderabad, Telangana",
                "6 Months",
                "₹85,000 / month",
                "Hybrid",
                "B.Tech / B.E / MCA with minimum 7.5 CGPA",
                "2027-01-15",
                "https://careers.microsoft.com/sample-fullstack-intern"
        ));

        samples.add(new Internship(
                "INT-103",
                "Amazon",
                "Cloud & DevOps Intern",
                Arrays.asList("Python", "Linux", "AWS", "Docker", "Git", "Bash"),
                "[Sample Opportunity] Support AWS cloud infrastructure automation, CI/CD pipelines, and server reliability.",
                "Bengaluru, Karnataka",
                "6 Months",
                "₹80,000 / month",
                "On-site",
                "3rd or 4th Year Engineering Students",
                "2026-12-20",
                "https://amazon.jobs/en/sample-devops-intern"
        ));

        samples.add(new Internship(
                "INT-104",
                "Razorpay",
                "Backend Engineering Intern",
                Arrays.asList("Java", "Spring Boot", "MySQL", "Redis", "REST APIs", "Git"),
                "[Sample Opportunity] Design low-latency, high-concurrency payment gateway routing and financial ledger microservices.",
                "Bengaluru, Karnataka",
                "6 Months",
                "₹50,000 / month",
                "Hybrid",
                "Graduating batches of 2026-2027",
                "2027-01-10",
                "https://razorpay.com/jobs/sample-backend-intern"
        ));

        samples.add(new Internship(
                "INT-105",
                "Adobe",
                "Frontend Development Intern",
                Arrays.asList("JavaScript", "TypeScript", "React", "HTML", "CSS", "Git"),
                "[Sample Opportunity] Develop fluid, interactive UI components for Adobe Creative Cloud web applications.",
                "Noida, Uttar Pradesh",
                "3 Months",
                "₹70,000 / month",
                "Remote",
                "B.Tech / BCA / MCA students passionate about UI/UX and web tech",
                "2026-12-28",
                "https://adobe.wd5.myworkdayjobs.com/sample-frontend-intern"
        ));

        samples.add(new Internship(
                "INT-106",
                "Swiggy",
                "Data Science & Analytics Intern",
                Arrays.asList("Python", "SQL", "Pandas", "Machine Learning", "Tableau"),
                "[Sample Opportunity] Analyze customer order patterns, delivery routing algorithms, and promotional performance.",
                "Bengaluru, Karnataka",
                "4 Months",
                "₹45,000 / month",
                "Hybrid",
                "Degree in Computer Science, Statistics, Mathematics, or Data Science",
                "2027-02-01",
                "https://careers.swiggy.com/sample-data-intern"
        ));

        samples.add(new Internship(
                "INT-107",
                "Tata Consultancy Services",
                "Java Graduate Trainee Intern",
                Arrays.asList("Java", "SQL", "OOP", "HTML", "CSS"),
                "[Sample Opportunity] Participate in enterprise digital transformation projects and software verification.",
                "Pune, Maharashtra",
                "6 Months",
                "₹22,000 / month",
                "On-site",
                "All B.E / B.Tech / BCA / MCA graduates with 60%+ throughout academics",
                "2027-03-15",
                "https://www.tcs.com/careers/sample-java-intern"
        ));

        samples.add(new Internship(
                "INT-108",
                "Infosys",
                "Systems Engineer Intern",
                Arrays.asList("Python", "Java", "SQL", "Data Structures"),
                "[Sample Opportunity] Learn and build automated enterprise software modules on Infosys Cobalt Cloud platform.",
                "Mysuru, Karnataka",
                "4 Months",
                "₹25,000 / month",
                "On-site",
                "Final Year B.Tech / MCA Students",
                "2027-02-28",
                "https://career.infosys.com/sample-sys-intern"
        ));

        samples.add(new Internship(
                "INT-109",
                "Cisco",
                "Network & Cybersecurity Intern",
                Arrays.asList("Python", "Linux", "Computer Networks", "Cybersecurity", "Wireshark"),
                "[Sample Opportunity] Analyze enterprise network packets, security vulnerabilities, and threat intelligence feeds.",
                "Bengaluru, Karnataka",
                "6 Months",
                "₹65,000 / month",
                "Hybrid",
                "B.Tech in CS/IT/ECE with strong computer networking fundamentals",
                "2027-01-20",
                "https://jobs.cisco.com/sample-cyber-intern"
        ));

        samples.add(new Internship(
                "INT-110",
                "Uber",
                "Mobile App Development Intern (Android)",
                Arrays.asList("Kotlin", "Java", "Android SDK", "Git", "REST APIs"),
                "[Sample Opportunity] Optimize driver and rider mobile interfaces with real-time GPS tracking and offline state syncing.",
                "Hyderabad, Telangana",
                "3 Months",
                "₹90,000 / month",
                "Hybrid",
                "Students proficient in mobile application development (Android/iOS)",
                "2027-01-05",
                "https://uber.com/careers/sample-android-intern"
        ));

        samples.add(new Internship(
                "INT-111",
                "Oracle",
                "Database & Cloud Solutions Intern",
                Arrays.asList("SQL", "Java", "Oracle DB", "Linux", "Cloud Computing"),
                "[Sample Opportunity] Work with Autonomous Database engineering teams on query optimization and cloud storage elasticity.",
                "Bengaluru, Karnataka",
                "6 Months",
                "₹60,000 / month",
                "Hybrid",
                "B.Tech / M.Tech in Computer Science or Information Technology",
                "2027-02-15",
                "https://oracle.com/corporate/careers/sample-db-intern"
        ));

        samples.add(new Internship(
                "INT-112",
                "Flipkart",
                "Backend SDE Intern",
                Arrays.asList("Java", "Data Structures", "Algorithms", "Kafka", "MySQL"),
                "[Sample Opportunity] Scale order processing and inventory streaming pipelines ahead of Big Billion Days.",
                "Bengaluru, Karnataka",
                "6 Months",
                "₹65,000 / month",
                "Hybrid",
                "Passionate problem solvers from all engineering disciplines",
                "2027-01-30",
                "https://flipkartcareers.com/sample-sde-intern"
        ));

        return samples;
    }
}
