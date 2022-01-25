package org.editor.project;

import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class Project {
//    private String projectName;
    private final int projectWidth;
    private final int projectHeight;
    private final double projectAspectRatio;
    private final boolean isTransparent;
    private ImageView bgTransparent;
    private Rectangle bgColor;
    private Color backgroundColor;
    //private String backgroundColor;


    public Project(int projectWidth, int projectHeight) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = (double) projectWidth / projectHeight;
        this.isTransparent = true;
        this.bgTransparent = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/transparent_bg.png"))));
        bgTransparent.setPreserveRatio(false);
    }
    public Project(int projectWidth, int projectHeight, Color backgroundColor) {
        this.projectWidth = projectWidth;
        this.projectHeight = projectHeight;
        this.projectAspectRatio = (double) projectWidth / projectHeight;
        this.isTransparent = false;
        this.backgroundColor = backgroundColor;
        this.bgColor = new Rectangle(projectWidth,projectHeight,backgroundColor);
    }


    public boolean isTransparent(){
        return this.isTransparent;
    }
    public ImageView getTransparent() {
        return bgTransparent;
    }
    public double getProjectWidth() {
        return projectWidth;
    }
    public double getProjectHeight() {
        return projectHeight;
    }
    public double getProjectAspectRatio() {return projectAspectRatio;}
    public Node getBackground(){
        if(isTransparent){
            return bgTransparent;
        }
        return bgColor;
    }

    public WritableImage getFinalImage(int[] drawingBuffer, int[] imageBuffer){
        WritableImage export = new WritableImage(projectWidth,projectHeight);
        PixelWriter pw = export.getPixelWriter();
        if(!isTransparent) {
            for (int y = 0; y < projectHeight; y++) {
                for (int x = 0; x < projectWidth; x++) {
                    pw.setColor(x, y, backgroundColor);
                }
            }
        }

        long start = System.nanoTime();
        for(int i  = 0; i < drawingBuffer.length ; i++){
            if(imageBuffer[i] != 0 || drawingBuffer[i] != 0){
                pw.setArgb(i % projectWidth, i / projectWidth, srcOverArgb(drawingBuffer[i], imageBuffer[i]));
            }
        }
        System.out.println(System.nanoTime()-start);

/*
        IntStream.range(0, imageBuffer.length)
                .filter(i -> imageBuffer[i] != 0 || drawingBuffer[i] != 0)
                .forEach(i -> {
                    pw.setArgb(i % projectWidth, i / projectWidth, srcOverArgb(drawingBuffer[i], imageBuffer[i]));
                });
*/

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
