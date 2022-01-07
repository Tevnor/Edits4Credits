package org.example;

import javafx.animation.ScaleTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    @FXML
    private Slider variableSlider;
    @FXML
    private ToggleButton silhouetteToggle;
    @FXML
    private ToggleButton complementToggle;
    @FXML
    private ToggleButton originalToggle;
    @FXML
    private ImageView editorImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //editorImageView.setImage();
    }

}
