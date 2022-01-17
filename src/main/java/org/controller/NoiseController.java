package org.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FormatStringConverter;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;
import javafx.scene.paint.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.ResourceBundle;
import org.controller.EditorController;
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
    private EditorController editor;
    private Canvas editorImageCanvas;

    public NoiseController(EditorController editor, Canvas editorImageCanvas){
        this.editor = editor;
        this.editorImageCanvas = editorImageCanvas;
        System.out.println("noise controller erstellt");
    }

    public void handleApplyNoiseOnlyOnImage(ActionEvent event) {
        //System.out.println(editor.editorCanvasImage.getHeight());
        noiseStrength = noiseSlider.getValue();
        System.out.println(editor.createImageFromCanvas(editorImageCanvas).getHeight());
        filteredImage = addNoiseToImage(editor.createImageFromCanvas(editorImageCanvas), noiseStrength);
        saveToFile(filteredImage);
        System.out.println("Filter applied");
        editor.setFilteredImage();

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

    public WritableImage getFilteredImage(){
        return filteredImage;
    }
    public static void saveToFile(Image writableImage) {
        try {
            File outputFile = new File("savedimage.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }}
