package org.screencontrol;

public class Window {
    private final double screenWidth;
    private final double screenHeight;

    public Window(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public double getScreenWidth() {
        return screenWidth;
    }
    public double getScreenHeight() {
        return screenHeight;
    }
}
