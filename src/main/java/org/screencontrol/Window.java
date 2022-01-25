package org.screencontrol;

import javafx.stage.Screen;

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
    public double getScaleX(){
        Screen screen = Screen.getPrimary();
        return screen.getOutputScaleX();
    }
    public double getScaleY(){
        Screen screen = Screen.getPrimary();
        return screen.getOutputScaleY();
    }

}
