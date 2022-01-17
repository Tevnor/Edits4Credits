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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;
import org.controller.tools.drawingtool.graphiccontrol.handlers.HandlerFactory;
import org.controller.tools.filtertool.FilterTool;

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
    private javafx.scene.control.MenuItem checkerboardFilterItem;
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
    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private ToolBar toolBar;

    private File imagePath;
    private EventHandler<MouseEvent> drawer = event -> {}, mover = event -> {};
    private DrawingTool dt;
    private HandlerFactory handlerFactory;
    Canvas editorCanvasImage;
    Project project;
    private double width;
    private double height;
    private double projectAspectRatio;
    private Parent drawOptRoot;
    private FXMLLoader drawOptLoader;
    private DrawOptionsController options;
    private Stage drawOptStage = new Stage();
    private Attributes attributes = new Attributes();

    ScreensController screensController;
    Window window;
    private double screenWidth;
    private double screenHeight;
    private double canvasWidth;
    private double canvasHeight;

    private Image resizedImage;
    private FilterTool filterTool;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dt = new DrawingTool(stack);
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
        this.canvasWidth = project.getProjectWidth();
        this.canvasHeight = project.getProjectHeight();
    }

    public void setWidthHeightAspectRatio(Project project){
        this.width = project.getProjectWidth();
        this.height = project.getProjectHeight();
        this.projectAspectRatio = project.getProjectAspectRatio();
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

    // Apply the checkerboard filter to the filterTool object that was instantiated at setImportedImage()
    public void handleApplyCheckerboard(ActionEvent event) {
        this.filterTool.selectCheckerboard();
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
        setEditorCanvas();
        setImportedImage(image);
    }

    public double getMenuBarHeight(){
        return menuBar.getPrefHeight();
    }
    public double getToolBarWidth(){
        return toolBar.getPrefWidth();
    }

    public void setStackPane() {
        double maxStackHeight = screenHeight - getMenuBarHeight();
        double maxStackWidth = screenWidth - getToolBarWidth();

        if (useWidthOrHeight()){
            double stackHeight = (int) screenHeight - getMenuBarHeight();
            double stackWidth = (int) stackHeight * projectAspectRatio;

            if (stackWidth > maxStackWidth) {
                stackWidth = maxStackWidth;
                stackHeight = stackWidth * projectAspectRatio;
            }
            stack.setPrefHeight(stackHeight);
            stack.setPrefWidth(stackWidth);
        }
        else {
            double stackWidth = screenWidth - getToolBarWidth();
            double stackHeight = stackWidth * (1 / projectAspectRatio);

            if (stackHeight > maxStackHeight) {
                stackHeight = maxStackHeight;
                stackWidth = stackHeight * projectAspectRatio;
            }
            stack.setPrefWidth(stackWidth);
            stack.setPrefHeight(stackHeight);
        }
    }

    // Return false: use width, return true: use height
    public boolean useWidthOrHeight(){
        return !(projectAspectRatio > 1);
    }
    public void setEditorCanvas(){
        editorCanvasImage = new Canvas(stack.getPrefWidth(), stack.getPrefHeight());
        stack.getChildren().add(editorCanvasImage);

        // TODO
//        stack.setAlignment(editorCanvasImage, Pos.CENTER);


        editorCanvasImage.toBack();
    }

    public void setImportedImage(Image importedImage) {
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        double ratio = getImageAspectRatio(importedImage);
        int editorCanvasImageWidth = (int) editorCanvasImage.getWidth();
        int editorCanvasImageHeight = (int) editorCanvasImage.getHeight();

        // Instantiate resized image from imported image
        if (ratio >= 1){
            resizedImage = scaleImage(importedImage, editorCanvasImageWidth, getResizedImageHeight(editorCanvasImageHeight, ratio), true, true);
        } else {
            resizedImage = scaleImage(importedImage, getResizedImageWidth(editorCanvasImageWidth, ratio), editorCanvasImageHeight, true, true);
        }

        // Draw resized image onto editorCanvasImage
        gc.drawImage(resizedImage,0, 0, resizedImage.getWidth(), resizedImage.getHeight());


        //disables import button if image was imported
        if (imagePath != null) {
            importButton.setDisable(true);
            importButton.setVisible(false);
            openFile.setDisable(true);
        }

        // Create pixel array from resized image to enhance performance of future references
        this.filterTool = new FilterTool(resizedImage, editorCanvasImage, stack);
        this.filterTool.stepOne();
    }

    // Scale the imported source image to the maximum canvas size
    public Image scaleImage(Image sourceImage, double targetWidth, double targetHeight, boolean preserveRatio, boolean smooth) {
        ImageView resizedImageView = new ImageView(sourceImage);
        resizedImageView.setPreserveRatio(preserveRatio);
        resizedImageView.setSmooth(smooth);

        resizedImageView.setFitWidth(targetWidth);
        resizedImageView.setFitHeight(targetHeight);

        return resizedImageView.snapshot(null, null);
    }

    public double getImageAspectRatio(Image image){
        return image.getWidth() / image.getHeight();
    }

    public double getResizedImageHeight(double height, double ratio){
        return height * ratio;
    }
    public double getResizedImageWidth(double width, double ratio){
        return width * ratio;
    }
}
