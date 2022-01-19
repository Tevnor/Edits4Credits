package org.controller.tools.imagetool.filtercontrol;

import javafx.scene.image.*;
import org.controller.tools.imagetool.filtercontrol.filter.FilterType;

import java.nio.IntBuffer;
import java.util.List;

public class ImageGrid {
    FilterOperation fon;

    // Image dimensions
    private final int width, height;
    private final int blockWidth;
    private final int blockHeight;
    private final int panelWidth;
    private final int panelHeight;
    // Image tools
    private final WritableImage writableImage;
    private final PixelReader pixelReader;
    private final PixelWriter pixelWriter;
    private final WritablePixelFormat<IntBuffer> writablePixelFormat;
    // Image arrays
    private final int[] pixelArray;
    private int[] pixelArrayNew;

    private final int runs = 2;

    public ImageGrid(Image image) {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.writableImage = new WritableImage(width, height);
        this.pixelReader = image.getPixelReader();
        this.pixelWriter = writableImage.getPixelWriter();
        this.writablePixelFormat = WritablePixelFormat.getIntArgbInstance();
        this.pixelArray = new int[width * height];
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
    public Image processImage(List<FilterType> filterTypeList) {
        fon = new FilterOperation(this, filterTypeList);
        this.pixelArrayNew = fon.startFilter();
        writeNewPixelArray();
        return writableImage;
    }

    // Write new values to the writable image and set to image view
    public void writeNewPixelArray() {
        pixelWriter.setPixels(0, 0, width, height, writablePixelFormat, pixelArrayNew, 0, width);
    }

    /**
     * Various getters
     * */
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
    public int getRuns() { return  this.runs; }
    public int[] getPixelArray() {
        return pixelArray;
    }
}
