package org.editor.tools.imagetool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.editor.EditorController;

import java.net.URL;
import java.util.ResourceBundle;

public class ScaleOptionsController implements Initializable {
    @FXML
    private TextField scaleFactorInput;
    @FXML
    private Button closeScaleOptions;

    private double scaleFactor;
    private EditorController editorController;
    private Stage stage;

    public void handleApplyScale(ActionEvent event) {

    }

    public void handleChangeInput(ActionEvent e){
        if(!scaleFactorInput.getText().equals("")){
            try {
                scaleFactor = Double.parseDouble(scaleFactorInput.getText());
                double newWidth = editorController.getEditorImageObject().getScaledWidth(scaleFactor);
                double newHeight = editorController.getEditorImageObject().getScaledHeight(scaleFactor);

                editorController.drawScaledImage(newWidth, newHeight);


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

    public void handleScaleOptions(ActionEvent event) {
        stage = (Stage) closeScaleOptions.getScene().getWindow();
        editorController.drawUnfilteredCanvasImage();
        stage.close();
    }
}
