package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    @FXML
    private StackPane stack;

    private File imagePath;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //editorImageView.setImage();
    }

    
    public void handleOpenFile(ActionEvent event) {
        importImageFromExplorer();
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

    public void handleImportButton(ActionEvent event) throws IOException {
        importImageFromExplorer();
        }

    public void importImageFromExplorer(){
        // opens file explore to choose image to edit
        FileChooser chooser = new FileChooser();
        File f = chooser.showOpenDialog(null);
        // saves file path from image to file object
        imagePath = f;
        //sets ImageView to chosen picture
        Image image = new Image(f.getPath());
        editorImageView.setImage(image);
        editorImageView.fitWidthProperty().bind(stack.widthProperty());


        //disables import button if image was imported
        if (editorImageView != null) {
            importButton.setDisable(true);
            importButton.setVisible(false);
        }
    }
}
