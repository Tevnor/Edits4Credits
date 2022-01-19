package org.controller.tools.filtertool;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import org.controller.tools.EditingTools;
import org.controller.tools.filtertool.filtercontrol.ImageGridOld;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class FilterToolOld implements EditingTools {

    private ImageGridOld imageGridOld;
    private final Image image;
    private Canvas canvas;
    private final GraphicsContext graphicsContext;
    private StackPane stackPane;


    public FilterToolOld(Image image, Canvas canvas, StackPane stackPane) {
        this.image = image;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.stackPane = stackPane;
    }


    // Read and write pixel argb values into array
    public void createPixelArray() {
        imageGridOld = new ImageGridOld(image);
        this.imageGridOld.setPixelArray();
    }

    public Canvas applyFilterSelection() {
        Image img = getResultingImage();
        saveToFile(img);
        graphicsContext.drawImage(img, 0,0);

        return canvas;
    }

    public Image getResultingImage() {
        this.imageGridOld.setPixelArrayNew();
        this.imageGridOld.writeNewPixelArray();

        return this.imageGridOld.retrieveImage();
    }

    public static void saveToFile(Image writableImage) {
        try {
            File outputFile = new File("savedimage.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void apply() {

    }

    @Override
    public void backward() {

    }

    @Override
    public void forward() {

    }
}
