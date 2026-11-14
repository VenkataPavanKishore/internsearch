// InternSearch Web Client Interactivity
document.addEventListener('DOMContentLoaded', () => {
    // Auto-dismiss alert banners after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            try {
                const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                bsAlert.close();
            } catch (e) {}
        }, 5000);
    });

    // Quick add chip button click handler
    const suggestionChips = document.querySelectorAll('.quick-add-chip');
    const skillsInput = document.getElementById('skillsInput');
    if (skillsInput && suggestionChips.length > 0) {
        suggestionChips.forEach(chip => {
            chip.addEventListener('click', () => {
                const skillName = chip.getAttribute('data-skill');
                if (skillsInput.value.trim() === '') {
                    skillsInput.value = skillName;
                } else {
                    const current = skillsInput.value.split(',').map(s => s.trim());
                    if (!current.includes(skillName)) {
                        skillsInput.value += ', ' + skillName;
                    }
                }
                chip.classList.add('opacity-50');
                skillsInput.focus();
            });
        });
    }
});
