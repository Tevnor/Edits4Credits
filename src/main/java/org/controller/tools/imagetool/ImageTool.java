package org.controller.tools.imagetool;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.controller.tools.EditingTools;
import org.controller.tools.imagetool.filtercontrol.ImageGrid;
import org.controller.tools.imagetool.filtercontrol.filter.FilterType;

import java.util.List;

public class ImageTool implements EditingTools {

    private ImageGrid imageGrid;
    private final Image image;
    private Image filteredImage;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    public ImageTool(Image image, Canvas canvas, GraphicsContext graphicsContext) {
        this.image = image;
        this.canvas = canvas;
        this.graphicsContext = graphicsContext;
    }

    public void createPixelArray() {
        imageGrid = new ImageGrid(image);
        this.imageGrid.setPixelArray();
    }

    public void startProcess(List<FilterType> filterTypeList) {
        filteredImage = this.imageGrid.processImage(maximizeList(filterTypeList));
        graphicsContext.drawImage(filteredImage, 0, 0);
//        saveToFile(imageNew);
    }
    public Image getFilteredImage(){
        return filteredImage;
    }

    public List<FilterType> maximizeList(List<FilterType> filterTypeList) {
        FilterType filterTypeEnumOne;
        FilterType filterTypeEnumTwo;

        switch (filterTypeList.size()) {
            case 1:
                filterTypeEnumOne = filterTypeList.get(0);
                filterTypeList.add(filterTypeEnumOne);
                filterTypeList.add(filterTypeEnumOne);
                filterTypeList.add(filterTypeEnumOne);
                return filterTypeList;
            case 2:
                filterTypeEnumOne = filterTypeList.get(0);
                filterTypeEnumTwo = filterTypeList.get(1);
                filterTypeList.add(filterTypeEnumTwo);
                filterTypeList.add(filterTypeEnumOne);
                return filterTypeList;
            default:
                return filterTypeList;
        }
    }

//    public void saveToFile(Image writableImage) {
//        try {
//            File outputFile = new File("savedImage.png");
//            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
//            ImageIO.write(bufferedImage, "png", outputFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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
