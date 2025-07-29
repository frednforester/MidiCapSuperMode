package com.midicap.midicap;

import java.util.List;

public class PageConfig {
    private GlobalSetup globalSetup;
    private PageSection pageSection;
    private List<KeySection> keySections; // Should be size 10

    public GlobalSetup getGlobalSetup() {
        return globalSetup;
    }

    public void setGlobalSetup(GlobalSetup globalSetup) {
        this.globalSetup = globalSetup;
    }

    public PageSection getPageSection() {
        return pageSection;
    }

    public void setPageSection(PageSection pageSection) {
        this.pageSection = pageSection;
    }

    public List<KeySection> getKeySections() {
        return keySections;
    }

    public void setKeySections(List<KeySection> keySections) {
        if (keySections != null && keySections.size() > 10) {
            throw new IllegalArgumentException("Number of KeySections cannot exceed 10. Found: " + keySections.size());
        }
        this.keySections = keySections;
    }

    @Override
    public String toString() {
        return "PageConfig{" +
                "globalSetup=" + globalSetup +
                ", pageSection=" + pageSection +
                ", keySections=" + keySections +
                '}';
    }
} 