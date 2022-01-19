package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, ControlScreen {
    private static final Logger logger = LogManager.getLogger(SettingsController.class.getName());

    ScreensController screensController;
    Window window;
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
    }

    public void handleCreateProject(ActionEvent event)  {
        try {
            double projectWidth = Double.parseDouble(widthInput.getText());
            double projectHeight = Double.parseDouble(heightInput.getText());
            project = new Project(projectWidth, projectHeight);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("tried to create project with no passed width and height");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Please enter valid values for project width and height");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    logger.warn("pressed ok");
                }
            });
        }
        enterProject();
    }

    public void enterProject(){
        screensController.setScreen(GuiDriver.editorScreenID);
        setEditorPresets();
    }

    public void handleOpenGallery(ActionEvent event) {
        //TODO
    }

    public void handleLogOut(ActionEvent event) {
        //TODO
    }

    public void setEditorPresets(){
        EditorController ec = screensController.getLoader().getController();
        ec.setWidthHeightAspectRatio(project);
        ec.setStackPane();
        ec.setCanvas(project);
        ec.initDrawOptions();
        ec.initAddNoiseOpt();
    }
}
