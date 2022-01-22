package org.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.canvas.Canvas;
import javax.imageio.ImageIO;


public class NoiseController {
    @FXML
    private Slider noiseSlider;
    @FXML
    private Button applyNoiseOnlyOnImage;
    @FXML
    private Button applyNoiseOnEverything;

    private double noiseStrength;
    private WritableImage filteredImage;
    private WritableImage filteredOriginalImage;
    private EditorController editorController;
    private Canvas editorImageCanvas;

    public void handleApplyNoiseOnlyOnImage(ActionEvent event) {
        noiseStrength = noiseSlider.getValue();
        filteredImage = addNoiseToImage(editorController.createWritableOriginalImage(), noiseStrength);
        filteredOriginalImage = addNoiseToImage(editorController.createWritableOriginalImage(), noiseStrength);
        editorController.setFilteredImage(filteredImage);
        editorController.drawFilteredImage();
        editorController.setFilteredOriginalImage(filteredOriginalImage);
        editorController.drawFilteredOriginalImage();
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }

    public void handleApplyNoiseOnEverything(ActionEvent event) {

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

}
