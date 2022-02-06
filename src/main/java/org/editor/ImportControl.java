package org.editor;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.project.Project;
import org.editor.tools.imagetool.ImageDimensions;
import org.apache.commons.io.FilenameUtils;
import org.marketplace.gallery.GalleryController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;

public class ImportControl {
    private static final Logger IC_LOGGER = LogManager.getLogger(ImportControl.class);
    private static final String galleryPath = GalleryController.galleryPath;

    private Image importedImg;
    private Image fitToEditor;
    private boolean isSet = false;

    /*  file manipulation   */
    private FileChooser getChooser(String name, boolean isOpen){
        FileChooser chooser = new FileChooser();
        if(isOpen){
            FileChooser.ExtensionFilter all = new FileChooser.ExtensionFilter("Image Types","*.jpg","*.jpeg","*.gif","*.png","*.tif");
            chooser.getExtensionFilters().add(all);
        }else{
            FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPG","*.jpg");
            FileChooser.ExtensionFilter jpeg = new FileChooser.ExtensionFilter("JPEG","*.jpeg");
            FileChooser.ExtensionFilter gif = new FileChooser.ExtensionFilter("GIF","*.gif");
            FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG","*.png");
            FileChooser.ExtensionFilter tif = new FileChooser.ExtensionFilter("TIF","*.tif");
            chooser.getExtensionFilters().addAll(jpg,jpeg,gif,png,tif);
        }
        if(name != null){
            chooser.setInitialFileName(name);
        }
        chooser.setTitle("Choose image");
        return chooser;
    }
    private void createGalleryDir(){
        File gallery = new File(galleryPath);
        if(!gallery.exists()){
            gallery.mkdir();
            IC_LOGGER.debug("Gallery directory created");
        }
    }
    protected void reset(){
        importedImg = null;
        fitToEditor = null;
        isSet = false;
        IC_LOGGER.debug("ImportControl reset()");
    }

    /*  import image    */
    private Image getFileChooserImage(){
        FileChooser chooser = getChooser(null, true);
        File f = chooser.showOpenDialog(null);
        try{
            return new Image(f.getPath());
        }catch (Exception e){
            IC_LOGGER.error("Exception thrown on image import:" + e.getMessage());
        }
        return null;
    }
    protected boolean setImageFromExplorer(){
        importedImg = getFileChooserImage();
        if(importedImg != null && !isSet){
            isSet = true;
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Could not find or open chosen file. Please try again.");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    IC_LOGGER.warn("Pressed ok");
                }
            });
            return false;
        }

    }
    protected boolean setImageFromGallery(Image image){
        if(image != null && !isSet){
            importedImg = image;
            isSet = true;
            return true;
        }
        return false;
    }

    /*  saving  */
    protected void save(Project project, Canvas finishedOriginal, int[] drawingBuffer, boolean toGallery){
        if(toGallery){
            ChoiceDialog<String> c = new ChoiceDialog(".png", ".jpg", ".gif",".png", ".tif");
            c.setTitle("Select Image Type");
            c.setHeaderText("Please select the image type to export.");
            c.setResultConverter( ( ButtonType type ) -> {
                ButtonBar.ButtonData data = type == null ? null : type.getButtonData();
                if ( data == ButtonBar.ButtonData.OK_DONE ) {
                    return "OK";
                } else {
                    return "CANCEL";
                }
            } );
            Optional<String> result = c.showAndWait();
            if(result.get().equals("OK")){
                saveToGallery(getFinalImage(project,drawingBuffer,getOriginalBuffer(finishedOriginal)),project,c.getSelectedItem());
            }else{
                IC_LOGGER.warn("No file type chose -> cancel save");
            }
        }else{
            saveToExtern(getFinalImage(project,drawingBuffer,getOriginalBuffer(finishedOriginal)),project);
        }
    }
    private void saveToExtern(Image writableImage, Project project) {
        FileChooser chooser = getChooser(project.getProjectName(), false);
        File file = chooser.showSaveDialog(null);

        try {
            writeImage(writableImage,file);
        } catch (Exception exception) {
            IC_LOGGER.error("Could not save image!: " + exception.getMessage());
            Alert cannotSave = new Alert(Alert.AlertType.ERROR);
            cannotSave.setHeaderText("Could not save image at chosen location. Please try again!");
        }
    }
    private void saveToGallery(Image writableImage, Project project, String extension) {
        createGalleryDir();
        File file = new File(galleryPath + '\\' + project.getProjectName() + extension);
        try {
            writeImage(writableImage,file);
        } catch (Exception exception) {
            IC_LOGGER.error("Could not save image!: " + exception.getMessage());
            Alert cannotSave = new Alert(Alert.AlertType.ERROR);
            cannotSave.setHeaderText("Could not save image at chosen location. Please try again!");
        }
    }

    private void writeImage(Image img, File file) throws IOException {
        String ext = FilenameUtils.getExtension(file.toString());
        BufferedImage outImg;
        if(ext.equals("jpg")||ext.equals("jpeg")){
            BufferedImage bi = SwingFXUtils.fromFXImage(img, null);
            outImg = new BufferedImage((int)img.getWidth(),(int)img.getHeight(),BufferedImage.TYPE_INT_RGB);
            outImg.getGraphics().drawImage(bi,0,0,java.awt.Color.WHITE,null);
        }else{
            outImg = SwingFXUtils.fromFXImage(img, null);
        }
        IC_LOGGER.debug("Writing image with ext: "+ext+" to: " + file);
        ImageIO.write(outImg,ext,file);
    }

    /*  initializing main canvases and images    */
    protected ImageDimensions getImageDimInstance(){
        return new ImageDimensions(importedImg,0,0,importedImg.getWidth(),importedImg.getHeight());
    }
    protected Canvas getEditorImageCanvas(StackPane stack){
        Canvas canvas = new Canvas(stack.getPrefWidth(),stack.getPrefHeight());
        return canvas;
    }
    protected Canvas getOriginalImageCanvas(Project project){
        return new Canvas(project.getProjectWidth(),project.getProjectHeight());
    }
    /**
     * Draws imported Image fitted to the canvas size.
     * Updates given ImageDimensions.
     * @param canvas {@link Canvas} object on which the imported image should be drawn upon
     * @param imgDims {@link ImageDimensions} object of to be drawn image
     */
    protected void setImportedImgOnCanvas(Canvas canvas, ImageDimensions imgDims, boolean isOriginal){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double ratio = imgDims.getImageAspectRatio(importedImg);
        Image resizedImage;
        int editorCanvasImageWidth = (int) canvas.getWidth();
        int editorCanvasImageHeight = (int) canvas.getHeight();

        // Instantiate resized image from imported image
        if (ratio >= 1){
            resizedImage = imgDims.scaleImage(importedImg, editorCanvasImageWidth, imgDims.getResizedImageHeight(editorCanvasImageWidth, ratio), true, true);
            if(resizedImage.getHeight() > editorCanvasImageHeight){
                resizedImage = imgDims.scaleImage(importedImg, imgDims.getResizedImageWidth(editorCanvasImageHeight, ratio), editorCanvasImageHeight, true, true);
            }
        } else {
            resizedImage = imgDims.scaleImage(importedImg, imgDims.getResizedImageWidth(editorCanvasImageHeight, ratio), editorCanvasImageHeight, true, true);
            if(resizedImage.getWidth() > editorCanvasImageWidth){
                resizedImage = imgDims.scaleImage(importedImg, editorCanvasImageWidth, imgDims.getResizedImageHeight(editorCanvasImageWidth,ratio), true, true);
            }
        }

        imgDims.setCurrentWidth(resizedImage.getWidth());
        imgDims.setCurrentHeight(resizedImage.getHeight());
        // Draw resized image onto editorCanvasImage
        gc.drawImage(resizedImage,0, 0, resizedImage.getWidth(), resizedImage.getHeight());
        IC_LOGGER.debug("Drew imported image with dims: x= " + resizedImage.getWidth() + " y= " + resizedImage.getHeight() + " ratio= " + ratio);
        if(!isOriginal){
            fitToEditor = resizedImage;
            IC_LOGGER.debug("Set fitToEditor to: " + resizedImage);
        }
    }

    /*  pixel manipulation  */
    private WritableImage getFinalImage(Project project, int[] drawingBuffer, int[] imageBuffer){
        int projectWidth = (int)project.getProjectWidth(), projectHeight = (int)project.getProjectHeight(),
            length = projectWidth * projectHeight;
        WritableImage export = new WritableImage(projectWidth,projectHeight);
        PixelWriter pw = export.getPixelWriter();

        IntStream range = IntStream.range(0,length).parallel();
        if(project.isTransparent()) {
            if(imageBuffer == null){
                range.forEach(i -> pw.setArgb(i % projectWidth, i / projectWidth, drawingBuffer[i]));
                IC_LOGGER.debug("final image: drawing layer");
            }else{
                range.forEach(i -> pw.setArgb(i % projectWidth, i / projectWidth, srcOverArgb(drawingBuffer[i], imageBuffer[i])));
                IC_LOGGER.debug("final image: image layer < drawing layer");
            }
        }else{
            if(imageBuffer == null){
                range.forEach(i -> pw.setArgb(i % projectWidth, i / projectWidth,
                        srcOverArgb(drawingBuffer[i], convertColor(project.getBackgroundColor()))));
                IC_LOGGER.debug("final image: background layer < drawing layer");
            }else{
                range.forEach(i -> pw.setArgb(i % projectWidth, i / projectWidth,
                        srcOverArgb(drawingBuffer[i], srcOverArgb(imageBuffer[i],convertColor(project.getBackgroundColor())))));
                IC_LOGGER.debug("final image: background layer < image layer < drawing layer");
            }
        }
        return export;
    }
    private int[] getOriginalBuffer(Canvas canvas){
        if(canvas == null){
            return null;
        }
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        int[] export = new int[(int)(canvas.getWidth()*canvas.getHeight())];

        canvas.snapshot(sp,null).getPixelReader()
                .getPixels(0,0,(int)canvas.getWidth(),(int)canvas.getHeight(),
                        WritablePixelFormat.getIntArgbInstance(), export, 0, (int)canvas.getWidth());
        return export;
    }
    private int convertColor(Color color){
        int alpha = (int)Math.round(color.getOpacity() * 255);
        int red = (int)Math.round(color.getRed() * 255);
        int green = (int)Math.round(color.getGreen() * 255);
        int blue = (int)Math.round(color.getBlue() * 255);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
    /**
     * Calculates an INT_ARGB of the SRC_OVER algorithm for two pixels
     * @param argb_fg int_argb value of foreground pixel
     * @param argb_bg int_argb value of background pixel
     * @return src_over value of input
     */
    private int srcOverArgb(int argb_fg, int argb_bg){
        //conversion into 0-1 multiplied by alpha value
        double alpha_fg = (double)((argb_fg >> 24) & 0xff)/255, alpha_bg = (double)((argb_bg >> 24) & 0xff)/255;
        double red_fg_a   = (double)((argb_fg >> 16) & 0xff)/255 * alpha_fg, red_bg_a   = (double)((argb_bg >> 16) & 0xff)/255 * alpha_bg;
        double green_fg_a = (double)((argb_fg >>  8) & 0xff)/255 * alpha_fg, green_bg_a = (double)((argb_bg >>  8) & 0xff)/255 * alpha_bg;
        double blue_fg_a  = (double)((argb_fg) & 0xff)/255 * alpha_fg, blue_bg_a  = (double)((argb_bg) & 0xff)/255 * alpha_bg;
        //src over algorithm
        double alpha_f = alpha_bg + alpha_fg - alpha_bg * alpha_fg;
        double red_final_a = (red_fg_a + red_bg_a * (1-alpha_fg)) / alpha_f;
        double blue_final_a = (blue_fg_a + blue_bg_a * (1-alpha_fg)) / alpha_f;
        double green_final_a = (green_fg_a + green_bg_a * (1-alpha_fg)) / alpha_f;
        //conversion back into 255
        int red_final = (int)(red_final_a * 255);
        int blue_final = (int)(blue_final_a * 255);
        int green_final = (int)(green_final_a * 255);
        int alpha_final = (int)(alpha_f * 255);
        //bitwise addition into INT_ARGB format
        return (alpha_final << 24) | ((red_final << 16) & 0xffffff) | ((green_final << 8) & 0xffff) | (blue_final  & 0xff);
    }
}
