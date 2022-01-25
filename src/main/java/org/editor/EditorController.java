package org.editor;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.layout.EditorControllerLayoutManager;
import org.editor.project.Project;
import org.editor.tools.drawingtool.DrawOptionsController;
import org.editor.tools.drawingtool.DrawingTool;
import org.editor.tools.drawingtool.graphiccontrol.handlers.HandlerFactory;
import org.editor.tools.drawingtool.graphiccontrol.handlers.PolygonDrawer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import org.editor.tools.filtertool.NoiseController;
import org.editor.tools.imagetool.ImageDimensions;
import org.editor.tools.imagetool.ImageTool;
import org.editor.tools.imagetool.PositionOptionsController;
import org.editor.tools.imagetool.ScaleOptionsController;
import org.editor.tools.imagetool.filtercontrol.filter.FilterType;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import javax.imageio.ImageIO;

public class EditorController implements Initializable, ControlScreen {

    private static final Logger EC_LOGGER = LogManager.getLogger(EditorController.class.getName());
    @FXML
    private ContextMenu contextPoly, contextRect;
    @FXML
    private MenuItem menuItemPoly, menuItemRect;
    @FXML
    private Button importButton;
    @FXML
    private Canvas editorCanvas;
    @FXML
    private javafx.scene.control.MenuItem addNoise;
    @FXML
    private javafx.scene.control.MenuItem addGlitch;
    @FXML
    private javafx.scene.control.MenuItem checkerboardFilterItem;
    @FXML
    private javafx.scene.control.MenuItem saveImage;
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
    private StackPane stack;
    @FXML
    private Button btnDrawUndo, btnDrawRedo;
    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private ToolBar toolBar;

    private Project project;
    private org.screencontrol.Window window;
    private ScreensController screensController;

    private EventHandler<MouseEvent> mover;
    private org.editor.tools.drawingtool.graphiccontrol.handlers.DrawHandler drawer;
    private DrawingTool dt;
    private HandlerFactory handlerFactory;
    private Parent drawOptRoot;
    private DrawOptionsController options;
    private Stage drawOptStage = new Stage();

    private Canvas editorCanvasImage;
    private Parent noiseOptRoot;
    private NoiseController noiseController;
    private Stage noiseOptStage = new Stage();
    private Image filteredImage;
    private ImageTool imageTool;
    List<FilterType> filterTypeEnumList;

    private Parent moveOptRoot;
    private FXMLLoader moveOptLoader;
    private PositionOptionsController moveOptions;


    private GraphicsContext gc;
    private Parent scaleOptRoot;
    private FXMLLoader scaleOptLoader;
    private ScaleOptionsController scaleOptions;

    private Canvas originalCanvas;
    private Boolean isFiltered = false;
    private ImageDimensions originalImageObject;
    private ImageDimensions editorImageObject;
    private EditorControllerLayoutManager layoutManager;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @Override
    public void setScreenParent(ScreensController screenPage) {
        this.screensController = screenPage;
    }

    @Override
    public void setWindow(Window window) {
        this.window = window;
    }


    /**
     *  Initializer
     **/

    public void setProject(Project project){
        this.project = project;
    }
    public void initEC(){
        initStackPane();
        initDrawTool();
        initDrawOptions();
        initAddNoiseOpt();
        initControls();
        initBackground();
        orderStack();
    }
    // method to calculate stack pane size
    private void initStackPane() {
            layoutManager = new EditorControllerLayoutManager();
            stack.setPrefWidth(layoutManager.getStackWidth(this));
            stack.setPrefHeight(layoutManager.getStackHeight(this));
    }
    public Window getWindow(){return this.window;}
    public Project getProject(){return this.project;}
    private void initDrawTool() {
        this.dt = new DrawingTool(stack);
        handlerFactory = new HandlerFactory();
        this.importButton.toFront();
    }
    //initializes controls and fits them to the screen
    private void initControls(){
        double viewCenterY = (((window.getScreenHeight() - menuBar.getPrefHeight()) / 2d) + menuBar.getPrefHeight());
        menuBar.setLayoutX(window.getScreenWidth()/2 - menuBar.getPrefWidth()/2);
        toolBar.setLayoutY(viewCenterY - (toolBar.getPrefHeight() / 2d));
        double x = (window.getScreenWidth() - getToolBarWidth())/2 + toolBar.getPrefWidth() + (20/window.getScaleX()) - stack.getPrefWidth()/2;
        double y = (window.getScreenHeight() - getMenuBarHeight())/2 + menuBar.getPrefHeight() + (20/window.getScaleY()) - stack.getPrefHeight()/2;
        stack.setLayoutX(x);
        stack.setLayoutY(y);
        EC_LOGGER.debug("succesfully created stack (x layout: " + x + ", y layout: " + y + ")");
    }
    private void initAddNoiseOpt() {
        try {
            FXMLLoader noiseOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/noiseOptions.fxml")));
            noiseOptRoot = noiseOptLoader.load();
            noiseController = noiseOptLoader.getController();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        noiseOptStage = new Stage();
        Scene noiseOptScene = new Scene(noiseOptRoot);
        noiseOptStage.setScene(noiseOptScene);

        noiseController.setEditorController(this);
    }
    // Return false: use width, return true: use height
    public double getMenuBarHeight(){
        return (menuBar.getPrefHeight() + 40/window.getScaleY());
    }
    public double getToolBarWidth(){
        return (toolBar.getPrefWidth() + 40/window.getScaleX());
    }
    private void initBackground(){
        if(project.getBackground() instanceof ImageView){
            ImageView bgTransparent = (ImageView) project.getBackground();
            bgTransparent.setFitHeight(stack.getPrefHeight());
            bgTransparent.setFitWidth(stack.getPrefWidth());
            stack.getChildren().add(bgTransparent);
            bgTransparent.toBack();
        }else if(project.getBackground() instanceof Rectangle){
            Rectangle bgColor = (Rectangle) project.getBackground();
            bgColor.setWidth(stack.getPrefWidth());
            bgColor.setHeight(stack.getPrefHeight());
            stack.getChildren().add(bgColor);
            bgColor.toBack();
        }

    }
    private void initDrawOptions(){
        loadDrawOptions();
        drawOptStage = new Stage();
        Scene drawOptScene = new Scene(drawOptRoot);
        drawOptScene.setFill(Color.TRANSPARENT);
        drawOptStage.setScene(drawOptScene);
        drawOptStage.centerOnScreen();
        drawOptStage.initStyle(StageStyle.UNDECORATED);
        drawOptStage.initStyle(StageStyle.TRANSPARENT);
        drawOptStage.setResizable(false);
        drawOptStage.initModality(Modality.APPLICATION_MODAL);
        drawOptStage.setOnHiding( w -> {
            if(options.getTmpHandler() != HandlerFactory.DrawHandler.MOVE){
                drawer = handlerFactory.getHandler(dt, options.getTmpHandler());
                stack.addEventHandler(MouseEvent.ANY,drawer);
                drawer.setAttributes(options.getAttributes());
            }
            options.resetLayout();

        });
    }
    private void loadDrawOptions(){
        try{
            FXMLLoader drawOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/drawOptions.fxml")));
            drawOptRoot = drawOptLoader.load();
            options = drawOptLoader.getController();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     *  Organizer
     **/

    private void orderStack(){
        importButton.toFront();
        dt.getCanvasBrush().toBack();
        dt.getCanvasShapes().toBack();
        if(editorCanvasImage != null){
            editorCanvasImage.toBack();
        }
        project.getBackground().toBack();
    }

    /**
     *  Drawing
     **/

    @FXML
    private void handleArc(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.ARC);
    }
    @FXML
    private void handleMove(ActionEvent e){
        initMoveHandler();
        mover = handlerFactory.getMove(dt);
        stack.addEventHandler(MouseEvent.ANY,mover);
    }
    @FXML
    private void handleCircle(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.CIRCLE);
    }
    @FXML
    private void handleEllipses(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.ELLIPSES);
    }
    @FXML
    private void handleEraser(ActionEvent e){
        initShapeHandler();
        dt.getDc().removeMarkingHandler(stack);
        openDrawOptions(HandlerFactory.DrawHandler.ERASER);
    }
    @FXML
    private void handleRectangle(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.RECTANGLE);
    }
    @FXML
    private void handleRoundedRectangle(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.ROUNDED_RECTANGLE);
    }
    @FXML
    private void handleLine(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.LINE);
    }
    @FXML
    private void handleText(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.TEXT);
    }
    @FXML
    private void handlePolygon(ActionEvent e){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.POLYGON);
    }
    @FXML
    private void handlePath(ActionEvent e){
        initShapeHandler();
        dt.getDc().removeMarkingHandler(stack);
        openDrawOptions(HandlerFactory.DrawHandler.PATH);
    }
    @FXML
    private void handleDrawPolygon(ActionEvent e){
        if(drawer instanceof PolygonDrawer){
            ((PolygonDrawer) drawer).drawPolygon();
        }
    }
    @FXML
    private void handleClear(ActionEvent e){
        dt.clear();
    }
    @FXML
    private void handleDrawUndo(ActionEvent e){
        dt.backward();
    }
    @FXML
    private void handleDrawRedo(ActionEvent e){
        dt.forward();
    }
    private void openDrawOptions(HandlerFactory.DrawHandler drawHandler){
        drawOptStage.centerOnScreen();
        options.setSelShape(drawHandler);
        drawOptStage.show();
    }
    private void initShapeHandler(){
        importButton.setVisible(false);
        dt.getDc().addMarkingHandler(stack);
        if(mover != null){
            stack.removeEventHandler(MouseEvent.ANY, mover);
        }
        if(drawer instanceof PolygonDrawer){
            ((PolygonDrawer) drawer).resetPoints();
        }
        if(drawer != null){
            stack.removeEventHandler(MouseEvent.ANY, drawer);
        }
        stack.setCursor(Cursor.DEFAULT);
    }
    private void initMoveHandler(){
        initShapeHandler();
        dt.getDc().removeMarkingHandler(stack);
        stack.setCursor(Cursor.OPEN_HAND);
    }

    /**
     * Filter
     * */

    public void handleAddNoise(ActionEvent event) {
        noiseOptStage.centerOnScreen();
        noiseOptStage.show();
    }

    public WritableImage createImageFromCanvas(Canvas canvas){
        WritableImage filterImage = canvas.snapshot(null, null);
        return filterImage;
    }

    // Apply the checkerboard filter to the filterTool object that was instantiated at setImportedImage()
    public void handleApplyCheckerboard(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(FilterType.ORIGINAL);
        filterTypeEnumList.add(FilterType.GLITCH);
        filterTypeEnumList.add(FilterType.INVERTED);
        filterTypeEnumList.add(FilterType.GRAYSCALE);

        startImageTool();
    }

    public void handleAddGlitch(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(FilterType.GLITCH);

        startImageTool();
    }
    public void handleAddInverse(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(FilterType.INVERTED);

        startImageTool();
    }
    public void handleAddGrayscale(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(FilterType.GRAYSCALE);

        startImageTool();
    }

    public void startImageTool() {
        imageTool.startProcess(filterTypeEnumList);
        filteredImage = imageTool.getFilteredImage();
        setImage();
    }

    public void setImage() {
        gc.drawImage(filteredImage,0, 0, filteredImage.getWidth(), filteredImage.getHeight());
        initImageTool(filteredImage);

        filterTypeEnumList.clear();
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
    /*@FXML
    private void handleDragDropped(DragEvent dragEvent) {
        try {
            File f = dragEvent.getDragboard().getFiles().get(0);
            imagePath = f;
            Image droppedImage = new Image(new FileInputStream(f));

            // Pass image to setter method
            setImageToCanvas(droppedImage);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    */

    public void handleImportButton(ActionEvent event) throws IOException {
        importImageFromExplorer();
        orderStack();
    }
    public void importImageFromExplorer(){
        Image fileChooserImage = layoutManager.getFileChooserImage();
        createEditorImage(fileChooserImage);
        createOriginalImage(fileChooserImage);
        setImageToCanvas(fileChooserImage);
        setImportOriginalImage(originalImageObject.getOriginalImage());
    }
    public void setImageToCanvas(Image image) {
        // Pass image to setter method
        setEditorImageCanvas();
        setOriginalImageCanvas();
        setImportedImage(image);
    }

    // create empty canvas for images with the size of the stack pane
    public void setEditorImageCanvas(){
        editorCanvasImage = new Canvas(stack.getPrefWidth(), stack.getPrefHeight());
        stack.getChildren().add(editorCanvasImage);

        StackPane.setAlignment(editorCanvasImage, Pos.CENTER);
        editorCanvasImage.toBack();
    }
    public void setOriginalImageCanvas(){
        this.originalCanvas = new Canvas(project.getProjectWidth(), project.getProjectHeight());
    }
    public void setFilteredImage(Image image){
        this.filteredImage = image;
    }
    public void drawFilteredImage(){
        gc = editorCanvasImage.getGraphicsContext2D();
        gc.drawImage(filteredImage, editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
        //initImageTool(filteredImage);
    }

    // draw selected image to the image canvas
    public void setImportedImage(Image importedImage) {
        gc = editorCanvasImage.getGraphicsContext2D();

        double ratio = getImageAspectRatio(importedImage);
        Image resizedImage;
        int editorCanvasImageWidth = (int) editorCanvasImage.getWidth();
        int editorCanvasImageHeight = (int) editorCanvasImage.getHeight();

        // Instantiate resized image from imported image
        if (ratio >= 1){
            resizedImage = editorImageObject.scaleImage(importedImage, editorCanvasImageWidth, getResizedImageHeight(editorCanvasImageWidth, ratio), true, true);
            if(resizedImage.getHeight() > stack.getPrefHeight()){
                resizedImage = editorImageObject.scaleImage(importedImage, getResizedImageWidth(editorCanvasImageHeight, ratio), editorCanvasImageHeight, true, true);
            }
        } else {
            resizedImage = editorImageObject.scaleImage(importedImage, getResizedImageWidth(editorCanvasImageHeight, ratio), editorCanvasImageHeight, true, true);
            if(resizedImage.getWidth() > stack.getPrefWidth()){
                resizedImage = editorImageObject.scaleImage(importedImage, editorCanvasImageWidth, getResizedImageHeight(editorCanvasImageWidth,ratio), true, true);
            }
        }

        editorImageObject.setCurrentWidth(resizedImage.getWidth());
        editorImageObject.setCurrentHeight(resizedImage.getHeight());
        // Draw resized image onto editorCanvasImage
        gc.drawImage(resizedImage,0, 0, resizedImage.getWidth(), resizedImage.getHeight());


        //disables import button if image was imported
        if (importedImage != null) {
            importButton.setDisable(true);
            importButton.setVisible(false);
            //openFile.setDisable(true);
        }

        // Instantiate ImageTool Object
        initImageTool(resizedImage);
    }
    public void setImportOriginalImage(Image importedImage){
        gc = originalCanvas.getGraphicsContext2D();

        double ratio = getImageAspectRatio(importedImage);
        Image resizedOriginalImage;

        // Instantiate resized image from imported image
        if (ratio >= 1){
            resizedOriginalImage = originalImageObject.scaleImage(importedImage, project.getProjectWidth(), getResizedImageHeight(project.getProjectWidth(), ratio), true, true);
            if(resizedOriginalImage.getHeight() > project.getProjectHeight()) {
                resizedOriginalImage = originalImageObject.scaleImage(importedImage, getResizedImageWidth(project.getProjectHeight(), ratio), project.getProjectHeight(), true, true);
            }
        } else {
            resizedOriginalImage = originalImageObject.scaleImage(importedImage, getResizedImageWidth(project.getProjectHeight(), ratio), project.getProjectHeight(), true, true);
            if(resizedOriginalImage.getWidth() > project.getProjectWidth()){
                resizedOriginalImage = originalImageObject.scaleImage(importedImage, project.getProjectWidth(),  getResizedImageHeight(project.getProjectWidth(), ratio), true, true);
            }
        }

        originalImageObject.setCurrentWidth(resizedOriginalImage.getWidth());
        originalImageObject.setCurrentHeight(resizedOriginalImage.getHeight());

        // Draw resized image onto editorCanvasImage
        gc.drawImage(resizedOriginalImage,0, 0, resizedOriginalImage.getWidth(), resizedOriginalImage.getHeight());
    }

    public void initImageTool(Image img) {
        // Create pixel array from resized image to enhance performance of future references
        this.imageTool = new ImageTool(img, editorCanvasImage, gc);
        this.imageTool.createPixelArray();
    }

    // Scale the imported source image to the maximum canvas size


    public double getImageAspectRatio(Image image){
        return image.getWidth() / image.getHeight();
    }

    public double getResizedImageHeight(double height, double ratio){
        return height / ratio;
    }
    public double getResizedImageWidth(double width, double ratio){
        return width * ratio;
    }

    public void handleMoveImage(ActionEvent event) {
        try{

            moveOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/moveOptions.fxml")));
            moveOptRoot = moveOptLoader.load();
            Stage moveOptStage = new Stage();
            Scene moveOptScene = new Scene(moveOptRoot);
            moveOptStage.setScene(moveOptScene);
            moveOptStage.show();
            moveOptions = moveOptLoader.getController();
            moveOptions.setEditorController(this);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public void drawChangedPosition(double newXPosition, double newYPosition){
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        if(!isFiltered) {
            gc.drawImage(editorImageObject.getOriginalImage(), newXPosition, newYPosition, editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
        }
        else if (isFiltered){
            gc.drawImage(editorImageObject.getFilteredImage(), newXPosition, newYPosition, editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());

        }
    }
    public void handleScaleImage(ActionEvent event){
        try {
            scaleOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/scaleOptions.fxml")));
            scaleOptRoot = scaleOptLoader.load();
            Stage scaleOptStage = new Stage();
            Scene scaleOptScene = new Scene(scaleOptRoot);
            scaleOptStage.setScene(scaleOptScene);
            scaleOptStage.show();
            scaleOptions = scaleOptLoader.getController();
            scaleOptions.setEditorController(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public ImageDimensions getEditorImageObject(){
        return this.editorImageObject;
    }
    public void drawScaledImage(double scaledWidth, double scaledHeight){
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(editorImageObject.getOriginalImage(), editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), scaledWidth, scaledHeight);
    }

    /**
     * File
     * */
    public void handleOpenFile(ActionEvent event) {
        importImageFromExplorer();
        orderStack();
    }

    public void handleDeleteFile(ActionEvent event) {
    }

    public void handleFileSettings(ActionEvent event) {
    }

    public void handleSaveFile(ActionEvent event) {
/*        Stage fileWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        File outputFile = fileChooser.showSaveDialog(fileWindow);
        if (outputFile != null) {
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(project.getFinalImage(dt.getPixelBufferOfDrawing(project)), null);
                ImageIO.write(bufferedImage, "png", outputFile);
            } catch (IOException ex) {

            }
        }*/


        //saveToFile(createImageFromCanvas(originalCanvas));
        saveToFile(project.getFinalImage(dt.getPixelBufferOfDrawing(project),getOriginalBuffer(originalCanvas)));
    }
    public static void saveToFile(Image writableImage) {
        try {
            File outputFile = new File("drawing.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void createOriginalImage(Image image){
        this.originalImageObject = new ImageDimensions(image, 0,0, image.getWidth(), image.getHeight());
    }
    public void createEditorImage(Image image){
        this.editorImageObject = new ImageDimensions(image, 0,0, image.getWidth(), image.getHeight());
    }


    public double getOriginalAndEditorCanvasRatio(){
        double ratio = originalCanvas.getHeight()/ editorCanvasImage.getHeight();
        return ratio;
    }
    public void drawChangedOriginalPosition(){
        GraphicsContext gc = originalCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, originalCanvas.getWidth(), originalCanvas.getHeight());
        if (isFiltered == false){
            gc.drawImage(originalImageObject.getOriginalImage(), originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), originalImageObject.getCurrentWidth(),originalImageObject.getCurrentHeight());
        }
        else if (isFiltered){
            gc.drawImage(originalImageObject.getFilteredImage(), originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), originalImageObject.getCurrentWidth(),originalImageObject.getCurrentHeight());
        }
    }
    public boolean getIsFiltered(){
        return this.isFiltered;
    }
    public void setIsFiltered(){
        this.isFiltered= true;
    }
    public void drawScaledOriginalImage(double newWidth, double newHeight){
        GraphicsContext gc = originalCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, originalCanvas.getWidth(), originalCanvas.getHeight());
        if(!isFiltered) {
            gc.drawImage(originalImageObject.getOriginalImage(), originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), newWidth, newHeight);
        }
        else if(isFiltered){
            gc.drawImage(originalImageObject.getFilteredImage(), originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), newWidth, newHeight);
        }
    }
    public void drawFilteredOriginalImage(){
        gc = originalCanvas.getGraphicsContext2D();
        gc.drawImage(originalImageObject.getOriginalImage(),originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), originalImageObject.getCurrentWidth(), originalImageObject.getCurrentHeight());
        //initImageTool(filteredOriginalImage);
    }
    public ImageDimensions getOriginalImageObject(){
        return this.originalImageObject;
    }

    public int[] getOriginalBuffer(Canvas canvas){
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        int[] export = new int[(int)(canvas.getWidth()*canvas.getHeight())];

        canvas.snapshot(sp,null).getPixelReader()
                .getPixels(0,0,(int)canvas.getWidth(),(int)canvas.getHeight(),
                        WritablePixelFormat.getIntArgbInstance(), export, 0, (int)canvas.getWidth());
        return export;
    }
}

