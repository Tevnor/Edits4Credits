package org.editor.tools.filterTool;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.EditorController;

import java.net.URL;
import java.util.ResourceBundle;


public class NoiseController implements Initializable {
    private static final Logger NC_LOGGER = LogManager.getLogger(NoiseController.class);
    @FXML
    private Slider noiseSlider;
    @FXML
    private Button closeNoiseOptions;
    @FXML
    private ButtonBar bar;


    private double noiseStrength;
    private WritableImage filteredImage;
    private WritableImage filteredOriginalImage;
    private EditorController editorController;
    private Stage stage;
    private Point2D delta;
    private boolean applied = false;

    @FXML
    private void handleCloseNoiseOptions() {
        stage = (Stage) closeNoiseOptions.getScene().getWindow();
        if (!applied){
            editorController.drawUnfilteredCanvasImage();
        }
        stage.close();
    }
    @FXML
    private void handleApplyNoiseOnlyOnImage() {
        noiseStrength = noiseSlider.getValue();

        filteredImage = addNoiseToImage(editorController.getOriginalImageObject().createWritableOriginalImage(), noiseStrength);
        filteredOriginalImage = addNoiseToImage(editorController.getOriginalImageObject().createWritableOriginalImage(), noiseStrength);

        editorController.getEditorImageObject().setFilteredImage(filteredImage);
        editorController.drawFilteredImage();

        editorController.getOriginalImageObject().setFilteredImage(filteredOriginalImage);
        editorController.drawFilteredOriginalImage();
        applied = true;
        stage.close();
        NC_LOGGER.debug("applied noise to image");
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }
    public WritableImage addNoiseToImage(WritableImage inputImage, double noiseStrength){
        PixelReader pixelReader = inputImage.getPixelReader();

        int w = (int) inputImage.getWidth();
        int h = (int) inputImage.getHeight();

        WritableImage image = new WritableImage(w,h);
        PixelWriter pixelWriter = image.getPixelWriter();

        for(int y= 0; y < h; y++){
            for (int x = 0; x < w; x++){
                Color color = pixelReader.getColor(x,y);

                double noise = Math.random() / noiseStrength;

                Color newColor = new Color(Math.min(color.getRed() + noise, 1),
                        Math.min(color.getGreen() + noise, 1),
                        Math.min(color.getBlue() + noise, 1),
                        1);

                pixelWriter.setColor(x, y, newColor);
            }
        }
        return image;
    }
    public void setApplied(){
        this.applied = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noiseSlider.valueProperty().addListener((observableValue, number, t1) -> {
            noiseStrength = noiseSlider.getValue();
            filteredImage = addNoiseToImage(editorController.getEditorImageObject().createWritableOriginalImage(), noiseStrength);
            editorController.getEditorImageObject().setPreviewImage(filteredImage);;
            editorController.drawPreviewImage();
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


