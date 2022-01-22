package org.controller;

import javafx.scene.Group;
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
    private final Group projectGroup = new Group();
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
        addGroupMember(bgTransparent);
    }
    public Project(double projectWidth, double projectHeight, Color backgroundColor) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = projectWidth / projectHeight;
        this.transparent = false;
        this.backgroundColor = backgroundColor;
        this.bgColor = new Rectangle(projectWidth,projectHeight,backgroundColor);
        addGroupMember(bgColor);
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
    public void addGroupMember(Node s){
        projectGroup.getChildren().add(s);
    }
    public Node getBackground(){
        return projectGroup.getChildren().get(0);
    }

}
