package org.editor.project;

import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;


public class Project {
//    private String projectName;
    private final int projectWidth;
    private final int projectHeight;
    private final double projectAspectRatio;
    private final boolean isTransparent;
    private ImageView bgTransparent;
    private Rectangle bgColor;
    private Color backgroundColor;
    //private String backgroundColor;


    public Project(int projectWidth, int projectHeight) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = (double) projectWidth / projectHeight;
        this.isTransparent = true;
        this.bgTransparent = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/transparent_bg.png"))));
        bgTransparent.setPreserveRatio(false);
    }
    public Project(int projectWidth, int projectHeight, Color backgroundColor) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = (double) projectWidth / projectHeight;
        this.isTransparent = false;
        this.backgroundColor = backgroundColor;
        this.bgColor = new Rectangle(projectWidth,projectHeight,backgroundColor);
    }


    public boolean isTransparent(){
        return this.isTransparent;
    }
    public ImageView getTransparent() {
        return bgTransparent;
    }
    public double getProjectWidth() {
        return projectWidth;
    }
    public double getProjectHeight() {
        return projectHeight;
    }
    public double getProjectAspectRatio() {return projectAspectRatio;}
    public Node getBackground(){
        if(isTransparent){
            return bgTransparent;
        }
        return bgColor;
    }
    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
