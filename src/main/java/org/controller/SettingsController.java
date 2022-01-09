package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

import java.io.IOException;

public class SettingsController {
    @FXML
    private TextField projectName;
    @FXML
    private TextField width;
    @FXML
    private TextField height;
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



    public void handleCreateProject(ActionEvent event) throws IOException {
        try {
            AnchorPane editorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/editor.fxml")));
            rootAnchorPane.getChildren().setAll(editorPane);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void handleOpenGallery(ActionEvent event) {
    }

    public void handleLogOut(ActionEvent event) {
    }
}
