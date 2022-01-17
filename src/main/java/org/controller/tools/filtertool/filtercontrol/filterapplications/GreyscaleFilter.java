package org.controller.tools.filtertool.filtercontrol.filterapplications;

import javafx.scene.paint.Color;
import org.controller.tools.filtertool.filtercontrol.FilterOperation;


public class GreyscaleFilter extends FilterOperation implements Runnable {
    public GreyscaleFilter(int index, int width, int height, int panelRow, int panelColumn, int panelWidth, int panelHeight, double factor, int[] pixelArray, int[] pixelArrayNew) {
        super(index, width, height, panelRow, panelColumn, panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
    }

    // Define greyscale filter
    @Override
    public int applyFilter(int argb, double factor) {
        // Retrieving the color of the pixel of the loaded image
        Color color = convertARGBtoColor(argb).grayscale();

        // Return greyscale ARGB value
        return convertColorToARGB(color);
    }
}
