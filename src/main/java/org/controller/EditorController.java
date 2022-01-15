package org.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;
import org.controller.tools.drawingtool.graphiccontrol.handlers.HandlerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditorController implements Initializable, ControlScreen {

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
    @FXML
    private Button btnDrawUndo, btnDrawRedo;

    private File imagePath;
    private EventHandler<MouseEvent> drawer = event -> {}, mover = event -> {};
    private DrawingTool dt;
    private HandlerFactory handlerFactory;
    Canvas editorCanvasImage;
    Project project;
    private double width;
    private double height;
    private Parent drawOptRoot;
    private FXMLLoader drawOptLoader;
    private DrawOptionsController options;
    private Stage drawOptStage = new Stage();
    private Attributes attributes = new Attributes();

    private ScreensController screensController;
    private Window window;
    private int screenWidth;
    private int screenHeight;
    private double canvasWidth;
    private double canvasHeight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //editorImageView.setImage();
        this.dt = new DrawingTool(editorCanvas, stack);
        handlerFactory = new HandlerFactory(dt);
        initDrawOptions();
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        this.screensController = screenPage;
    }

    @Override
    public void setWindow(Window window) {
        this.window = window;
        this.screenWidth = window.getScreenWidth();
        this.screenHeight = window.getScreenHeight();
    }

    public void setCanvas(Project project) {
        this.canvasWidth = project.getCanvasWidth();
        this.canvasHeight = project.getCanvasHeight();
    }


    public void handleArc(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions();
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.ARC, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleMove(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        mover = handlerFactory.getHandler(HandlerFactory.Handler.MOVE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,mover);
    }
    public void handleCircle(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions();
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.CIRCLE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleEllipses(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions();
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.ELLIPSES, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);

    }
    public void handleRectangle(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions();
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.RECTANGLE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleLine(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions();
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.LINE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleText(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions();
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.TEXT, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handlePolygon(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions();
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.POLYGON, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleDrawUndo(ActionEvent e){
        dt.backward();
    }
    public void handleDrawRedo(ActionEvent e){
        dt.forward();
    }
    public void openDrawOptions(ActionEvent e){
        drawOptStage.centerOnScreen();
        drawOptStage.show();
    }
    public void openDrawOptions(){
        drawOptStage.centerOnScreen();

        drawOptStage.show();
    }
    public void initDrawOptions(){
        try{
            drawOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/options.fxml")));
            drawOptRoot = drawOptLoader.load();
            options = drawOptLoader.getController();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        drawOptStage = new Stage();
        Scene drawOptScene = new Scene(drawOptRoot);
        drawOptScene.setFill(Color.TRANSPARENT);
        drawOptStage.setScene(drawOptScene);
        drawOptStage.centerOnScreen();
        drawOptStage.initStyle(StageStyle.UNDECORATED);
        drawOptStage.initStyle(StageStyle.TRANSPARENT);
        drawOptStage.setTitle("Draw Options");
        drawOptStage.setResizable(false);
        drawOptStage.initModality(Modality.APPLICATION_MODAL);
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

    public void handleAddNoise(ActionEvent event) {
    }

    /*
        Import images via drag and drop
    */
    @FXML
    private void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }
    @FXML
    private void handleDragDropped(DragEvent dragEvent) {
        try {
            File f = dragEvent.getDragboard().getFiles().get(0);
            imagePath = f;
            Image droppedImage = new Image(new FileInputStream(f));

            // Pass image to setter method
            setImportedImage(droppedImage);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /*
        Import images from explorer
    */
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

        // Pass image to setter method
        setImportedImage(image);
    }

    public void setImportedImage(Image importedImage) {
        editorCanvasImage = new Canvas(getResizedImageWidth(getImageAspectRatio(importedImage)), getResizedImageHeight());
        stack.getChildren().add(editorCanvasImage);
        stack.setAlignment(editorCanvasImage, Pos.CENTER);

        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.drawImage(importedImage,0, 0, getResizedImageWidth(getImageAspectRatio(importedImage)), getResizedImageHeight());
        System.out.println(width + height);


        //disables import button if image was imported
        if (imagePath != null) {
            importButton.setDisable(true);
            importButton.setVisible(false);
            openFile.setDisable(true);
        }
    }

    public void setWidthAndHeight(Project project){
        this.width = project.getCanvasWidth();
        this.height = project.getCanvasHeight();
    }

    public double getResizedImageWidth(double ratio){
        GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int widthMonitor = monitor.getDisplayMode().getWidth();
        int heightMonitor = monitor.getDisplayMode().getHeight();
        double resizedHeight = 0;
        double resizedWidth = 0;

        if (heightMonitor > 1000 && heightMonitor < 1400){
            resizedWidth = 900 / ratio;
        }
        else if (heightMonitor > 1400 && heightMonitor < 2160){
            resizedWidth = 1300 / ratio;
        }
        else if (heightMonitor < 1000){
            resizedWidth = 600 / ratio;
        }
        else if (heightMonitor > 2100){
            resizedWidth = 1900 / ratio;
        }

        return resizedWidth;
    }
    public double getResizedImageHeight(){
        GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int widthMonitor = monitor.getDisplayMode().getWidth();
        int heightMonitor = monitor.getDisplayMode().getHeight();
        double resizedHeight = 0;


        if (heightMonitor > 1000 && heightMonitor < 1400){
            resizedHeight = 900;
        }
        else if (heightMonitor > 1400 && heightMonitor < 2160){
            resizedHeight = 1300;
        }
        else if (heightMonitor < 1000){
            resizedHeight = 600;
        }
        else if (heightMonitor > 2100){
            resizedHeight = 1900;
        }

        return resizedHeight;
    }

    public double getImageAspectRatio(Image image){
        double height = image.getHeight();
        double width = image.getWidth();
        double ratio = height / width;
        return ratio;
    }
}
