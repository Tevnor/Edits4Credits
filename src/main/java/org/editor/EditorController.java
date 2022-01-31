package org.editor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import org.editor.tools.drawingtool.handlers.EraserDrawer;
import org.editor.tools.drawingtool.handlers.HandlerFactory;
import org.editor.tools.drawingtool.handlers.PolygonDrawer;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import org.editor.tools.filtertool.FilterOptionsController;
import org.editor.tools.filtertool.NoiseController;
import org.editor.tools.filtertool.filtercontrol.FilterApplicationType;
import org.editor.tools.filtertool.filtercontrol.filter.FilterType;
import org.editor.tools.imagetool.ImageDimensions;
import org.editor.tools.imagetool.ImageTool;
import org.editor.tools.imagetool.PositionOptionsController;
import org.editor.tools.imagetool.ScaleOptionsController;
import org.marketplace.gallery.GalleryController;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import static org.editor.tools.filtertool.filtercontrol.FilterApplicationType.CHECKERBOARD;
import static org.editor.tools.filtertool.filtercontrol.FilterApplicationType.STANDARD;
import static org.editor.tools.filtertool.filtercontrol.filter.FilterType.*;

public class EditorController implements Initializable, ControlScreen {

    private static final Logger EC_LOGGER = LogManager.getLogger(EditorController.class.getName());

    @FXML
    private ContextMenu contextPoly, contextRect;
    @FXML
    private MenuItem menuItemPoly, menuItemRect;
    @FXML
    private Button importButton;
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
    private StackPane stack;
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
    private org.editor.tools.drawingtool.handlers.DrawHandler drawer;
    private DrawingTool dt;
    private HandlerFactory handlerFactory;
    private Parent drawOptRoot;
    private DrawOptionsController options;
    private Stage drawOptStage = new Stage();

    private ImportControl ic = new ImportControl();
    private Canvas editorCanvasImage;
    private Parent noiseOptRoot;
    private NoiseController noiseController;
    private Stage noiseOptStage = new Stage();
    private ImageTool imageTool;


    private Parent moveOptRoot;
    private FXMLLoader moveOptLoader;
    private PositionOptionsController moveOptions;


    private GraphicsContext gc;
    private Parent scaleOptRoot;
    private FXMLLoader scaleOptLoader;
    private ScaleOptionsController scaleOptions;

    private Canvas originalCanvas;
    //private Boolean isFiltered = false;
    private ImageDimensions originalImageObject;
    private ImageDimensions editorImageObject;
    private EditorControllerLayoutManager layoutManager;

    /**
     * Filter Variables
     * */
    private Parent filterOptRoot;
    private FilterOptionsController filterOptionsController;
    private Stage filterOptStage = new Stage();
//    private List<FilterType> filterTypeList;
    private FilterType filterType;


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
        initFilterOptions();
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
        noiseOptScene.setFill(Color.TRANSPARENT);
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
    private void initImage(){
        EC_LOGGER.debug("entered initImage()");
        editorImageObject = ic.getImageDimInstance();
        originalImageObject = ic.getImageDimInstance();
        editorCanvasImage = ic.getEditorImageCanvas(stack);
        originalCanvas = ic.getOriginalImageCanvas(project);
        ic.setImportedImgOnCanvas(editorCanvasImage,editorImageObject, false);
        ic.setImportedImgOnCanvas(originalCanvas,originalImageObject, true);
        layoutManager.changeDisableBtn(importButton);
        layoutManager.addToStack(editorCanvasImage,stack);
        imageTool = ic.getImageTool(editorCanvasImage);
    }
    private void delete(){
        EC_LOGGER.debug("entered delete()");
        layoutManager.removeFromStack(editorCanvasImage, stack);
        editorImageObject = null;
        originalImageObject = null;
        editorCanvasImage = null;
        originalCanvas = null;
        layoutManager.changeDisableBtn(importButton);
        imageTool = null;
        ic.reset();
        orderStack();
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
    private void handleArc(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.ARC);
    }
    @FXML
    private void handleMove(){
        initMoveHandler();
        mover = handlerFactory.getMove(dt);
        stack.addEventHandler(MouseEvent.ANY,mover);
    }
    @FXML
    private void handleCircle(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.CIRCLE);
    }
    @FXML
    private void handleEllipses(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.ELLIPSES);
    }
    @FXML
    private void handleEraser(){
        initShapeHandler();
        dt.getDc().removeMarkingHandler(stack);
        openDrawOptions(HandlerFactory.DrawHandler.ERASER);
    }
    @FXML
    private void handleRectangle(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.RECTANGLE);
    }
    @FXML
    private void handleRoundedRectangle(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.ROUNDED_RECTANGLE);
    }
    @FXML
    private void handleLine(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.LINE);
    }
    @FXML
    private void handleText(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.TEXT);
    }
    @FXML
    private void handlePolygon(){
        initShapeHandler();
        openDrawOptions(HandlerFactory.DrawHandler.POLYGON);
    }
    @FXML
    private void handlePath(){
        initShapeHandler();
        dt.getDc().removeMarkingHandler(stack);
        openDrawOptions(HandlerFactory.DrawHandler.PATH);
    }
    @FXML
    private void handleDrawPolygon(){
        if(drawer instanceof PolygonDrawer){
            ((PolygonDrawer) drawer).drawPolygon();
        }
    }
    @FXML
    private void handleClear(){
        dt.clear();
    }
    @FXML
    private void handleDrawUndo(){
        dt.backward();
    }
    @FXML
    private void handleDrawRedo(){
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
        }else if(drawer instanceof EraserDrawer){
            ((EraserDrawer) drawer).reset();
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
    public void initFilterOptions() {
        try {
            /**
             * Filter Variables
             * */
            FXMLLoader filterOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/filterOptions.fxml")));
            filterOptRoot = filterOptLoader.load();
            filterOptionsController = filterOptLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterOptStage = new Stage();
        Scene filterOptScene = new Scene(filterOptRoot);
        filterOptScene.setFill(Color.TRANSPARENT);
        filterOptStage.setScene(filterOptScene);
        filterOptStage.centerOnScreen();
    }

    public void handleAddNoise(ActionEvent event) {
        noiseOptStage.centerOnScreen();
        noiseOptStage.initStyle(StageStyle.UNDECORATED);
        noiseOptStage.initStyle(StageStyle.TRANSPARENT);
        noiseOptStage.setResizable(false);
        noiseOptStage.initModality(Modality.APPLICATION_MODAL);
        noiseOptStage.show();

    }

    // Checkerboard mode
    public void handleApplyCheckerboard(ActionEvent event) {
        openFilterOptions(CHECKERBOARD, null);
    }

    // Singular filter
    public void handleAddGlitch(ActionEvent event) {
        openFilterOptions(STANDARD, GLITCH);
    }
    public void handleAddInverse(ActionEvent event) {
        openFilterOptions(STANDARD, INVERTED);
    }
    public void handleAddGrayscale(ActionEvent event) {
        openFilterOptions(STANDARD, GRAYSCALE);
    }

    private void openFilterOptions(FilterApplicationType appType,  FilterType filterType) {
        try {
            filterOptionsController.initFilterOptions(
                    originalImageObject.getFilteredImage(),
                    editorImageObject.getFilteredImage(),
                    appType,
                    filterType,
                    this);
            filterOptStage.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            EC_LOGGER.debug("Images not loaded");
            // Maybe add finally to prompt users to import image
        }
    }
    //TODO new
    public void setFilteredImages(Image originalImage, Image editorImage) {
        originalImageObject.setFilteredImage(originalImage);
        editorImageObject.setFilteredImage(editorImage);
        editorImageObject.setPreviousImage(editorImage);
    }
    public void drawFilteredImage(){
        gc = editorCanvasImage.getGraphicsContext2D();
        gc.drawImage(editorImageObject.getFilteredImage(), editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
        //initImageTool(filteredImage);
    }

    /**
     * Drawing to Canvas
     * */
    //TODO duplicate code

    // overwrite both canvases
    public void drawFilteredImages(){
        // Draw resized
        gc = editorCanvasImage.getGraphicsContext2D();
        gc.drawImage(
                editorImageObject.getFilteredImage(),
                editorImageObject.getCurrentXPosition(),
                editorImageObject.getCurrentYPosition(),
                editorImageObject.getCurrentWidth(),
                editorImageObject.getCurrentHeight());

        //Draw original
        GraphicsContext orig = originalCanvas.getGraphicsContext2D();
        orig.drawImage(
                originalImageObject.getFilteredImage(),
                originalImageObject.getCurrentXPosition(),
                originalImageObject.getCurrentYPosition(),
                originalImageObject.getCurrentWidth(),
                originalImageObject.getCurrentHeight());
    }
    // Draw temporary preview image
    public void drawPreviousImage(){
        gc = editorCanvasImage.getGraphicsContext2D();
        gc.drawImage(editorImageObject.getPreviousImage() ,editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }
    // Draw temporary preview image
    public void drawPreviewImage(Image previewImage){
        gc = editorCanvasImage.getGraphicsContext2D();
        gc.drawImage(previewImage,editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }

    // Scale the imported source image to the maximum canvas size

    public void handleMoveImage(ActionEvent event) {
        try{
            moveOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/moveOptions.fxml")));
            moveOptRoot = moveOptLoader.load();
            Stage moveOptStage = new Stage();
            Scene moveOptScene = new Scene(moveOptRoot);
            moveOptScene.setFill(Color.TRANSPARENT);
            moveOptStage.setScene(moveOptScene);
            moveOptStage.initStyle(StageStyle.UNDECORATED);
            moveOptStage.initStyle(StageStyle.TRANSPARENT);
            moveOptStage.setResizable(false);
            moveOptStage.initModality(Modality.APPLICATION_MODAL);
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
        gc.drawImage(editorImageObject.getFilteredImage(), newXPosition, newYPosition, editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }
    public void handleScaleImage(ActionEvent event){
        try {
            scaleOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/scaleOptions.fxml")));
            scaleOptRoot = scaleOptLoader.load();
            Stage scaleOptStage = new Stage();
            Scene scaleOptScene = new Scene(scaleOptRoot);
            scaleOptScene.setFill(Color.TRANSPARENT);
            scaleOptStage.setScene(scaleOptScene);
            scaleOptStage.initStyle(StageStyle.UNDECORATED);
            scaleOptStage.initStyle(StageStyle.TRANSPARENT);
            scaleOptStage.setResizable(false);
            scaleOptStage.initModality(Modality.APPLICATION_MODAL);
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
    public void drawScaledImage(double scaledWidth, double scaledHeight) {
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(editorImageObject.getFilteredImage(), editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), scaledWidth, scaledHeight);
    }

    /**
     * File
     * */
    @FXML
    private void importImageFromExplorer(){
        if(ic.setImageFromExplorer()){
            initImage();
        }
        orderStack();
    }
    public void setImportedImage(Image img){
        if(ic.setImageFromGallery(img)){
            initImage();
        }
        orderStack();
    }
    @FXML
    private void handleSaveGallery() {
        ic.save(project,originalCanvas,dt.getPixelBufferOfDrawing(project),true);
    }
    @FXML
    private void handleSaveExtern() {
        ic.save(project,originalCanvas,dt.getPixelBufferOfDrawing(project),false);
    }
    @FXML
    private void handleDeleteFile() {
        delete();
    }
    @FXML
    private void handleGallery() {
        ((GalleryController)screensController.getController(ScreenName.GALLERY)).init(true);
        screensController.setScreen(ScreenName.GALLERY);
    }


    public double getOriginalAndEditorCanvasRatio(){
        double ratio = originalCanvas.getHeight()/ editorCanvasImage.getHeight();
        return ratio;
    }
    public void drawChangedOriginalPosition(){
        GraphicsContext gc = originalCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, originalCanvas.getWidth(), originalCanvas.getHeight());
        gc.drawImage(originalImageObject.getFilteredImage(), originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), originalImageObject.getCurrentWidth(),originalImageObject.getCurrentHeight());
    }
    public void drawScaledOriginalImage(double newWidth, double newHeight){
        GraphicsContext gc = originalCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, originalCanvas.getWidth(), originalCanvas.getHeight());
        gc.drawImage(originalImageObject.getFilteredImage(), originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), newWidth, newHeight);
    }
    public void drawFilteredOriginalImage(){
        gc = originalCanvas.getGraphicsContext2D();
        gc.drawImage(originalImageObject.getOriginalImage(),originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), originalImageObject.getCurrentWidth(), originalImageObject.getCurrentHeight());
        //initImageTool(filteredOriginalImage);
    }
    public ImageDimensions getOriginalImageObject(){
        return this.originalImageObject;
    }
    public void drawUnfilteredCanvasImage(){
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(editorImageObject.getOriginalImage(), editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }
}