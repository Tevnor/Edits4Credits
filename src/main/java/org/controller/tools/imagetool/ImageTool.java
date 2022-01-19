package org.controller.tools.imagetool;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.controller.tools.EditingTools;
import org.controller.tools.imagetool.filtercontrol.Filter;
import org.controller.tools.imagetool.filtercontrol.ImageGrid;
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

    public void startProcess(List<Filter.FilterTypeEnum> filterTypeEnumList) {
        filteredImage = this.imageGrid.processImage(maximizeList(filterTypeEnumList));
        graphicsContext.drawImage(filteredImage, 0, 0);
//        saveToFile(imageNew);
    }
    public Image getFilteredImage(){
        return filteredImage;
    }

    public List<Filter.FilterTypeEnum> maximizeList(List<Filter.FilterTypeEnum> filterTypeEnumList) {
        Filter.FilterTypeEnum filterTypeEnumOne;
        Filter.FilterTypeEnum filterTypeEnumTwo;

        switch (filterTypeEnumList.size()) {
            case 1:
                filterTypeEnumOne = filterTypeEnumList.get(0);
                filterTypeEnumList.add(filterTypeEnumOne);
                filterTypeEnumList.add(filterTypeEnumOne);
                filterTypeEnumList.add(filterTypeEnumOne);
                return filterTypeEnumList;
            case 2:
                filterTypeEnumOne = filterTypeEnumList.get(0);
                filterTypeEnumTwo = filterTypeEnumList.get(1);
                filterTypeEnumList.add(filterTypeEnumTwo);
                filterTypeEnumList.add(filterTypeEnumOne);
                return filterTypeEnumList;
            default:
                return filterTypeEnumList;
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
