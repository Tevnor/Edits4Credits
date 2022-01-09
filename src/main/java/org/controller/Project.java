package org.controller;

public class Project {
    private String projectName;
    private int canvasWidth;
    private int canvasHeight;
    private String backgroundColor;

    public Project(String projectName, int canvasWidth, int canvasHeight, String backgroundColor) {
        this.projectName = projectName;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.backgroundColor = backgroundColor;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
