package org.editor.tools.filtertool;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.editor.tools.filtertool.filtercontrol.FilterInputAttributes;
import org.editor.tools.imagetool.ImageGrid;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class FilterOperationTest {
    private final JFXPanel panel = new JFXPanel();
    private int width;
    private int panelsOnAxis;
    private FilterOperation filterOperation;


    @Before
    public void setUp() {
        Image image = new Image("https://tvseriesfinale.com/wp-content/uploads/2019/06/i-think-you-should-leave-590x332.jpeg");
        ImageGrid imageGrid = new ImageGrid(image);
        FilterInputAttributes filterInputAttributes = new FilterInputAttributes();
        filterOperation = new FilterOperation(imageGrid, filterInputAttributes);
        width = (int) image.getWidth();
        panelsOnAxis = 8;
    }

    @Test
    public void panelWidthFromImage_Valid() {
        Assertions.assertEquals(74, filterOperation.calculatePanelDimension(width, panelsOnAxis));
    }

    @Test
    public void panelWidthFromImage_Invalid(){
        Assertions.assertNotEquals(73, filterOperation.calculatePanelDimension(width, panelsOnAxis));
    }

    @Test
    public void panelWidthFromVariable_Valid(){
        int width = 29929;
        int panelsOnAxis = 16;
        Assertions.assertEquals(1871, filterOperation.calculatePanelDimension(width, panelsOnAxis));
    }

    @Test
    public void panelWidthFromVariable_Invalid(){
        int width = 200;
        int panelsOnAxis = 32;
        Assertions.assertNotEquals(7, filterOperation.calculatePanelDimension(width, panelsOnAxis));
    }

    @Test
    public void lastPanelWidthFromImageValid() {
        int panelWidth = filterOperation.calculatePanelDimension(width, panelsOnAxis);
        Assertions.assertEquals(72, filterOperation.calculatePanelDimensionLast(width, panelWidth, panelsOnAxis));
    }

    @Test
    public void lastPanelWidthFromImage_Invalid() {
        int panelWidth = filterOperation.calculatePanelDimension(width, panelsOnAxis);
        Assertions.assertNotEquals(73, filterOperation.calculatePanelDimensionLast(width, panelWidth, panelsOnAxis));
    }

    @Test
    public void lastPanelFromVariable_Valid() {
        int width = 938123;
        int panelsOnAxis = 16;
        int panelWidth = filterOperation.calculatePanelDimension(width, panelsOnAxis);

        Assertions.assertEquals(58628, filterOperation.calculatePanelDimensionLast(width, panelWidth, panelsOnAxis));
    }

    @Test
    public void lastPanelWidthFromVariable_Invalid() {
        int width = 1143411;
        int panelsOnAxis = 64;
        int panelWidth = filterOperation.calculatePanelDimension(width, panelsOnAxis);
        Assertions.assertNotEquals(17854, filterOperation.calculatePanelDimensionLast(width, panelWidth, panelsOnAxis));
    }
}