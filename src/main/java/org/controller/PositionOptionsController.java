package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PositionOptionsController implements Initializable {

    @FXML
    private TextField xPositionInput;
    @FXML
    private TextField yPositionInput;
    @FXML
    private Button applyPositionChange;


    private double xPosition;
    private double yPosition;
    private EditorController editorController;

    public void handleApplyPostionChange(ActionEvent event) {

        if (xPositionInput.getText() == ""){
            xPosition = 0;
        }
        else if(xPositionInput.getText() != null){
            xPosition = Double.parseDouble(xPositionInput.getText());
        }
        if (yPositionInput.getText() == ""){
            yPosition = 0;
        }
        else if (yPositionInput.getText() != null){
            yPosition = Double.parseDouble(yPositionInput.getText());
        }
        editorController.setChangedPostion(xPosition, yPosition, editorController.getCurrentImageWidth(), editorController.getCurrentImageHeight());
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xPositionInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                xPositionInput.setText(newValue.replaceAll("[^\\d]", ""));
            }

        });
        yPositionInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        yPositionInput.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
        );
    }
}

