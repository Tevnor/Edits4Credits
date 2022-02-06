package org.editor.tools.filterTool;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoiseControllerTest {
    private NoiseController tester;
    private WritableImage testImage;
    private WritableImage emptyTestImage;
    JFXPanel jfxPanel = new JFXPanel();

    @Test
    public void addNoiseToImage() {
        tester = new NoiseController();
        Image image = new Image("https://www.chip.de/ii/4/7/2/8/5/5/4/f8c3bf084e08658b.jpg");
        testImage = clone(image);

        assertEquals("First Pixel is the same after applying noise.", true,firstPixelColor(testImage) != firstPixelColor(tester.addNoiseToImage(testImage,5)));
        assertFalse("Doesn't return image.", tester.addNoiseToImage(testImage,5) == null);
        assertNotNull("Image is empty", tester.addNoiseToImage(testImage, 5));
    }

    @Test
    public void addNoiseToImageException() {
        tester = new NoiseController();

        try {
            tester.addNoiseToImage(emptyTestImage, 5);
            fail("Image was empty");
        }
        catch (Exception e) {
            assertNotNull(e);
        }
    }



    public static WritableImage clone(Image image) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        WritableImage writableImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        final PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }
        return writableImage;
    }

    public Color firstPixelColor(Image image) {
        Color firstPixelColor = new Color(0,0,0,0);

        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        WritableImage writableImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        final PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 1; x++) {
                firstPixelColor = pixelReader.getColor(x, y);
            }
        }
        return firstPixelColor;
    }




}