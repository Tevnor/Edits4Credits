package org.controller;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.WritableImage;
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
import org.controller.tools.drawingtool.graphiccontrol.handlers.HandlerFactory;
import org.controller.tools.drawingtool.graphiccontrol.handlers.PolygonDrawer;
import org.controller.tools.drawingtool.graphiccontrol.objects.Shapes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import org.controller.tools.imagetool.ImageTool;
import org.controller.tools.imagetool.filtercontrol.Filter;
import org.controller.tools.imagetool.filtercontrol.FilterOperation;
import org.controller.NoiseController;

import javax.imageio.ImageIO;

public class EditorController implements Initializable, ControlScreen {

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
    private Parent noiseOptRoot;
    private DrawOptionsController options;
    private NoiseController noiseController;
    private Stage drawOptStage = new Stage();
    private Stage noiseOptStage = new Stage();

    ScreensController screensController;
    Window window;
    private double screenWidth;
    private double screenHeight;
    private double canvasWidth;
    private double canvasHeight;

    private Image resizedImage;
    private Image originalImage;
    private Image filteredImage;

    private ImageTool imageTool;
    List<Filter.FilterTypeEnum> filterTypeEnumList;

    private Parent moveOptRoot;
    private FXMLLoader moveOptLoader;
    private PositionOptionsController moveOptions;
    private double xPosition = 0;
    private double yPosition = 0;
    private double currentImageHeight;
    private double currentImageWidth;

    private GraphicsContext gc;
    private Parent scaleOptRoot;
    private FXMLLoader scaleOptLoader;
    private ScaleOptionsController scaleOptions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        initDrawOptions();
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
        this.dt = new DrawingTool(stack);
        handlerFactory = new HandlerFactory(dt);
        this.canvasWidth = project.getProjectWidth();
        this.canvasHeight = project.getProjectHeight();
        this.importButton.toFront();
    }

    public void setWidthHeightAspectRatio(Project project){
        this.width = project.getProjectWidth();
        this.height = project.getProjectHeight();
        this.projectAspectRatio = project.getProjectAspectRatio();
    }

    public void handleArc(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions(Shapes.Type.ARC);
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
        openDrawOptions(Shapes.Type.CIRCLE);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.CIRCLE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleEllipses(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions(Shapes.Type.ELLIPSES);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.ELLIPSES, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);

    }
    public void handleRectangle(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions(Shapes.Type.RECTANGLE);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.RECTANGLE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleRoundedRectangle(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions(Shapes.Type.ROUNDED_RECT);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.ROUNDED_RECTANGLE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleLine(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions(Shapes.Type.LINE);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.LINE, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleText(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions(Shapes.Type.TEXT);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.TEXT, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handlePolygon(ActionEvent e){
        stack.removeEventHandler(MouseEvent.ANY, mover);
        stack.removeEventHandler(MouseEvent.ANY, drawer);
        openDrawOptions(Shapes.Type.POLYGON);
        drawer = handlerFactory.getHandler(HandlerFactory.Handler.POLYGON, options.getAttributes());
        stack.addEventHandler(MouseEvent.ANY,drawer);
    }
    public void handleDrawPolygon(ActionEvent e){
        if(drawer instanceof PolygonDrawer){
            ((PolygonDrawer) drawer).drawPolygon();
        }
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
    public void openDrawOptions(Shapes.Type type){
        drawOptStage.centerOnScreen();
        options.setSelShape(type);
        drawOptStage.show();
    }

    public void initDrawOptions(){
        try{
            FXMLLoader drawOptLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/drawOptions.fxml")));
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

    /**
     * Filter
     * */
    public void initEnumToFilterMap() {
        FilterOperation.EnumToFilterMap enumMap = new FilterOperation.EnumToFilterMap();
        enumMap.createEnumMap();
    }

    public void initAddNoiseOpt() {
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

    public void handleAddNoise(ActionEvent event) {
        noiseOptStage.centerOnScreen();
        noiseOptStage.show();
    }

    public WritableImage createImageFromCanvas(Canvas canvas){
        WritableImage filterImage = editorCanvasImage.snapshot(null, null);
        return filterImage;
    }

    // Apply the checkerboard filter to the filterTool object that was instantiated at setImportedImage()
    public void handleApplyCheckerboard(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(Filter.FilterTypeEnum.ORIGINAL);
        filterTypeEnumList.add(Filter.FilterTypeEnum.GLITCH);
        filterTypeEnumList.add(Filter.FilterTypeEnum.INVERTED);
        filterTypeEnumList.add(Filter.FilterTypeEnum.GRAYSCALE);

        startImageTool();
    }

    public void handleAddGlitch(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(Filter.FilterTypeEnum.GLITCH);

        startImageTool();
    }
    public void handleAddInverse(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(Filter.FilterTypeEnum.INVERTED);

        startImageTool();
    }
    public void handleAddGrayscale(ActionEvent event) {
        filterTypeEnumList = new ArrayList<>();
        filterTypeEnumList.add(Filter.FilterTypeEnum.GRAYSCALE);

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
    @FXML
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

    public void handleImportButton(ActionEvent event) throws IOException {
        importImageFromExplorer();
    }

    public void importImageFromExplorer(){

        // opens file explore to choose image to edit
        FileChooser chooser = new FileChooser();
        File f = chooser.showOpenDialog(null);

        // saves file path from image to file object
        imagePath = f;

        //creates new image from the selected path
        Image fileChooserImage = new Image(f.getPath());
        setImageToCanvas(fileChooserImage);
    }

    public void setImageToCanvas(Image image) {
        setOriginalImage(image);

        // Pass image to setter method
        setEditorImageCanvas();
        setImportedImage(image);
    }

    public double getMenuBarHeight(){
        return menuBar.getPrefHeight();
    }
    public double getToolBarWidth(){
        return toolBar.getPrefWidth();
    }


    // method to calculate stack pane size
    public void setStackPane() {

        // define the maximum size for the stack pane based on the monitor size
        double maxStackHeight = screenHeight - getMenuBarHeight();
        double maxStackWidth = screenWidth - getToolBarWidth();

        // when the aspect ratio is greater than 1 calculate based on the width
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
        // when the aspect ratio is smaller than 1 calculate based on height
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

    // create empty canvas for images with the size of the stack pane
    public void setEditorImageCanvas(){
        editorCanvasImage = new Canvas(stack.getPrefWidth(), stack.getPrefHeight());
        stack.getChildren().add(editorCanvasImage);

        stack.setAlignment(editorCanvasImage, Pos.CENTER);
        editorCanvasImage.toBack();
    }

    public void setFilteredImage(){
        filteredImage = noiseController.getFilteredImage();
//        gc = editorCanvasImage.getGraphicsContext2D();
        gc.drawImage(filteredImage,0, 0, filteredImage.getWidth(), filteredImage.getHeight());

        initImageTool(filteredImage);
    }

    // draw selected image to the image canvas
    public void setImportedImage(Image importedImage) {
        gc = editorCanvasImage.getGraphicsContext2D();

        double ratio = getImageAspectRatio(importedImage);
        int editorCanvasImageWidth = (int) editorCanvasImage.getWidth();
        int editorCanvasImageHeight = (int) editorCanvasImage.getHeight();

        // Instantiate resized image from imported image
        if (ratio >= 1){
            resizedImage = scaleImage(importedImage, editorCanvasImageWidth, getResizedImageHeight(editorCanvasImageHeight, ratio), true, true);
            if(resizedImage.getHeight() > stack.getPrefHeight()){
                resizedImage = scaleImage(importedImage, getResizedImageWidth(editorCanvasImageWidth, ratio), editorCanvasImageHeight, true, true);
            }
        } else {
            resizedImage = scaleImage(importedImage, getResizedImageWidth(editorCanvasImageWidth, ratio), editorCanvasImageHeight, true, true);
        }

        this.currentImageHeight = resizedImage.getHeight();
        this.currentImageWidth = resizedImage.getWidth();
        // Draw resized image onto editorCanvasImage
        gc.drawImage(resizedImage,0, 0, resizedImage.getWidth(), resizedImage.getHeight());


        //disables import button if image was imported
        if (imagePath != null) {
            importButton.setDisable(true);
            importButton.setVisible(false);
            //openFile.setDisable(true);
        }

        // Instantiate ImageTool Object
        initImageTool(resizedImage);
        initEnumToFilterMap();
    }

    public void initImageTool(Image img) {
        // Create pixel array from resized image to enhance performance of future references
        this.imageTool = new ImageTool(img, editorCanvasImage, gc);
        this.imageTool.createPixelArray();
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
    public void setChangedPosition(double xPosition, double yPosition, double currentImageWidth, double currentImageHeight){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(originalImage, xPosition, yPosition, currentImageWidth,currentImageHeight);
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
    public double getScaledHeight(double scaleFactor) {
        double scaledHeight = currentImageHeight * scaleFactor /100;
        return scaledHeight;
    }
    public double getScaledWidth(double scaleFactor) {
        double scaledWidth = currentImageWidth * scaleFactor /100;
        return scaledWidth;
    }
    public void drawScaledImage(Image sourceImage, double xPosition, double yPosition, double scaledWidth, double scaledHeight){
        GraphicsContext gc = editorCanvasImage.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvasImage.getWidth(), editorCanvasImage.getHeight());
        gc.drawImage(sourceImage, xPosition, yPosition, scaledWidth, scaledHeight);
    }
    public Image getOriginalImage(){
        return originalImage;
    }
    public double getXPosition(){
        return xPosition;
    }
    public double getYPosition(){
        return yPosition;
    }
    public void setCurrentImageHeight(double height){
        this.currentImageHeight = height;
    }
    public void setCurrentImageWidth(double width) {
        this.currentImageWidth = width;
    }
    public void setOriginalImage(Image image){
        this.originalImage = image;
    }
    public double getCurrentImageWidth(){
        return this.currentImageWidth;
    }
    public double getCurrentImageHeight(){
        return this.currentImageHeight;
    }

    /**
     * File
     * */
    public void handleOpenFile(ActionEvent event) {
        importImageFromExplorer();
    }

    public void handleDeleteFile(ActionEvent event) {
    }

    public void handleFileSettings(ActionEvent event) {
    }

    public void handleSaveFile(ActionEvent event) {
        saveToFile(filteredImage);
    }
    public static void saveToFile(Image writableImage) {
        try {
            File outputFile = new File("savedImage.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
