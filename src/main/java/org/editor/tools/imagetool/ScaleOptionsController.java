package org.editor.tools.imagetool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.editor.EditorController;

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

        //jedes mal wenn sich slider ändert wird das neu ausgeführt
        double newWidth = editorController.getEditorImageObject().getScaledWidth(scaleFactor);
        double newHeight = editorController.getEditorImageObject().getScaledHeight(scaleFactor);


        double newOriginalWidth = editorController.getOriginalImageObject().getScaledOriginalWidth(scaleFactor);
        double newOriginalHeight = editorController.getOriginalImageObject().getScaledOriginalHeight(scaleFactor);

        editorController.getEditorImageObject().setCurrentWidth(newWidth);
        editorController.getEditorImageObject().setCurrentHeight(newHeight);


        if(!editorController.getIsFiltered()){
            editorController.drawScaledImage(newWidth, newHeight);
            editorController.drawScaledOriginalImage(newOriginalWidth, newOriginalHeight);
        }
        else if(editorController.getIsFiltered()){
            editorController.drawScaledImage(newWidth, newHeight);
            editorController.drawScaledOriginalImage(newOriginalWidth, newOriginalHeight);
        }

        editorController.getOriginalImageObject().setCurrentWidth(newOriginalWidth);
        editorController.getOriginalImageObject().setCurrentHeight(newOriginalHeight);



    }

    public void handleChangeInput(ActionEvent e){
        if(!scaleFactorInput.getText().equals("")){
            try {
                scaleFactor = Double.parseDouble(scaleFactorInput.getText());
                double newWidth = editorController.getEditorImageObject().getScaledWidth(scaleFactor);
                double newHeight = editorController.getEditorImageObject().getScaledHeight(scaleFactor);
                if(!editorController.getIsFiltered()) {
                    editorController.drawScaledImage(newWidth, newHeight);
                }
                else if(editorController.getIsFiltered()) {
                    editorController.drawScaledImage(newWidth, newHeight);
                }

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
        scaleFactorInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                scaleFactorInput.setText(newValue.replaceAll("[^\\d]", ""));
            }

        });
    }
}
