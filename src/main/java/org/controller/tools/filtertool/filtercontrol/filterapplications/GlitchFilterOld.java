package org.controller.tools.filtertool.filtercontrol.filterapplications;

import org.controller.tools.filtertool.filtercontrol.FilterOperationOld;
import org.controller.tools.filtertool.filtercontrol.ImageGridOld;

public class GlitchFilterOld extends FilterOperationOld implements Runnable {

    public GlitchFilterOld(int index, ImageGridOld imageGridOld, int panelRow, int panelColumn, int[] pixelArrayNew) {
        super(index, imageGridOld, panelRow, panelColumn, pixelArrayNew);
    }

    // Define glitch filter
    @Override
    public int applyFilter(int argb, double factor) {
        // Return "glitched" ARGB value
        return (int) ((argb / 1.0222) * factor);
    }
}
