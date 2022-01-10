package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

import java.io.IOException;

public class SettingsController {
    @FXML
    private TextField projectName;
    @FXML
    private TextField widthInput;
    @FXML
    private TextField heightInput;
    @FXML
    private TextField backgroundColor;
    @FXML
    private Button createProject;
    @FXML
    private Button openGallery;
    @FXML
    private Button logOut;
    @FXML
    private AnchorPane rootAnchorPane;

    public static double heightProject;
    public static double widthProject;
    public Project project;



    public void handleCreateProject(ActionEvent event) throws IOException {
        try {
            if (widthInput != null && heightInput != null) {
                widthProject = Integer.parseInt(widthInput.getText());
                heightProject = Integer.parseInt(heightInput.getText());
            }
            //FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/editor.fxml")));
            //EditorController export = loader.getController();
            //export.setWidthAndHeight(widthProject,heightProject);
            //AnchorPane editorPane = loader.load();

            project = new Project("Project 1", widthProject, heightProject);
            enterProject();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void enterProject(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/fxml/editor.fxml")));
            AnchorPane editorPane = loader.load();
            EditorController export = loader.getController();
            export.setWidthAndHeight(project);
            rootAnchorPane.getChildren().setAll(editorPane);
        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void handleOpenGallery(ActionEvent event) {
    }

    public void handleLogOut(ActionEvent event) {
    }
}
