package org.marketplace.gallery;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
import org.editor.EditorController;
import org.editor.project.SettingsController;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class GalleryController implements Initializable, ControlScreen {
    public static final Logger GC_LOGGER = LogManager.getLogger(GalleryController.class);
    public static final String galleryPath = System.getProperty("user.home") + "\\.edits4credits_gallery";

    @FXML
    private MenuItem toProject, toEditor;
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
    private FutureTask<Boolean> loadImgs = new FutureTask(() -> loadImagesDir(new File(galleryPath),true, true));

    private final Map<Image,File> imgTree = new HashMap<>();
    private int page = 0;
    private boolean atEnd = false, fromEditor = false, delete = false;
    private Popup zoom = new Popup();
    private Image selImg = null;

    @FXML
    private void handleForwards(){
        if(!atEnd){
            page++;
            populateDisplays();
        }
    }
    @FXML
    private void handleBackwards(){
        if(page > 0){
            page--;
            populateDisplays();
            atEnd = false;
        }
    }
    @FXML
    private void handleFirst(){
        page = 0;
        populateDisplays();
    }
    @FXML
    private void handleLast(){
        page = (imgTree.size() - 1) / 9;
        populateDisplays();
    }
    @FXML
    private void handleLoadDir(){
        GC_LOGGER.debug("entered load directory");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File directory = directoryChooser.showDialog(null);

        if(directory != null){
            page = (imgTree.size() - 1) / 9;
            loadImagesDir(directory,true, false);
            populateDisplays();
            GC_LOGGER.debug("succesfully loaded new imgs");
        }

    }
    @FXML
    private void handleLoadOther(){
        GC_LOGGER.debug("entered import multiple");
        List<File> files = getFileChooser().showOpenMultipleDialog(null);
        files.forEach(file -> {
            try {
                Path copy = findFileName(galleryPath, FilenameUtils.removeExtension(file.getName()),
                        FilenameUtils.getExtension(file.getName()));
                Files.copy(file.toPath(), copy, StandardCopyOption.REPLACE_EXISTING);
                loadImage(copy.toFile(), false);
            } catch (IOException exception) {
                GC_LOGGER.warn("File could not be imported");
            }
        });
        populateDisplays();
    }
    @FXML
    private void handlePopup(MouseEvent e){
        Image img = ((ImageView) e.getSource()).getImage();
        if(img != null){
            getPopup(delete, img);
        }
    }
    @FXML
    private void handleLogOut(){
        ((Stage)(grid.getScene().getWindow())).close();
    }
    @FXML
    private void handleToProject(){
        screensController.setScreen(ScreenName.PROJECT_SETTINGS);
    }
    @FXML
    private void handleToEditor(){
        screensController.setScreen(ScreenName.EDITOR);
    }
    @FXML
    private void handleUse(){
        this.delete = false;
    }
    @FXML
    private void handleDelete(){
        this.delete = true;
    }

    /* helper */
    /**
     * Loads all images from a directory
     * @param dir File as directory of to be loaded images
     * @param descend boolean if subdirectories should be loaded
     * @return true if all images are loaded successfully or files is empty - false otherwise
     */
    public boolean loadImagesDir(File dir, boolean descend, boolean init){
        try {
            if(dir.exists() && dir.isDirectory() && dir.listFiles().length > 0){
                return loadImages(dir.listFiles(), descend, init);
            }else{
                GC_LOGGER.debug("'" + dir.getName() + "' does not exist or is not a directory" );
                return false;
            }
        }catch (SecurityException e){
            GC_LOGGER.warn("You have no access to dir: '" + dir.getPath() + "'" );
            return false;
        }

    }
    /**
     * Loads all images from files
     * @param files File[] of to be loaded images
     * @param descend boolean if subdirectories should be loaded
     * @return true if all images are loaded successfully or files is empty - false otherwise
     */
    private boolean loadImages(File[] files, boolean descend, boolean init){
        if(files != null && files.length > 0){
            boolean importOk = true;
            for(File file : files){
                if(file != null){
                    if(descend && file.isDirectory()){
                        try{
                            loadImages(file.listFiles(),true, init);
                        }catch (SecurityException e){
                            GC_LOGGER.warn("You have no access to sub-dir: '" + file.getPath() + "'");
                        }
                    }else{
                        importOk = loadImage(file, init);
                    }
                }
            }
            return importOk;
        }else{
            GC_LOGGER.warn("No files in selected directory/files");
            return true;
        }
    }
    /**
     * Loads an image from file
     * @param file File instance of to be loaded image
     * @return true if image is imported successfully - false otherwise
     */
    private boolean loadImage(File file, boolean init){
        File img = file;
        if(!init){
            img = copyToGalleryDir(file);
        }
        if(img != null){
            try {
                Image i = SwingFXUtils.toFXImage(ImageIO.read(img),null);
                imgTree.put(i,img);
                GC_LOGGER.debug("Image read successfully");
                return true;
            } catch (IOException exception) {
                GC_LOGGER.warn("Could not read image from '~\\.edits4credits_gallery\\" + img.getName());
                return false;
            }
        }
        return false;
    }
    private File copyToGalleryDir(File file){
        String ex = FilenameUtils.getExtension(file.getName()).toLowerCase();
        if(ex.equals("jpeg") || ex.equals("jpg") ||
                ex.equals("gif") || ex.equals("png") || ex.equals("tif")) {
            Path copy = findFileName(galleryPath, FilenameUtils.removeExtension(file.getName()),
                    FilenameUtils.getExtension(file.getName()));
            try {
                Files.copy(file.toPath(), copy, StandardCopyOption.REPLACE_EXISTING);
                return copy.toFile();
            }catch (IOException e){
                GC_LOGGER.warn("Copying file to '~\\.edits4credits_gallery\\" + file.getName()  + "' failed");
                return null;
            }
        }
        GC_LOGGER.debug("File is not an image");
        return null;
    }
    private void createGalleryDir(){
        File gallery = new File(galleryPath);
        if(!gallery.exists()){
            gallery.mkdir();
        }
    }
    /**
     * Creates a FileChooses object fitted to saving an Image
     * @return initialized FileChooser
     */
    private FileChooser getFileChooser(){
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter types = new FileChooser.ExtensionFilter("Image Types","*.jpeg", "*jpg", "*.gif", "*.png", "*.tif");
        fc.setTitle("Choose Images");
        fc.getExtensionFilters().add(types);
        return fc;
    }
    /**
     * Checks if file name is already used and if yes creates path with added (number) at the end of the file name
     * @param dir directory of file
     * @param baseName file name without extension
     * @param extension file extension e.g. "png"
     * @return (new) target Path (if file already exists)
     */
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
    private boolean deleteImage(Image img){
        try{
            if(imgTree.get(img).delete()){
                imgTree.remove(img);
                populateDisplays();
                return true;
            }
        }catch (SecurityException e){
            GC_LOGGER.warn("Could not delete image, cause: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displays = new ImageView[]{img0,img1,img2,img3,img4,img5,img6,img7,img8};
        cases = new BorderPane[]{case0,case1,case2,case3,case4,case5,case6,case7,case8};
    }
    public void setScreenParent(ScreensController screenPage)  {
        this.screensController = screenPage;
    }
    public void setWindow(Window window)  {
        this.window = window;
    }
    public void init(){
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.submit(loadImgs);
        ex.shutdown();
        setLayout();
        initCases();
        initDisplays();
        createGalleryDir();
        //loadImagesDir(new File(galleryPath),true, true);
    }

    /* layout */
    public boolean setOpen(boolean fromEditor){
        if(loadIsDone()){
            this.fromEditor = fromEditor;
            if(fromEditor){
                toProject.setVisible(false);
                toEditor.setVisible(true);
            }else{
                toProject.setVisible(true);
                toEditor.setVisible(false);
            }
            populateDisplays();
            return true;
        }
        return false;
    }
    private void setLayout(){
        grid.setPrefWidth(window.getScreenWidth());
        grid.setPrefHeight(window.getScreenHeight());
        display.setPrefHeight(window.getScreenHeight() - grid.getRowConstraints().get(0).getPrefHeight() - grid.getRowConstraints().get(2).getPrefHeight());
        display.setPrefWidth(display.getPrefHeight() * window.getScreenRatio());
        display.getColumnConstraints().forEach(col -> col.setPrefWidth(display.getPrefWidth()/3));
        display.getRowConstraints().forEach(row -> row.setPrefHeight(display.getPrefHeight()/3));
        region.setPrefWidth(grid.getPrefWidth());
        region.setPrefHeight(grid.getPrefHeight());
    }
    private void initDisplays(){
        for(int i = 0; i < displays.length; i++){
            int row = i / 3;
            int col = i % 3;
            displays[i].setFitWidth(display.getColumnConstraints().get(col).getPrefWidth()-30);
            displays[i].setFitHeight(display.getRowConstraints().get(row).getPrefHeight()-30 / window.getScreenRatio());
        }
    }
    private void initCases(){
        for(int i = 0; i < cases.length; i++){
            int row = i / 3;
            int col = i % 3;
            cases[i].setPrefWidth(display.getColumnConstraints().get(col).getPrefWidth());
            cases[i].setPrefHeight(display.getRowConstraints().get(row).getPrefHeight());
        }
    }
    private void getPopup(boolean delete, Image img){
        if(!zoom.isShowing()){
            selImg = img;
            BorderPane bp = new BorderPane();
            zoom = new Popup();
            bp.setBottom(getButton(delete,img));
            bp.setCenter(getPopView(img));

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
    private Button getButton(boolean delete,Image img){
        Button btn = new Button();
        if(delete) {
            btn.setText("Delete");
            btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Image permanently?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteImage(img);
                    }
                });
                zoom.hide();
            });
        }else {
            btn.setText("Use");
            if (fromEditor) {
                btn.setOnAction(event -> {
                    zoom.hide();
                    ((EditorController) screensController.getController(ScreenName.EDITOR))
                            .setImportedImage(selImg);
                    screensController.setScreen(ScreenName.EDITOR);
                });
            } else {
                btn.setOnAction(event -> {
                    ((SettingsController) screensController.getController(ScreenName.PROJECT_SETTINGS))
                            .setBaseImage(selImg);
                    screensController.setScreen(ScreenName.PROJECT_SETTINGS);
                });
            }
        }
        btn.setStyle("-fx-background-color: #282828;" +
                "-fx-text-fill: #eeeeee;" +
                "-fx-font-family: \"Segoe UI Semibold\";" +
                "-fx-font-size: 30;" +
                "-fx-background-radius: 10;");
        BorderPane.setMargin(btn, new Insets(20));
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
        view.setFitHeight(window.getScreenHeight()*3/5);
        view.setFitWidth(window.getScreenWidth()*3/5);
        GC_LOGGER.debug(view.getFitWidth() + " " + view.getFitHeight());
        vBox.getChildren().add(view);
        VBox.setMargin(view, new Insets(15));
        return vBox;
    }
    private void populateDisplays(){
        int imgCount = (page*9);
        int imgSize = imgTree.size();
        List<Image> imgs = List.copyOf(imgTree.keySet());
        for(ImageView view : displays){
            if(imgCount < imgSize){
                view.setImage(imgs.get(imgCount));
                view.getParent().setVisible(true);
            }else{
                view.setImage(null);
                view.getParent().setVisible(false);
                atEnd = true;
            }
            imgCount++;
        }
    }

    public boolean loadIsDone(){
        return loadImgs.isDone();
    }
    public boolean loadedCorrectly(){
        try {
            return loadImgs.get();
        } catch (Exception e) {
           GC_LOGGER.error("Error on loading images");
           return false;
        }
    }
}
