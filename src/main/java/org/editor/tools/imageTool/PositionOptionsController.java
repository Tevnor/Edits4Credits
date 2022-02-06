package org.editor.tools.imageTool;

import javafx.event.ActionEvent;
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

public class PositionOptionsController implements Initializable {
    private static final Logger PO_LOGGER = LogManager.getLogger(PositionOptionsController.class);

    @FXML
    private TextField xPositionInput;
    @FXML
    private TextField yPositionInput;
    @FXML
    private Button closeMoveOptions;
    @FXML
    private ButtonBar bar;

    private double xPosition = 0;
    private double yPosition = 0;
    private EditorController editorController;
    private Stage stage;
    private Point2D delta;

    @FXML
    private void handleCloseMoveOptions() {
        stage = (Stage) closeMoveOptions.getScene().getWindow();
        editorController.drawUnfilteredCanvasImage();
        stage.close();
    }
    @FXML
    private void handleApplyPositionChange() {
        if (xPositionInput.getText().equals("")){
            xPosition = 0;
        }
        else if(xPositionInput.getText() != null){
            xPosition = Double.parseDouble(xPositionInput.getText());
        }
        if (yPositionInput.getText().equals("")){
            yPosition = 0;
        }
        else if (yPositionInput.getText() != null){
            yPosition = Double.parseDouble(yPositionInput.getText());
        }
        editorController.getEditorImageObject().setCurrentXPosition(xPosition);
        editorController.getEditorImageObject().setCurrentYPosition(yPosition);
        editorController.drawChangedPosition(xPosition, yPosition);

        editorController.getOriginalImageObject().setCurrentXPosition(xPosition * editorController.getOriginalAndEditorCanvasRatio());
        editorController.getOriginalImageObject().setCurrentYPosition(yPosition * editorController.getOriginalAndEditorCanvasRatio());
        editorController.drawChangedOriginalPosition();
        PO_LOGGER.debug("applied position change");
    }
    @FXML
    private void handlePositionInput(ActionEvent e){

        if(e.getSource().equals(xPositionInput)){
            moveTmp(xPositionInput, true);
        }else if(e.getSource().equals(yPositionInput)) {
            moveTmp(yPositionInput, false);
        }

    }
    private void moveTmp(TextField field, boolean isX) {
        if(!field.getText().equals("")){
            try {
                if(field.getText() != null){
                    if(isX){
                        xPosition = Double.parseDouble(field.getText());
                    }else{
                        yPosition = Double.parseDouble(field.getText());
                    }
                    editorController.drawChangedPosition(xPosition, yPosition);
                }
            } catch (NumberFormatException exception){
                PO_LOGGER.warn("Could not read number from input field");
            }
        }

    }
    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xPositionInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") && !newValue.matches("[-]")) {
                xPositionInput.setText(newValue.replaceAll("[^\\d-]", ""));
            }

        });
        yPositionInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*") && !newValue.matches("[-]")) {
                        yPositionInput.setText(newValue.replaceAll("[^\\d-]", ""));
                    }
                }
        );
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

