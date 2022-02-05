package org.editor.tools.imagetool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.editor.EditorController;

import java.net.URL;
import java.util.ResourceBundle;

public class ScaleOptionsController implements Initializable {
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

    public void handleApplyScale(ActionEvent event) {
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
        initButtons();
    }

    public void handleScaleOptions(ActionEvent event) {
        stage = (Stage) closeScaleOptions.getScene().getWindow();
        editorController.drawUnfilteredCanvasImage();
        stage.close();
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
