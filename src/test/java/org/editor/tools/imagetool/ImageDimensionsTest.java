package org.editor.tools.imagetool;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageDimensionsTest {
    private ImageDimensions tester;
    private Image testImage;
    private Image testImageZwei;
    private Image testImageDrei;
    private Image emptyImage;
    JFXPanel jfxPanel = new JFXPanel();


    @Test
    public void getImageAspectRatioTest(){
        testImage = new Image("https://www.chip.de/ii/4/7/2/8/5/5/4/f8c3bf084e08658b.jpg");
        testImageZwei = new Image("https://via.placeholder.com/1080x1080/eee?text=1:1");
        testImageDrei = new Image("https://i.pinimg.com/originals/33/08/8a/33088a684f8af83e6b6be61828190a7b.jpg");

        tester = new ImageDimensions(testImage, 0,0, 1920, 1080);
        assertEquals("Image with aspect ratio greater than 1 didn't match", (int) 16/9, (int) tester.getImageAspectRatio(testImage) );
        assertEquals("Image with aspect ratio of 1 didn't match", (int) 1, (int) tester.getImageAspectRatio(testImageZwei) );
        assertEquals("Image with aspect ratio smaller than 1 didn't match", (int) 675/1200, (int) tester.getImageAspectRatio(testImageDrei) );

        assertFalse("Doesn't return number bigger than 0", tester.getImageAspectRatio(testImage) <= 0);

        assertNotNull("Method doesn't return aspect ratio", tester.getImageAspectRatio(testImage));

        try {
            tester.getImageAspectRatio(emptyImage);
            fail("Image was empty");
        }
        catch (Exception e) {
            assertNotNull(e);
        }

    }

    @Test
    public void getResizedImageHeightTest(){
        tester = new ImageDimensions(testImage, 0,0, 1920, 1080);

        assertEquals("Resized height didn't match", (int) 3000, (int) tester.getResizedImageHeight(1500,0.5));
        assertEquals("Resized height didn't match", (int) 1651, (int) tester.getResizedImageHeight(2477,1.5));
        assertEquals("Resized height didn't match", (int) -4000, (int) tester.getResizedImageHeight(-3000,0.75));

        assertFalse("Returns wrong resized height", (int) tester.getResizedImageHeight(1500,0.5) != 3000);
        assertFalse("Returns wrong resized height", (int) tester.getResizedImageHeight(2477,1.5) != 1651);
        assertFalse("Returns wrong resized height", (int) tester.getResizedImageHeight(-3000,0.75) != -4000);

        assertFalse("Dividing by 0 isn't infinite", Double.isFinite(tester.getResizedImageHeight(1500, 0)));
        assertTrue("Dividing by 0 is finite", Double.isInfinite(tester.getResizedImageHeight(1500, 0)));

        assertNotNull("Returns no height", tester.getResizedImageHeight(1000,1));

    }

}