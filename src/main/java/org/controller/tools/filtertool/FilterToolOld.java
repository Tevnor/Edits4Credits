//package org.controller.tools.filtertool;
//
//import javafx.embed.swing.SwingFXUtils;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.image.Image;
//import javafx.scene.layout.StackPane;
//import org.controller.tools.EditingTools;
//import org.controller.tools.filtertool.filtercontrol.ImageGrid;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//
//public class FilterTool implements EditingTools {
//
//    private ImageGrid imageGrid;
//    private Image image;
//    private Canvas canvas;
//    private GraphicsContext graphicsContext;
//    private StackPane stackPane;
//
//
//    public FilterTool(Image image, Canvas canvas, StackPane stackPane) {
//        this.image = image;
//        this.canvas = canvas;
//        this.graphicsContext = canvas.getGraphicsContext2D();
//        this.stackPane = stackPane;
//    }
//
//
//    // Read and write pixel argb values into array
//    public void stepOne() {
//        imageGrid = new ImageGrid(image);
//        this.imageGrid.setPixelArray();
//    }
//
//    public Canvas selectCheckerboard() {
//        Image img = getResultingImage();
//        saveToFile(img);
//        graphicsContext.drawImage(img, 0,0);
//
//        return canvas;
//    }
//
//    public Image getResultingImage() {
//        this.imageGrid.setPixelArrayNew();
//        this.imageGrid.writeNewPixelArray();
//
//        return this.imageGrid.retrieveImage();
//    }
//
//    public static void saveToFile(Image writableImage) {
//        try {
//            File outputFile = new File("savedimage.png");
//            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
//            ImageIO.write(bufferedImage, "png", outputFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public Canvas getCanvas() {
//        return canvas;
//    }
//
//    public void setCanvas(Canvas canvas) {
//        this.canvas = canvas;
//    }
//
//    @Override
//    public void apply() {
//
//    }
//
//    @Override
//    public void backward() {
//
//    }
//
//    @Override
//    public void forward() {
//
//    }
//}
