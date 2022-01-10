package org.controller;

public class Project {
    private String projectName;
    private double canvasWidth;
    private double canvasHeight;
    //private String backgroundColor;

    public Project(String projectName, double canvasWidth, double canvasHeight) {
        this.projectName = projectName;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        //this.backgroundColor = backgroundColor;
    }

    public String getProjectName() {
        return projectName;
    }

    public double getCanvasWidth() {
        return canvasWidth;
    }

    public double getCanvasHeight() {
        return canvasHeight;
    }

    //public String getBackgroundColor() {
        //return backgroundColor;
    //}
}
