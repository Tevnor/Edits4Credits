package org.controller.tools.filtertool.filtercontrol;

import javafx.scene.image.*;
import org.controller.tools.filtertool.filtercontrol.filterapplications.CheckerboardOld;

import java.nio.IntBuffer;

public class ImageGridOld {
    private final ImageView imageView;
    private final WritableImage writableImage;
    private final PixelReader pixelReader;
    private final PixelWriter pixelWriter;
    private final WritablePixelFormat<IntBuffer> writablePixelFormat;
    private final int width, height;
    private final int[] pixelArray;
    private int[] pixelArrayNew;

    private int factor = 5;
    private final int runs = 1;

    private int blockWidth;
    private int blockHeight;
    private int panelWidth;
    private int panelHeight;


    private CheckerboardOld checkerboardOld;
    private boolean silhouette;
    private boolean complement;

    public ImageGridOld(Image image) {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.imageView = new ImageView(image);
        this.writableImage = new WritableImage(width, height);
        this.pixelReader = image.getPixelReader();
        this.pixelWriter = writableImage.getPixelWriter();
        this.writablePixelFormat = WritablePixelFormat.getIntArgbInstance();
        this.pixelArray = new int[width * height];
//        this.filterTypesList = filterTypesList;
        this.pixelArrayNew = new int[pixelArray.length];
        this.blockWidth = width / runs;
        this.blockHeight = height / runs;
        this.panelWidth = width / (runs * 2);
        this.panelHeight = height / (runs * 2);
    }

    /**
     * For initial read/write of pixel array
     * */
    public void setPixelArray() {
        this.pixelReader.getPixels(0, 0, width, height, writablePixelFormat, pixelArray, 0, width);
    }

    /**
     * Sequence for actual filter application
     * */

    public Image retrieveImage() {
        return writableImage;
    }

    /**
     * Various getter and setter methods
     * */

    public void setPixelArrayNew() {
        checkerboardOld = new CheckerboardOld(this);
        this.pixelArrayNew = this.checkerboardOld.applyCheckerBoard();
    }

    // Write new values to the writable image and set to image view
    public void writeNewPixelArray() {
        pixelWriter.setPixels(0, 0, width, height, writablePixelFormat, pixelArrayNew, 0, width);
    }


    public ImageView getImageView() {
        return imageView;
    }

    // Overwritten integer array of argb values

    public int[] getPixelArrayNew() {
        return this.pixelArrayNew;
    }

    public WritablePixelFormat<IntBuffer> getWritablePixelFormat() {
        return writablePixelFormat;
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public int getBlockWidth() { return this.blockWidth; }
    public int getBlockHeight() { return this.blockHeight; }
    public int getPanelWidth() { return this.panelWidth; }
    public int getPanelHeight() { return this.panelHeight; }
    // Original integer array of argb values
    public int[] getPixelArray() {
        return pixelArray;
    }
    public int getFactor() { return this.factor; }
    public PixelWriter getPixelWriter() {
        return this.pixelWriter;
    }
}
