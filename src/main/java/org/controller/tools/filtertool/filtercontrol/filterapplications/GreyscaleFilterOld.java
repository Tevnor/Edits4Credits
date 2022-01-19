package org.controller.tools.filtertool.filtercontrol.filterapplications;

import javafx.scene.paint.Color;
import org.controller.tools.filtertool.filtercontrol.FilterOperationOld;
import org.controller.tools.filtertool.filtercontrol.ImageGridOld;


public class GreyscaleFilterOld extends FilterOperationOld implements Runnable {
    public GreyscaleFilterOld(int index, ImageGridOld imageGridOld, int panelRow, int panelColumn, int[] pixelArrayNew) {
        super(index, imageGridOld, panelRow, panelColumn, pixelArrayNew);
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
