package org.editor.project;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Project {
//    private String projectName;
    private final double projectWidth;
    private final double projectHeight;
    private final double projectAspectRatio;
    private final boolean transparent;
    private ImageView bgTransparent;
    private Rectangle bgColor;
    private Color backgroundColor;
    //private String backgroundColor;


    public Project(double projectWidth, double projectHeight) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = projectWidth / projectHeight;
        this.transparent = true;
        this.bgTransparent = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/transparent_bg.png"))));
        bgTransparent.setPreserveRatio(false);
    }
    public Project(double projectWidth, double projectHeight, Color backgroundColor) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = projectWidth / projectHeight;
        this.transparent = false;
        this.backgroundColor = backgroundColor;
        this.bgColor = new Rectangle(projectWidth,projectHeight,backgroundColor);
    }


    public boolean isTransparent(){
        return this.transparent;
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
        if(transparent){
            return bgTransparent;
        }
        return bgColor;
    }

}
