package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Objects;

import java.io.IOException;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, ControlScreen {

    ScreensController screensController;
    Window window;
    private double screenWidth;
    private double screenHeight;

    private Project project;

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
    @FXML
    private Pane settingsPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        this.screensController = screenPage;
    }

    @Override
    public void setWindow(Window window) {
        this.window = window;
        this.screenWidth = window.getScreenWidth();
        this.screenHeight = window.getScreenHeight();
    }


    public void handleCreateProject(ActionEvent event) throws IOException {
        try {
            double projectWidth = Double.parseDouble(widthInput.getText());
            double projectHeight = Double.parseDouble(heightInput.getText());
            project = new Project(projectWidth, projectHeight);

            EditorController ec = new EditorController();
            enterProject();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void enterProject(){
        screensController.setScreen(GuiDriver.editorScreenID);
        setEditorPresets();
    }

    public void handleOpenGallery(ActionEvent event) {
    }

    public void handleLogOut(ActionEvent event) {
    }

    public void setEditorPresets(){
        EditorController ec = screensController.getLoader().getController();
        ec.setWidthHeightAspectRatio(project);
        ec.setStackPane();
        ec.setCanvas(project);

    }



}
