package org.controller;

public class Window {
    private final double screenWidth;
    private final double screenHeight;
    private final double scaleX;
    private final double scaleY;

    public Window(double screenWidth, double screenHeight, double scaleX, double scaleY) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getScreenWidth() {
        return screenWidth;
    }
    public double getScreenHeight() {
        return screenHeight;
    }
    public double getScaleX() {
        return scaleX;
    }
    public double getScaleY() {
        return scaleY;
    }
}
