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
        setCenter();
    }

    public void setCenter() {
        double xCenter = screenWidth / 4d;
        double yCenter = screenHeight / 4d;

//        this.screensController.setLayoutX(xCenter - 300);
//        this.screensController.setLayoutY(yCenter - 300);

//        settingsPane.setLayoutX(xCenter - 300);
//        settingsPane.setLayoutY(yCenter - 300);
    }

    public void handleCreateProject(ActionEvent event) throws IOException {
        try {
            double projectWidth = Double.parseDouble(widthInput.getText());
            double projectHeight = Double.parseDouble(heightInput.getText());
            project = new Project(projectWidth, projectHeight);

            EditorController ec = new EditorController();
            ec.setCanvas(project);



            enterProject();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void enterProject(){
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(Objects.requireNonNull(getClass().getResource("/fxml/editor.fxml")));
//            AnchorPane editorPane = loader.load();
//
//            EditorController export = loader.getController();
//            export.setWidthAndHeight(project);
//
//            rootAnchorPane.getChildren().setAll(editorPane);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            e.getCause();
//        }
        screensController.setScreen(GuiDriver.editorScreenID);
    }

    public void handleOpenGallery(ActionEvent event) {
    }

    public void handleLogOut(ActionEvent event) {
    }


}
