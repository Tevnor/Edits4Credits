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

import org.editor.tools.filtertool.EffectOptionsController;
import org.editor.tools.filtertool.FilterOptionsController;
import org.editor.tools.filtertool.NoiseController;
import org.editor.tools.imagetool.ImageDimensions;
import org.editor.tools.imagetool.PositionOptionsController;
import org.editor.tools.imagetool.ScaleOptionsController;
import org.launcher.GuiDriver;
import org.marketplace.gallery.GalleryController;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import static org.editor.tools.filtertool.filtercontrol.FilterApplicationType.CHECKERBOARD;
import static org.editor.tools.filtertool.filtercontrol.FilterApplicationType.STANDARD;
import static org.editor.tools.filtertool.filtercontrol.effects.EffectType.*;
import static org.editor.tools.filtertool.filtercontrol.filter.FilterType.*;

public class EditorController implements Initializable, ControlScreen {

    private final Logger EC_LOGGER = LogManager.getLogger(this.getClass());

    @FXML
    private Button importButton;
    @FXML
    private StackPane stack;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuBarFile;
    @FXML
    private Menu menuBarImage;
    @FXML
    private Menu menuBarEffect;
    @FXML
    private Menu menuBarFilter;
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

    private Parent moveOptRoot;
    private FXMLLoader moveOptLoader;
    private PositionOptionsController moveOptions;

    private GraphicsContext graphicsContext;
    private Parent scaleOptRoot;
    private FXMLLoader scaleOptLoader;
    private ScaleOptionsController scaleOptions;

    private Canvas originalCanvas;
    private ImageDimensions originalImageObject;
    private ImageDimensions editorImageObject;
    private EditorControllerLayoutManager layoutManager;

    /**
     * Filter Variables
     * */
    private FXMLLoader filterOptLoader;
    private Parent filterOptRoot;
    private FilterOptionsController filterOptionsController;
    private Stage filterOptStage = new Stage();

    /**
     * Effect Variables
     * */
    private FXMLLoader effectOptLoader;
    private Parent effectOptRoot;
    private EffectOptionsController effectOptionsController;
    private Stage effectOptStage = new Stage();


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
    public void setOpen(){
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

    // Method shared by all options controllers to set their stage
    private Stage initStage(Parent root) {
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.centerOnScreen();

        return stage;
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
        toggleFileMenuItems(true);
        toggleMenuBarItems(true);
        EC_LOGGER.debug("Successfully created stack (x layout: " + x + ", y layout: " + y + ")");
    }
    private void initAddNoiseOpt() {
        try {
            FXMLLoader noiseOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/noiseOptions.fxml")));
            noiseOptRoot = noiseOptLoader.load();
            noiseController = noiseOptLoader.getController();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        noiseOptStage = initStage(noiseOptRoot);

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
        drawOptStage = initStage(drawOptRoot);
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

        toggleFileMenuItems(false);
        toggleMenuBarItems(false);

        loadFilterOptions();
        loadEffectOptions();
    }
    private void delete(){
        EC_LOGGER.debug("entered delete()");
        layoutManager.removeFromStack(editorCanvasImage, stack);
        editorImageObject = null;
        originalImageObject = null;
        editorCanvasImage = null;
        originalCanvas = null;
        layoutManager.changeDisableBtn(importButton);
        ic.reset();
        toggleMenuBarItems(true);
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
    private void toggleMenuBarItems(boolean setDisabled) {
        toggleFilterMenuItems(setDisabled);
        toggleImageMenuItems(setDisabled);
        toggleEffectMenuItems(setDisabled);
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
        toggleFileMenuItems(false);
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
    private void loadFilterOptions() {
        try {
            filterOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/filterOptions.fxml")));
            initFilterOptionsController();
            filterOptRoot = filterOptLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterOptStage = initStage(filterOptRoot);
    }

    private void initFilterOptionsController() {
        filterOptLoader.setController(new FilterOptionsController(
                originalImageObject.getFilteredImage(),
                editorImageObject.getFilteredImage(),
                this
        ));
        filterOptionsController = filterOptLoader.getController();
    }

    private void toggleFilterMenuItems(boolean setDisabled) {
        menuBarFilter.getItems().forEach(menuItem -> menuItem.setDisable(setDisabled));
    }
    @FXML
    private void handleAddNoise() {
        openNoiseOptions();
    }
    private void openNoiseOptions(){
        noiseController.setApplied();
        noiseOptStage.show();
    }

    // Checkerboard mode
    @FXML
    private void handleApplyCheckerboard() {
        filterOptionsController.setFilterView(CHECKERBOARD, null);
        filterOptStage.show();
    }

    // Singular filter
    @FXML
    private void handleAddGlitch() {
        filterOptionsController.setFilterView(STANDARD, GLITCH);
        filterOptStage.show();
    }
    @FXML
    private void handleAddInverse() {
        filterOptionsController.setFilterView(STANDARD, INVERTED);
        filterOptStage.show();
    }
    @FXML
    private void handleAddGrayscale() {
        filterOptionsController.setFilterView(STANDARD, GRAYSCALE);
        filterOptStage.show();
    }

    /**
     * Effect
     * */
    private void loadEffectOptions() {
        try {
            effectOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/effectOptions.fxml")));
            initEffectOptionsController();
            effectOptRoot = effectOptLoader.load();
            effectOptionsController = effectOptLoader.getController();
        } catch (Exception e ) {
            e.printStackTrace();
            EC_LOGGER.debug("FXML for {} couldn't be loaded", effectOptLoader);
        }
        effectOptStage = initStage(effectOptRoot);
    }
    private void initEffectOptionsController() {
        try {
            effectOptLoader.setController(new EffectOptionsController(
                    originalImageObject.getFilteredImage(),
                    editorImageObject.getFilteredImage(),
                    this
            ));
        } catch (Exception e) {
            e.printStackTrace();
            EC_LOGGER.error("Could not load {}", effectOptLoader);
        }
        effectOptionsController = effectOptLoader.getController();
    }

    private void toggleEffectMenuItems(boolean setDisabled) {
        menuBarEffect.getItems().forEach(menuItem -> menuItem.setDisable(setDisabled));
    }

    // Effects
    @FXML
    private void handleAddBlur() {
        effectOptionsController.setEffectView(BLUR);
        effectOptStage.show();
    }
    @FXML
    private void handleAddSepia() {
        effectOptionsController.setEffectView(SEPIA);
        effectOptStage.show();
    }
    @FXML
    private void handleAddDisplacement() {
        effectOptionsController.setEffectView(DISPLACE);
        effectOptStage.show();
    }
    // Adjustments
    @FXML
    private void handleAdjustBrightness() {
        effectOptionsController.setEffectView(BRIGHTNESS);
        effectOptStage.show();
    }
    @FXML
    private void handleAdjustContrast() {
        effectOptionsController.setEffectView(CONTRAST);
        effectOptStage.show();
    }
    @FXML
    private void handleAdjustSaturation() {
        effectOptionsController.setEffectView(SATURATION);
        effectOptStage.show();
    }

    /**
     * Drawing to Canvas
     * */
    public void drawFilteredImage(){
        graphicsContext = editorCanvasImage.getGraphicsContext2D();
        graphicsContext.drawImage(
                editorImageObject.getFilteredImage(),
                editorImageObject.getCurrentXPosition(),
                editorImageObject.getCurrentYPosition(),
                editorImageObject.getCurrentWidth(),
                editorImageObject.getCurrentHeight());
    }
    // overwrite both canvases
    public void drawFilteredImages(){
        // Draw editor image
        graphicsContext = editorCanvasImage.getGraphicsContext2D();
        graphicsContext.drawImage(
                editorImageObject.getFilteredImage(),
                editorImageObject.getCurrentXPosition(),
                editorImageObject.getCurrentYPosition(),
                editorImageObject.getCurrentWidth(),
                editorImageObject.getCurrentHeight());

        // Draw original image
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
        graphicsContext = editorCanvasImage.getGraphicsContext2D();
        graphicsContext.drawImage(editorImageObject.getPreviousImage() ,editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }
    // Draw temporary preview image
    public void drawPreviewImage(Image previewImage){
        graphicsContext = editorCanvasImage.getGraphicsContext2D();
        graphicsContext.drawImage(previewImage,editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }

    /**
     * Image Transforms
     * */
    private void toggleImageMenuItems(boolean setDisabled) {
        menuBarImage.getItems().forEach(menuItem -> menuItem.setDisable(setDisabled));
    }

    @FXML
    private void handleMoveImage() {
        try{
            moveOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/moveOptions.fxml")));
            moveOptRoot = moveOptLoader.load();
            Stage moveOptStage = initStage(moveOptRoot);
            moveOptStage.show();
            moveOptions = moveOptLoader.getController();
            moveOptions.setEditorController(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    @FXML
    private void handleScaleImage(){
        try {
            scaleOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/scaleOptions.fxml")));
            scaleOptRoot = scaleOptLoader.load();
            Stage scaleOptStage = initStage(scaleOptRoot);
            scaleOptStage.show();
            scaleOptions = scaleOptLoader.getController();
            scaleOptions.setEditorController(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public void drawChangedPosition(double newXPosition, double newYPosition){
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(editorImageObject.getFilteredImage(), newXPosition, newYPosition, editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }
    public ImageDimensions getEditorImageObject(){
        return this.editorImageObject;
    }
    public void drawScaledImage(double scaledWidth, double scaledHeight) {
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(editorImageObject.getFilteredImage(), editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), scaledWidth, scaledHeight);
    }
    public void setFilteredImages(Image originalImage, Image editorImage) {
        originalImageObject.setFilteredImage(originalImage);
        editorImageObject.setFilteredImage(editorImage);
        editorImageObject.setPreviousImage(editorImage);

        loadFilterOptions();
        loadEffectOptions();
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
        graphicsContext = originalCanvas.getGraphicsContext2D();
        graphicsContext.drawImage(originalImageObject.getOriginalImage(),originalImageObject.getCurrentXPosition(), originalImageObject.getCurrentYPosition(), originalImageObject.getCurrentWidth(), originalImageObject.getCurrentHeight());
    }
    public ImageDimensions getOriginalImageObject(){
        return this.originalImageObject;
    }
    public void drawUnfilteredCanvasImage(){
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(editorImageObject.getFilteredImage(), editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());
    }
    public void drawPreviewImage(){
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(editorImageObject.getPreviewImage(), editorImageObject.getCurrentXPosition(), editorImageObject.getCurrentYPosition(), editorImageObject.getCurrentWidth(), editorImageObject.getCurrentHeight());

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
        if(((GalleryController)screensController.getController(ScreenName.GALLERY)).setOpen(true)){
            screensController.setScreen(ScreenName.GALLERY);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING,"Images did not finish loading please wait.");
            alert.show();
        }
    }
    @FXML
    private void handleResetImage() {
        ic.setImportedImgOnCanvas(editorCanvasImage, editorImageObject, false);
        ic.setImportedImgOnCanvas(originalCanvas, originalImageObject, true);
        originalImageObject.setPreviousImage(originalImageObject.getOriginalImage());
        originalImageObject.setFilteredImage(originalImageObject.getOriginalImage());
        editorImageObject.setPreviousImage(editorImageObject.getOriginalImage());
        editorImageObject.setFilteredImage(editorImageObject.getOriginalImage());

        loadFilterOptions();
        loadEffectOptions();
    }
    private void toggleFileMenuItems(boolean setDisabled) {
        menuBarFile.getItems().stream().skip(1).forEach(menuItem -> menuItem.setDisable(setDisabled));
    }
    public void setImportedImage(Image img){
        if(ic.setImageFromGallery(img)){
            initImage();
        }
        orderStack();
    }
    /**
     * Close
     * */
    @FXML
    private void handleLogOut(){
        Dialog<ButtonType> alert = new Dialog();
        Stage dialog = (Stage)alert.getDialogPane().getScene().getWindow();
        if(GuiDriver.getIcon() != null){
            dialog.getIcons().add(GuiDriver.getIcon());
        }
        alert.getDialogPane().getButtonTypes().addAll(ButtonType.YES,ButtonType.NO,ButtonType.CANCEL);
        alert.setResultConverter(buttonType -> {
            if(buttonType == ButtonType.YES){
                return ButtonType.YES;
            }else if(buttonType == ButtonType.NO){
                return ButtonType.NO;
            }else{
                return ButtonType.CANCEL;
            }
        });
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to save before quitting?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                handleSaveExtern();
                ((Stage)stack.getScene().getWindow()).close();
            } else if (response == ButtonType.NO){
                ((Stage)stack.getScene().getWindow()).close();
            }
        });

    }



}