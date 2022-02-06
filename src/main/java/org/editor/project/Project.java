package org.editor.project;

import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


public class Project {

    private static final Logger P_LOGGER = LogManager.getLogger(Project.class);

    private final String projectName;
    private final int projectWidth;
    private final int projectHeight;
    private final double projectAspectRatio;
    private final boolean isTransparent;
    private ImageView bgTransparent;
    private Rectangle bgColor;
    private Color backgroundColor;


    public Project(String projectName, int projectWidth, int projectHeight, boolean isTransparent, Color backgroundColor) {
        if (projectName == null || projectName.equals("")) {
            this.projectName = "no_name";
        } else {
            this.projectName = projectName;
        }
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = (double) projectWidth / projectHeight;
        this.isTransparent = isTransparent;

        if (isTransparent){
            this.bgTransparent = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/transparent_bg.png"))));
            bgTransparent.setPreserveRatio(false);
        } else {
            if (backgroundColor != null) {
                this.backgroundColor = backgroundColor;
                this.bgColor = new Rectangle(projectWidth,projectHeight,backgroundColor);
            } else {
                this.backgroundColor = Color.WHITE;
                this.bgColor = new Rectangle(projectWidth,projectHeight,Color.WHITE);
            }
        }
        P_LOGGER.info("New project {} created", projectName);
    }


    public String getProjectName(){
        return projectName;
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
