package com.snapstudy.backend.model;

public enum DegreeType {
    SOCIAL_SCIENCES_AND_LAW,
    HEALTH_SCIENCES,
    EXACT_AND_NATURAL_SCIENCES,
    ENGINEERING_AND_ARCHITECTURE,
    ARTS_AND_HUMANITIES,
    INFORMATION_AND_COMMUNICATION_SCIENCES,
    AGRICULTURAL_AND_VETERINARY_SCIENCES;

    // Method to get a user-friendly name
    public String getDisplayName() {
        String name = this.name().toLowerCase().replace("_", " ");
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}