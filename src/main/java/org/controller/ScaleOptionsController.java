package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ScaleOptionsController implements Initializable {
    @FXML
    private TextField scaleFactorInput;
    @FXML
    private Button applyScale;

    private double scaleFactor;
    private EditorController editorController;


    public void handleApplyScale(ActionEvent event) {
        scaleFactor = Double.parseDouble(scaleFactorInput.getText());

        double newWidth = editorController.getScaledWidth(scaleFactor);
        double newHeight = editorController.getScaledHeight(scaleFactor);
        double newOriginalWidth = editorController.getScaledOriginalWidth(scaleFactor);
        double newOriginalHeight = editorController.getScaledOriginalHeight(scaleFactor);

        editorController.setCurrentImageHeight(newHeight);
        editorController.setCurrentImageWidth(newWidth);
        editorController.setCurrentOriginalImageHeight(newOriginalHeight);
        editorController.setCurrentOriginalImageWidth(newOriginalWidth);

        if(editorController.getIsFiltered()== false){
            editorController.drawScaledImage(editorController.getOriginalImage(), editorController.getXPosition(), editorController.getYPosition(), newWidth, newHeight);
            editorController.drawScaledOriginalImage(editorController.getOriginalImage(), editorController.getXOriginalPosition(), editorController.getYOriginalPosition(), newOriginalWidth, newOriginalHeight);
        }
        else if(editorController.getIsFiltered()){
            editorController.drawScaledImage(editorController.getOriginalFilteredImage(), editorController.getXPosition(), editorController.getYPosition(), newWidth, newHeight);
            editorController.drawScaledOriginalImage(editorController.getOriginalFilteredImage(), editorController.getXOriginalPosition(), editorController.getYOriginalPosition(), newOriginalWidth, newOriginalHeight);
        }
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
    }
}
