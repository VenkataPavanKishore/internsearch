import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Model class representing the student user profile and their technical resume skills.
 * Uses a LinkedHashSet to guarantee unique skills while preserving the sequence of entry.
 */
public class Student {
    private String name;
    private final Set<String> skills;

    public Student() {
        this.name = "Student User";
        this.skills = new LinkedHashSet<>();
    }

    public Student(String name) {
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : "Student User";
        this.skills = new LinkedHashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    /**
     * Returns an unmodifiable list of all technical skills.
     */
    public List<String> getSkills() {
        return new ArrayList<>(skills);
    }

    /**
     * Checks if a skill is already present (case-insensitive check).
     */
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

    /**
     * Adds a single skill if not already present.
     * @return true if added, false if duplicate or invalid.
     */
    public boolean addSkill(String skill) {
        if (skill == null) return false;
        String cleanSkill = skill.trim();
        if (cleanSkill.isEmpty() || containsSkill(cleanSkill)) {
            return false;
        }
        return skills.add(cleanSkill);
    }

    /**
     * Adds multiple skills from a collection.
     * @return count of newly added skills.
     */
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

    /**
     * Removes a skill by name (case-insensitive).
     * @return true if removed, false if not found.
     */
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

    /**
     * Edits/renames an existing skill.
     * @return true if successfully edited.
     */
    public boolean editSkill(String oldSkill, String newSkill) {
        if (oldSkill == null || newSkill == null) return false;
        String cleanOld = oldSkill.trim();
        String cleanNew = newSkill.trim();

        if (cleanNew.isEmpty() || !containsSkill(cleanOld)) {
            return false;
        }

        // Check if the new name clashes with another existing skill (unless it's just casing)
        if (!cleanOld.equalsIgnoreCase(cleanNew) && containsSkill(cleanNew)) {
            return false;
        }

        // Rebuild set preserving order
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

    /**
     * Clears all skills.
     */
    public void clearSkills() {
        skills.clear();
    }

    /**
     * Replaces current skills with a new collection.
     */
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
