package org.controller.tools.filtertool.filtercontrol.filterapplications;

import org.controller.tools.filtertool.filtercontrol.FilterOperation;

public class GlitchFilter extends FilterOperation implements Runnable {

    public GlitchFilter(int index, int width, int height, int panelRow, int panelColumn, int panelWidth, int panelHeight, double factor, int[] pixelArray, int[] pixelArrayNew) {
        super(index, width, height, panelRow, panelColumn, panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
    }

    // Define glitch filter
    @Override
    public int applyFilter(int argb, double factor) {
        // Return "glitched" ARGB value
        return (int) ((argb / 1.0222) * factor);
    }
}
