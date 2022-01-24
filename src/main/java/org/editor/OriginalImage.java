package org.editor;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class OriginalImage {
    private double xPosition;
    private double yPosition;
    private double currentWidth;
    private double currentHeight;
    private Image originalImage;
    private Image originalFilteredImage;

    public OriginalImage(Image originalImage, double xPosition, double yPosition, double currentWidth, double currentHeight){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.currentWidth = currentWidth;
        this.currentHeight = currentHeight;
        this.originalImage = originalImage;
    }
    public void setCurrentWidth(double newWidth){
        this.currentWidth = newWidth;
    }
    public void setCurrentHeight(double newHeight){
        this.currentHeight = newHeight;
    }
    public void setCurrentXPosition(double newXPosition){
        this.xPosition = newXPosition;
    }
    public void setCurrentYPosition(double newYPosition){
        this.yPosition = newYPosition;
    }
    public double getCurrentWidth(){
        return this.currentWidth;
    }
    public double getCurrentHeight(){
        return this.currentHeight;
    }
    public double getCurrentXPosition(){
        return this.xPosition;
    }
    public double getCurrentYPosition(){
        return this.yPosition;
    }
    public Image getOriginalImage(){
        return this.originalImage;
    }
    public void setOriginalFilteredImage(Image image){
        this.originalFilteredImage = image;
    }
    public Image getOriginalFilteredImage(){
        return this.originalFilteredImage;
    }

    public WritableImage createWritableOriginalImage(){
        Canvas tmp = new Canvas(originalImage.getWidth(), originalImage.getHeight());
        GraphicsContext gc = tmp.getGraphicsContext2D();
        gc.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
        WritableImage originalImage = tmp.snapshot(null, null);
        return originalImage;
    }
    public double getScaledOriginalWidth(double scaleFactor) {
        double scaledWidth = currentWidth * scaleFactor /100;
        return scaledWidth;
    }
    public double getScaledOriginalHeight(double scaleFactor) {
        double scaledHeight = currentHeight * scaleFactor /100;
        return scaledHeight;
    }

}
