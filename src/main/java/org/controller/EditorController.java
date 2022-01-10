package org.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.handlers.HandlerFactory;

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
    private ToggleButton arcBtn;
    @FXML
    private ToggleButton circleBtn;
    @FXML
    private ToggleButton ellipseBtn;
    @FXML
    private ToggleButton lineBtn;
    @FXML
    private ToggleButton rectangleBtn;
    @FXML
    private ToggleButton polygonBtn;
    @FXML
    private ToggleButton textBtn;
    @FXML
    private ToggleButton moveBtn;
    @FXML
    private ToggleButton drawOptionsBtn;
    @FXML
    private StackPane stack;

    private File imagePath;
    private EventHandler<MouseEvent> drawer = event -> {}, mover = event -> {};
    private DrawingTool dt;
    private HandlerFactory handlerFactory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //editorImageView.setImage();
        this.dt = new DrawingTool(editorCanvas, stack);
        handlerFactory = new HandlerFactory(dt);
    }

    public void handleArc(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.ARC);
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleMove(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        mover = handlerFactory.getHandler(HandlerFactory.Handler.MOVE);
        stack.addEventHandler(MouseEvent.ANY,mover);
    }
    public void handleCircle(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.CIRCLE);
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleEllipses(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.ELLIPSES);
        stack.addEventHandler(MouseEvent.ANY,drawer);

    }
    public void handleRectangle(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.RECTANGLE);
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleLine(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.LINE);
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleText(ActionEvent e){
        //openTextOptions();
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.TEXT);
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handlePolygon(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.POLYGON);
        stack.addEventHandler(MouseEvent.ANY,drawer);
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
