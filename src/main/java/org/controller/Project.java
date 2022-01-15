package org.controller;

public class Project {
//    private String projectName;
    private double canvasWidth;
    private double canvasHeight;
    //private String backgroundColor;

    public Project(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }


    public double getCanvasWidth() {
        return canvasWidth;
    }

    public double getCanvasHeight() {
        return canvasHeight;
    }

}
