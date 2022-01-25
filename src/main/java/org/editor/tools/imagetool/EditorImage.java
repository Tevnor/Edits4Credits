package org.editor.tools.imagetool;

import javafx.scene.image.Image;

public class EditorImage {
    private double xPosition;
    private double yPosition;
    private double currentWidth;
    private double currentHeight;
    private Image originalImage;
    private Image filteredImage;

    public EditorImage(Image originalImage, double xPosition, double yPosition, double currentWidth, double currentHeight){
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
    public Image getOriginalImage() {
        return this.originalImage;
    }
    public void setFilteredImage(Image image){
        this.filteredImage = image;
    }
    public Image getFilteredImage(){
        return this.filteredImage;
    }
    public double getScaledWidth(double scaleFactor) {
        double scaledWidth = currentWidth * scaleFactor /100;
        return scaledWidth;
    }
    public double getScaledHeight(double scaleFactor) {
        double scaledHeight = currentHeight * scaleFactor /100;
        return scaledHeight;
    }
}
