package com.academiago.backend.model;

public enum MaterialType {
    NOTE("Note"),
    SYLLABUS("Syllabus"),
    LECTURE_PPT("Lecture PPT"),
    OTHERS("Others");

    private final String displayName;

    MaterialType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
