package org.editor.tools.imagetool;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.editor.tools.EditingTools;
import org.editor.tools.filtertool.filtercontrol.filter.FilterType;

import java.util.List;

public class ImageTool implements EditingTools {

    private ImageGrid imageGrid;
    private Image image;
    private Image filteredImage;
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    public ImageTool(Image image, Canvas canvas, GraphicsContext graphicsContext) {
        this.image = image;
        this.canvas = canvas;
        this.graphicsContext = graphicsContext;
    }

    public void createPixelArray() {
        imageGrid = new ImageGrid(image);
        this.imageGrid.setPixelArray();
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