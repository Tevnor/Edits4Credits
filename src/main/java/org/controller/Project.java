package org.controller;

public class Project {
//    private String projectName;
    private double projectWidth;
    private double projectHeight;
    private double projectAspectRatio;
    //private String backgroundColor;

    public Project(double projectWidth, double projectHeight) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = projectWidth / projectHeight;
    }



    public double getProjectWidth() {
        return projectWidth;
    }

    public double getProjectHeight() {
        return projectHeight;
    }

    public double getProjectAspectRatio() {return projectAspectRatio;}

}
