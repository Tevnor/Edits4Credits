package org.editor.tools.imagetool;

import javafx.scene.image.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.IntBuffer;

/**
 * ImageGrid objects handle the conversion of Images into integer arrays of pixels and vice versa.
 * @see WritableImage
 * @see PixelReader
 * @see PixelWriter
 * @see WritablePixelFormat
 * */
public class ImageGrid {

    private static final Logger IG_LOGGER = LogManager.getLogger(ImageGrid.class);

    private final int width;
    private final int height;
    private final WritableImage writableImage;
    private final PixelReader pixelReader;
    private final PixelWriter pixelWriter;
    private final WritablePixelFormat<IntBuffer> writablePixelFormat;
    private final int[] pixelArray;

    public ImageGrid(Image image) {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.writableImage = new WritableImage(width, height);
        this.pixelReader = image.getPixelReader();
        this.pixelWriter = writableImage.getPixelWriter();
        this.writablePixelFormat = WritablePixelFormat.getIntArgbInstance();
        this.pixelArray = new int[width * height];

        IG_LOGGER.debug("New ImageGrid instantiated.");
    }

    /**
     * For initial read/write of pixel array
     * */
    public void readPixelsIntoArray() {
        this.pixelReader.getPixels(0, 0, width, height, writablePixelFormat, pixelArray, 0, width);
        IG_LOGGER.debug("Pixel array created with length of: " + pixelArray.length);
    }

    /**
     * Write new pixel array onto image
     * @param pixelArrayNew the array of the pixels after processing
     * @return the newly overwritten WriteableImage
     * */
    public WritableImage writeNewPixelArray(int[] pixelArrayNew) {
        pixelWriter.setPixels(0, 0, width, height, writablePixelFormat, pixelArrayNew, 0, width);

        IG_LOGGER.debug("Finished writing pixel array into image.");
        return writableImage;
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
    public int[] getPixelArray() {
        return pixelArray;
    }
}
