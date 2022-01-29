package org.editor.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.EditorController;
import org.marketplace.gallery.GalleryController;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, ControlScreen {
    private static final Logger logger = LogManager.getLogger(SettingsController.class);

    private ScreensController screensController;
    private Window window;
    private Project project;

    @FXML
    private TextField projectName;
    @FXML
    private TextField widthInput;
    @FXML
    private TextField heightInput;
    @FXML
    private ColorPicker cpBackgroundColor;
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
    @FXML
    private RadioButton radioBackground;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Arrays.stream(new TextField[]{widthInput,heightInput}).forEach(t -> t.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                t.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }));
        cpBackgroundColor.setValue(Color.WHITE);
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
            int projectWidth = Integer.parseInt(widthInput.getText());
            int projectHeight = Integer.parseInt(heightInput.getText());
            if(projectName.getText() == null || projectName.getText().trim().equals("")){
                throw new InvalidNameException();
            }
            if(radioBackground.isSelected()){
                project = new Project(projectName.getText().trim(), projectWidth, projectHeight, true ,cpBackgroundColor.getValue());
            }else{
                project = new Project(projectName.getText().trim(), projectWidth, projectHeight, false ,cpBackgroundColor.getValue());
            }
            createProject.setDisable(true);
            enterProject();
        } catch (NumberFormatException | InvalidNameException e) {
            if(e instanceof NumberFormatException){
                logger.warn("tried to create project with invalid dimensions");
            }else{
                logger.warn("tried to create project with invalid name");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Please enter valid values for project width, height and name");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    logger.warn("pressed ok");
                }
            });
        } catch (Exception e){
            logger.error(e.getCause());
            e.printStackTrace();
        }

    }

    public void enterProject(){
        screensController.setScreen(ScreenName.EDITOR);
        setEditorPresets();
    }
    public void handleTransparent(){
        cpBackgroundColor.setDisable(radioBackground.isSelected());
    }

    public void handleOpenGallery(ActionEvent event) {
        screensController.setScreen(ScreenName.GALLERY);
        GalleryController gc = (GalleryController)screensController.getController(ScreenName.GALLERY);
        gc.init();
    }

    public void handleLogOut(ActionEvent event) {
        //TODO
    }

    public void setEditorPresets(){
        EditorController ec = (EditorController)screensController.getController(ScreenName.EDITOR);
        ec.setProject(project);
        ec.initEC();
    }
}
