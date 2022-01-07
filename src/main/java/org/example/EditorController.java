package org.example;

import javafx.animation.ScaleTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    @FXML
    private Button importButton;
    @FXML
    private Canvas editorCanvas;
    @FXML
    private javafx.scene.control.MenuItem addNoise;
    @FXML
    private javafx.scene.control.MenuItem saveFile;
    @FXML
    private javafx.scene.control.MenuItem openFileSettings;
    @FXML
    private javafx.scene.control.MenuItem deleteFile;
    @FXML
    private javafx.scene.control.MenuItem openFile;
    @FXML
    private ImageView editorImageView;
    @FXML
    private ToggleButton arc;
    @FXML
    private ToggleButton circle;
    @FXML
    private ToggleButton ellipses;
    @FXML
    private ToggleButton line;
    @FXML
    private ToggleButton rectangle;
    @FXML
    private ToggleButton polygon;
    @FXML
    private ToggleButton text;
    @FXML
    private ToggleButton move;
    @FXML
    private ToggleButton drawOptions;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //editorImageView.setImage();
    }

    
    public void handleOpenFile(ActionEvent event) {
    }

    public void handleDeleteFile(ActionEvent event) {
    }

    public void handleFileSettings(ActionEvent event) {
    }

    public void handleSafeFile(ActionEvent event) {
    }

    public void handleAddNouise(ActionEvent event) {
    }

    public void handleDragDropped(DragEvent dragEvent) {
    }

    public void handleDragOver(DragEvent dragEvent) {
    }

    public void handleButtonClick(ActionEvent event) {
    }
}
