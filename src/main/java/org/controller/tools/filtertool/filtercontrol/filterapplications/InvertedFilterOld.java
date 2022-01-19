package org.controller.tools.filtertool.filtercontrol.filterapplications;

import javafx.scene.paint.Color;
import org.controller.tools.filtertool.filtercontrol.FilterOperationOld;
import org.controller.tools.filtertool.filtercontrol.ImageGridOld;


public class InvertedFilterOld extends FilterOperationOld implements Runnable {

    public InvertedFilterOld(int index, ImageGridOld imageGridOld, int panelRow, int panelColumn, int[] pixelArrayNew) {
        super(index, imageGridOld, panelRow, panelColumn, pixelArrayNew);
    }

    // Define inverted filter
    @Override
    public int applyFilter(int argb, double factor) {

        Color color = convertARGBtoColor(argb).invert();

        return convertColorToARGB(color);
    }
}
