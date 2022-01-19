package org.controller;

import javafx.scene.paint.Color;

public class Project {
//    private String projectName;
    private final double projectWidth;
    private final double projectHeight;
    private final double projectAspectRatio;
    private Color backgroundColor;
    //private String backgroundColor;

    public Project(double projectWidth, double projectHeight) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = projectWidth / projectHeight;
    }
    public Project(double projectWidth, double projectHeight, Color backgroundColor) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = projectWidth / projectHeight;
        this.backgroundColor = backgroundColor;
    }



    public double getProjectWidth() {
        return projectWidth;
    }

    public double getProjectHeight() {
        return projectHeight;
    }

    public double getProjectAspectRatio() {return projectAspectRatio;}

}
