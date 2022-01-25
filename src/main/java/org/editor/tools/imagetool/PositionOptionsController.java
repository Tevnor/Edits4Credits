package org.editor.tools.imagetool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.editor.EditorController;

import java.net.URL;
import java.util.ResourceBundle;

public class PositionOptionsController implements Initializable {

    @FXML
    private TextField xPositionInput;
    @FXML
    private TextField yPositionInput;
    @FXML
    private Button applyPositionChange;


    private double xPosition = 0;
    private double yPosition = 0;
    private EditorController editorController;

    public void handleApplyPositionChange(ActionEvent event) {

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

    }
    public void handleXPositionInput(ActionEvent e){
        if(!xPositionInput.getText().equals("")){
            try {
                if (xPositionInput.getText().equals("")){
                    xPosition = 0;
                }
                else if(xPositionInput.getText() != null){
                    xPosition = Double.parseDouble(xPositionInput.getText());
                }
                editorController.drawChangedPosition(xPosition, yPosition);

            } catch (NumberFormatException exception){
                exception.printStackTrace();
            }
        }
    }
    public void handleYPositionInput(ActionEvent e){
        if(!xPositionInput.getText().equals("")){
            try {
                if (yPositionInput.getText().equals("")){
                    yPosition = 0;
                }
                else if(yPositionInput.getText() != null){
                    yPosition = Double.parseDouble(yPositionInput.getText());
                }
                editorController.drawChangedPosition(xPosition, yPosition);

            } catch (NumberFormatException exception){
                exception.printStackTrace();
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
    }
}

