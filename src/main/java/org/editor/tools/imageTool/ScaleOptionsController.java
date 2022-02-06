package org.editor.tools.imageTool;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.EditorController;

import java.net.URL;
import java.util.ResourceBundle;

public class ScaleOptionsController implements Initializable {
    private static final Logger SO_LOGGER = LogManager.getLogger(ScaleOptionsController.class);
    @FXML
    private TextField scaleFactorInput;
    @FXML
    private Button closeScaleOptions;
    @FXML
    private ButtonBar bar;

    private double scaleFactor;
    private EditorController editorController;
    private Stage stage;
    private Point2D delta;

    @FXML
    private void handleApplyScale() {
        scaleFactor = Double.parseDouble(scaleFactorInput.getText());

        double newWidth = editorController.getEditorImageObject().getScaledWidth(scaleFactor);
        double newHeight = editorController.getEditorImageObject().getScaledHeight(scaleFactor);
        editorController.getEditorImageObject().setCurrentWidth(newWidth);
        editorController.getEditorImageObject().setCurrentHeight(newHeight);

        double newOriginalWidth = editorController.getOriginalImageObject().getScaledWidth(scaleFactor);
        double newOriginalHeight = editorController.getOriginalImageObject().getScaledHeight(scaleFactor);
        editorController.getOriginalImageObject().setCurrentWidth(newOriginalWidth);
        editorController.getOriginalImageObject().setCurrentHeight(newOriginalHeight);


        editorController.drawScaledOriginalImage(newOriginalWidth, newOriginalHeight);
        editorController.drawScaledImage(newWidth, newHeight);
        SO_LOGGER.debug("applied scaling change");
    }
    @FXML
    private void handleChangeInput(){
        if(!scaleFactorInput.getText().equals("")){
            try {
                scaleFactor = Double.parseDouble(scaleFactorInput.getText());
                double newWidth = editorController.getEditorImageObject().getScaledWidth(scaleFactor);
                double newHeight = editorController.getEditorImageObject().getScaledHeight(scaleFactor);
                editorController.drawScaledImage(newWidth, newHeight);
                } catch (NumberFormatException exception){
                SO_LOGGER.warn("Could not read number from input field");
            }
        }
    }
    @FXML
    private void handleCloseScaleOptions() {
        stage = (Stage) closeScaleOptions.getScene().getWindow();
        editorController.drawUnfilteredCanvasImage();
        stage.close();
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scaleFactorInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                scaleFactorInput.setText(newValue.replaceAll("[^\\d]", ""));
            }

        });
        initButtons();
    }
    private void initButtons(){
        bar.addEventHandler(MouseEvent.ANY, event -> {
            stage = (Stage) bar.getScene().getWindow();
            if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
                delta = new Point2D(stage.getX()-event.getScreenX(),stage.getY()-event.getScreenY());
            }else if(event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
                stage.setX(event.getScreenX()+delta.getX());
                stage.setY(event.getScreenY()+delta.getY());
            }
        });
    }
}
