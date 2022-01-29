package org.marketplace.gallery;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GalleryController implements Initializable, ControlScreen {
    @FXML
    private MenuBar menuBar;
    @FXML
    private ImageView img0, img1,img2,img3,img4,img5,img6,img7,img8;
    @FXML
    private GridPane display, grid;
    @FXML
    private Region region;
    @FXML
    private BorderPane case0,case1,case2,case3,case4,case5,case6,case7,case8;
    private BorderPane[] cases;
    private ImageView[] displays;

    private ScreensController screensController;
    private Window window;
    public static final Logger GC_LOGGER = LogManager.getLogger(GalleryController.class);
    public static final String galleryPath = System.getProperty("user.home") + "\\.edits4credits_gallery";
    private final List<Image> imgs = new ArrayList<>();
    private final List<File> e4cFiles = new ArrayList<>();
    private int page = 0;
    private boolean atEnd = false;
    private Popup zoom = new Popup();

    @FXML
    private void handleForwards(ActionEvent e){
        if(!atEnd){
            page++;
            populateDisplays();
        }
    }
    @FXML
    private void handleBackwards(ActionEvent e){
        if(page > 0){
            page--;
            populateDisplays();
            atEnd = false;
        }
    }
    @FXML
    private void handleLoadDir(ActionEvent e){
        GC_LOGGER.debug("entered load directory");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File directory = directoryChooser.showDialog(null);

        if(directory != null){
            page = 0;
            imgs.clear();
            loadImagesDir(directory,true);
            populateDisplays();
            GC_LOGGER.debug("succesfully loaded new imgs");
        }

    }
    @FXML
    private void handleLoadOther(ActionEvent e){
        GC_LOGGER.debug("entered load multiple");
        List<File> files = getFileChooser().showOpenMultipleDialog(null);

        if(files != null && files.size() > 0){
            page = 0;
            imgs.clear();
            loadImages(files.toArray(new File[files.size()]),true);
            populateDisplays();
            GC_LOGGER.debug("succesfully loaded new imgs");
        }
    }
    @FXML
    private void handleImport(ActionEvent e) {
        GC_LOGGER.debug("entered import");
        int before = imgs.size();
        page = (imgs.size() - 1) / 9;
        List<File> files = getFileChooser().showOpenMultipleDialog(null);

        files.forEach(file -> {
            try {
                Path copy = findFileName(galleryPath, FilenameUtils.removeExtension(file.getName()),
                        FilenameUtils.getExtension(file.getName()));
                Files.copy(file.toPath(), copy, StandardCopyOption.REPLACE_EXISTING);
                e4cFiles.add(copy.toFile());
                loadImage(copy.toFile());
            } catch (IOException exception) {
                GC_LOGGER.warn("File could not be imported");
            }
        });
        int delta = imgs.size() - before;
        populateDisplays();
    }
    @FXML
    private void handlePopup(MouseEvent e){
        Image img = ((ImageView) e.getSource()).getImage();
        initPopup(img);
    }
    @FXML
    private void handleLogOut(ActionEvent e){


        ((Stage)(grid.getScene().getWindow())).close();
    }
    @FXML
    private void handleToProject(ActionEvent e){
        screensController.setScreen(ScreenName.PROJECT_SETTINGS);
    }

    /* helper */
    public boolean loadImagesDir(File dir, boolean descend){
        if(dir.exists() && dir.isDirectory()){
            return loadImages(dir.listFiles(), descend);
        }else{
            GC_LOGGER.debug("'" + dir.getName() + "' does not exist or is not a directory" );
            return false;
        }
    }
    /**
     * Loads all images from files
     * @param files File[] of to be loaded images
     * @param descend boolean if subdirectories should be loaded
     * @return true if all images are loaded successfully or files is empty - false otherwise
     */
    private boolean loadImages(File[] files, boolean descend){
        if(files != null && files.length > 0){
            boolean importOk = true;
            for(File file : files){
                if(file != null){
                    if(descend && file.isDirectory()){
                        loadImages(file.listFiles(),true);
                    }else{
                        importOk = loadImage(file);
                    }
                }
            }
            return importOk;
        }else{
            GC_LOGGER.debug("No files in selected directory/files");
            return true;
        }
    }
    /**
     * Loads an image from file
     * @param file File instance of to be loaded image
     * @return false if import is corrupted - true otherwise
     */
    private boolean loadImage(File file){
        String name = file.getName().toLowerCase();
        if(!file.isDirectory() && name.endsWith(".jpeg") || name.endsWith(".jpg") ||
                name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".tif")){
            try {
                imgs.add(SwingFXUtils.toFXImage(ImageIO.read(file),null));
                e4cFiles.add(file);
                return true;
            } catch (IOException exception) {
                GC_LOGGER.error("Could not read image from '~\\.edits4credits_gallery': " + exception.getMessage());
                return false;
            }
        }
        GC_LOGGER.debug("File is a directory or not an image");
        return true;
    }
    private void createGalleryDir(){
        File gallery = new File(galleryPath);
        if(!gallery.exists()){
            gallery.mkdir();
        }
    }
    private FileChooser getFileChooser(){
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter types = new FileChooser.ExtensionFilter("Image Types","*.jpeg", "*jpg", "*.gif", "*.png", "*.tif");
        fc.setTitle("Choose Images");
        fc.getExtensionFilters().add(types);
        return fc;
    }
    private Path findFileName(final String dir, final String baseName, final String extension) {
        Path ret = Paths.get(dir,String.format("%s.%s",baseName,extension));
        if (!Files.exists(ret))
            return ret;

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String newName = String.format("%s(%d).%s", baseName, i, extension);
            ret = Paths.get(dir, newName);
            if (!Files.exists(ret))
                GC_LOGGER.debug(baseName + '.' + extension + " renamed to: " + newName);
                return ret;
        }
        throw new IllegalStateException("What the...");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displays = new ImageView[]{img0,img1,img2,img3,img4,img5,img6,img7,img8};
        cases = new BorderPane[]{case0,case1,case2,case3,case4,case5,case6,case7,case8};
    }
    @Override
    public void setScreenParent(ScreensController screenPage)  {
        this.screensController = screenPage;
    }
    @Override
    public void setWindow(Window window)  {
        this.window = window;
    }


    /* layout */
    public void init(){
        setLayout();
        initCases();
        initDisplays();
        createGalleryDir();
        loadImagesDir(new File(galleryPath),true);
        populateDisplays();
    }
    private void setLayout(){
        grid.setPrefWidth(window.getScreenWidth());
        grid.setPrefHeight(window.getScreenHeight());
        display.setPrefHeight(window.getScreenHeight() - menuBar.getPrefHeight() - 75);
        display.setPrefWidth(display.getPrefHeight() * window.getScreenRatio());
        display.getColumnConstraints().forEach(col -> col.setPrefWidth(display.getPrefWidth()/3));
        display.getRowConstraints().forEach(row -> row.setPrefHeight(display.getPrefHeight()/3));
        region.setPrefWidth(grid.getPrefWidth());
        region.setPrefHeight(grid.getPrefHeight());
//        region.setStyle("-fx-background-color: rgba(0,0,0,1)");
    }
    private void initDisplays(){
        for(int i = 0; i < displays.length; i++){
            int row = i / 3;
            int col = i % 3;
            displays[i].setFitWidth(display.getColumnConstraints().get(col).getPrefWidth()-50);
            displays[i].setFitHeight(display.getRowConstraints().get(row).getPrefHeight()-50);
        }
    }
    private void initCases(){
        for(int i = 0; i < cases.length; i++){
            int row = i / 3;
            int col = i % 3;
            cases[i].setPrefWidth(display.getColumnConstraints().get(col).getPrefWidth()-15);
            cases[i].setPrefHeight(display.getRowConstraints().get(row).getPrefHeight()-15);
        }
    }
    private void initPopup(Image img){
        if(!zoom.isShowing()){
            BorderPane bp = new BorderPane();
            zoom = new Popup();

            bp.setCenter(getPopView(img));
            bp.setBottom(getButton());
            region.setVisible(true);
            zoom.getContent().addAll(bp);
            zoom.setConsumeAutoHidingEvents(true);
            zoom.setAutoHide(true);
            zoom.centerOnScreen();
            zoom.setOnHidden(event -> {
                region.setVisible(false);
            });
            zoom.show(grid.getScene().getWindow());
        }
    }
    private Button getButton(){
        Button btn = new Button();
        btn.setText("Use");
        btn.setStyle("-fx-background-color: #282828;" +
                "-fx-text-fill: #eeeeee;" +
                "-fx-font-family: \"Segoe UI Semibold\";" +
                "-fx-font-size: 30;" +
                "-fx-background-radius: 10;");
        BorderPane.setMargin(btn, new Insets(20,0,0,0));
        BorderPane.setAlignment(btn,Pos.CENTER);
        return btn;
    }
    private VBox getPopView(Image img){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(vBox, Pos.CENTER);
        vBox.setStyle("-fx-background-color: #282828;" +
                "-fx-background-radius: 10px");
        ImageView view = new ImageView(img);
        view.setPreserveRatio(true);
        view.setFitHeight(window.getScreenHeight()/window.getScaleY());
        view.setFitWidth(window.getScreenWidth()/window.getScaleX());
        vBox.getChildren().add(view);
        VBox.setMargin(view, new Insets(15));
        return vBox;
    }
    private void populateDisplays(){
        int imgCount = (page*9);
        int imgSize = imgs.size();

        for(ImageView view : displays){
            if(imgCount < imgSize){
                view.setImage(imgs.get(imgCount));
            }else{
                view.setImage(null);
                atEnd = true;
            }
            imgCount++;
        }
    }

}
