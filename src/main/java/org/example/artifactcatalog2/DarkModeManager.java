package org.example.artifactcatalog2;

public class DarkModeManager {
    private static DarkModeManager instance;
    private boolean isDarkModeOn;

    private DarkModeManager() {
        this.isDarkModeOn = false;
    }

    public static DarkModeManager getInstance() {
        if (instance == null) {
            instance = new DarkModeManager();
        }
        return instance;
    }

    public boolean isDarkModeOn() {
        return isDarkModeOn;
    }

    public void setDarkModeOn(boolean isDarkModeOn) {
        this.isDarkModeOn = isDarkModeOn;
    }
}