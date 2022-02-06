package org.editor.project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.EditorController;
import org.launcher.GuiDriver;
import org.marketplace.gallery.GalleryController;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, ControlScreen {
    private static final Logger SC_LOGGER = LogManager.getLogger(SettingsController.class);

    private ScreensController screensController;
    private Project project;
    private Image base;

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
    public void setWindow(Window window) {}

    @FXML
    private void handleCreateProject()  {
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
                SC_LOGGER.warn("tried to create project with invalid dimensions");
            }else{
                SC_LOGGER.warn("tried to create project with invalid name");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Please enter valid values for project width, height and name");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    SC_LOGGER.warn("pressed ok");
                }
            });
        } catch (Exception e){
            SC_LOGGER.error(e.getCause());
            e.printStackTrace();
        }

    }
    @FXML
    private void handleTransparent(){
        cpBackgroundColor.setDisable(radioBackground.isSelected());
    }
    @FXML
    private void handleOpenGallery() {
        if(((GalleryController)screensController.getController(ScreenName.GALLERY)).setOpen(false)){
            screensController.setScreen(ScreenName.GALLERY);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING,"Images did not finish loading please wait.");
            alert.show();
        }
    }
    @FXML
    private void handleLogOut() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage dialog = (Stage)alert.getDialogPane().getScene().getWindow();
        alert.setHeaderText("Do you really want to log out?");
        if(GuiDriver.getIcon() != null){
            dialog.getIcons().add(GuiDriver.getIcon());
        }
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ((Stage)radioBackground.getScene().getWindow()).close();
                SC_LOGGER.info("logged out");
            }
        });
    }

    private void enterProject(){
        screensController.setScreen(ScreenName.EDITOR);
        setEditorPresets();
    }
    public void setBaseImage(Image img){
        base = img;
        widthInput.setText(Integer.toString((int)img.getWidth()));
        heightInput.setText(Integer.toString((int)img.getHeight()));
        widthInput.setDisable(true);
        heightInput.setDisable(true);
    }
    private void setEditorPresets(){
        EditorController ec = (EditorController)screensController.getController(ScreenName.EDITOR);
        ec.setProject(project);
        ec.setOpen();
        if(base != null){
            ec.setImportedImage(base);
        }
    }
}
