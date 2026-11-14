# 🎓 InternSearch — Smart Internship Discovery & Skill Matching System

A complete internship discovery platform built in **Java** for college students and freshers.
Match your resume skills to internships, see missing skills, and explore companies — all without leaving your terminal or browser.

---

## 📁 Project Structure

```
internsearch/
├── InternSearch/          ← Console CLI App (Core Java, no frameworks)
│   ├── InternSearch.java
│   ├── Internship.java
│   ├── Student.java
│   ├── SkillManager.java
│   ├── InternshipManager.java
│   ├── MatchingEngine.java
│   ├── CompanySearch.java
│   ├── ApplicationManager.java
│   ├── FileManager.java
│   ├── MatchResult.java
│   ├── internships.txt       ← Internship dataset
│   ├── resume.txt            ← Your saved skills
│   ├── saved_internships.txt ← Your bookmarked internships
│   └── README.md             ← Detailed CLI docs & viva Q&A
│
├── mywebsite/             ← Spring Boot Web App (localhost:8080)
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/mywebsite/
│   │       │   ├── controller/   ← WebController, ApiController
│   │       │   ├── model/        ← Internship, Student, MatchResult, SavedEntry
│   │       │   └── service/      ← InternshipService, MatchingService, BookmarkService…
│   │       └── resources/
│   │           ├── templates/    ← Thymeleaf HTML pages
│   │           ├── static/       ← CSS & JS
│   │           └── data/         ← internships.txt, resume.txt, saved_internships.txt
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   └── README.md
│
├── run-console.bat        ← One-click launcher for the CLI app
├── run-website.bat        ← One-click launcher for the web app
├── push-to-github.bat     ← Git push helper
├── README.md              ← This file
└── .gitignore
```

---

## 🚀 Module 1 — Console CLI App

**Tech:** Core Java 17+, File I/O, OOP

```bash
cd InternSearch
javac *.java
java InternSearch
```

### Features
| # | Feature |
|---|---------|
| 1 | Enter Resume Skills |
| 2 | Find Internships (ranked by match %) |
| 3 | Update Resume Skills (Add / Delete / Edit) |
| 4 | View Saved Internships |
| 5 | Search Internship by Company Name |
| 6 | Exit |

---

## 🌐 Module 2 — Spring Boot Web App

**Tech:** Spring Boot 3, Thymeleaf, Bootstrap 5, Java 21

```bash
cd mywebsite
.\mvnw.cmd spring-boot:run
# Open browser → http://localhost:8080
```

### Pages
| URL | Page |
|-----|------|
| `/` | Dashboard — stats, top 6 matches |
| `/internships` | Discover & filter all internships |
| `/internships/{id}` | Full details + Apply link |
| `/skills` | Manage your resume skills |
| `/companies` | Search companies by name |
| `/saved` | View & manage bookmarked internships |

---

## ⚙️ Matching Algorithm

```
Match % = (Number of Your Skills that Appear in Required Skills) / (Total Required Skills) × 100
```

- Case-insensitive comparison
- Duplicates prevented in both skills and bookmarks
- Data persisted in `.txt` files between sessions

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 / 21 |
| Web Framework | Spring Boot 3 + Thymeleaf |
| UI | Bootstrap 5.3, Bootstrap Icons |
| Storage | Plain text files (no database) |
| Build | Maven (via mvnw wrapper) |
| IDE | Visual Studio Code |

---

## 📌 Sample Data

The app ships with **15 sample internship opportunities** across companies like:
Google · Microsoft · Amazon · Flipkart · Infosys · Wipro · TCS · Razorpay · Adobe · JPMorgan Chase · Swiggy · Zomato · ISRO · PhonePe · Atlassian

> **Note:** All entries are marked `[Sample Opportunity]` and are for educational demonstration only.

---

## 👨‍💻 Author

**Venkata Pavan Kishore**
GitHub: [@VenkataPavanKishore](https://github.com/VenkataPavanKishore)
