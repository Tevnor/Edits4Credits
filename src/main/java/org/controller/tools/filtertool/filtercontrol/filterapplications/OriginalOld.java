package org.controller.tools.filtertool.filtercontrol.filterapplications;

import org.controller.tools.filtertool.filtercontrol.FilterOperationOld;
import org.controller.tools.filtertool.filtercontrol.ImageGridOld;

public class OriginalOld extends FilterOperationOld implements Runnable {

    public OriginalOld(int index, ImageGridOld imageGridOld, int panelRow, int panelColumn, int[] pixelArrayNew) {
        super(index, imageGridOld, panelRow, panelColumn, pixelArrayNew);
    }

    public int applyFilter(int argb, double factor) {

        // Returning the original ARGB value

        return argb;
    }
}
