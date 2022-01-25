package org.editor;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import org.editor.tools.imagetool.ImageTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImportControl {
    private static final Logger IC_LOGGER = LogManager.getLogger(ImportControl.class.getName());

    private Image importedImg;
    private Image fitToEditor;

    /*
        import image
    */
    private Image getFileChooserImage(){
        FileChooser chooser = new FileChooser();
        File f = chooser.showOpenDialog(null);
        try{
            return new Image(f.getPath());
        }catch (Exception e){
            IC_LOGGER.error("Exception thrown on image import:" + e.getMessage());
        }
        return null;
    }
    public boolean setImageFromExplorer(){
        importedImg = getFileChooserImage();
        if(importedImg != null){
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

    /*
    initilizing main canvases and images
    */
    public ImageDimensions getImageDimInstance(){
        return new ImageDimensions(importedImg,0,0,importedImg.getWidth(),importedImg.getHeight());
    }
    public Canvas getEditorImageCanvas(StackPane stack){
        Canvas canvas = new Canvas(stack.getPrefWidth(),stack.getPrefHeight());
        return canvas;
    }
    public Canvas getOriginalImageCanvas(Project project){
        return new Canvas(project.getProjectWidth(),project.getProjectHeight());
    }
    /**
     * Draws imported Image fitted to the canvas size.
     * Updates given ImageDimensions.
     * @param canvas {@link Canvas} object on which the imported image should be drawn upon
     * @param imgDims {@link ImageDimensions} object of to be drawn image
     */
    public void setImportedImgOnCanvas(Canvas canvas, ImageDimensions imgDims, boolean isOriginal){
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
        IC_LOGGER.debug("Drawed imported image with dims: x= " + resizedImage.getWidth() + " y= " + resizedImage.getHeight() + " ratio= " + ratio);
        if(!isOriginal){
            fitToEditor = resizedImage;
            IC_LOGGER.trace("Set fitToEditor to: " + resizedImage);
        }
    }
    public ImageTool getImageTool(Canvas editorCanvas){
        if(fitToEditor != null){
            ImageTool imgTool = new ImageTool(fitToEditor, editorCanvas, editorCanvas.getGraphicsContext2D());
            imgTool.createPixelArray();
            return imgTool;
        }
        IC_LOGGER.error("Could not create ImageTool because this.fitToEditorImg is null");
        return null;
    }
    public ImageTool getImageTool(Canvas editorCanvas,Image filtered){
        ImageTool imgTool = new ImageTool(filtered, editorCanvas, editorCanvas.getGraphicsContext2D());
        imgTool.createPixelArray();
        return imgTool;
    }

    /*
        saving
    */
    public void save(Project project, Canvas finishedOriginal, int[] drawingBuffer){
        saveToFile(getFinalImage(project,drawingBuffer,getOriginalBuffer(finishedOriginal)));
    }
    private void saveToFile(Image writableImage) {
        try {
            File outputFile = new File("drawing.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public WritableImage getFinalImage(Project project, int[] drawingBuffer, int[] imageBuffer){
        int projectWidth = (int)project.getProjectWidth(), projectHeight = (int)project.getProjectHeight();
        WritableImage export = new WritableImage(projectWidth,projectHeight);
        PixelWriter pw = export.getPixelWriter();
        if(!project.isTransparent()) {
            for (int i = 0; i < projectHeight * projectWidth; i++) {
                pw.setColor(i % projectWidth, i / projectWidth, project.getBackgroundColor());
            }
        }
        for(int i  = 0; i < drawingBuffer.length ; i++){
            if(imageBuffer[i] != 0 || drawingBuffer[i] != 0){
                pw.setArgb(i % projectWidth, i / projectWidth, srcOverArgb(drawingBuffer[i], imageBuffer[i]));
            }
        }
        return export;
    }
    private int[] getOriginalBuffer(Canvas canvas){
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        int[] export = new int[(int)(canvas.getWidth()*canvas.getHeight())];

        canvas.snapshot(sp,null).getPixelReader()
                .getPixels(0,0,(int)canvas.getWidth(),(int)canvas.getHeight(),
                        WritablePixelFormat.getIntArgbInstance(), export, 0, (int)canvas.getWidth());
        return export;
    }
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
