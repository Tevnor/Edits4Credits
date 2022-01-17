package org.controller.tools.filtertool.filtercontrol.filterapplications;

import org.controller.tools.filtertool.filtercontrol.FilterOperation;

public class Original extends FilterOperation implements Runnable {

    public Original(int index, int width, int height, int panelRow, int panelColumn, int panelWidth, int panelHeight, double factor, int[] pixelArray, int[] pixelArrayNew) {
        super(index, width, height, panelRow, panelColumn, panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
    }

    @Override
    public int applyFilter(int argb, double factor) {

        // Returning the original ARGB value

        return argb;
    }
}
