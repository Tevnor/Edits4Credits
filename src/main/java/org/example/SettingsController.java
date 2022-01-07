package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import java.io.IOException;
import java.awt.*;

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
