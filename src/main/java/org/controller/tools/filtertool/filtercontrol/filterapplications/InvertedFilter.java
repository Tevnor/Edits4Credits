package org.controller.tools.filtertool.filtercontrol.filterapplications;

import javafx.scene.paint.Color;
import org.controller.tools.filtertool.filtercontrol.FilterOperation;


public class InvertedFilter extends FilterOperation implements Runnable {

    public InvertedFilter(int index, int width, int height, int panelRow, int panelColumn, int panelWidth, int panelHeight, double factor, int[] pixelArray, int[] pixelArrayNew) {
        super(index, width, height, panelRow, panelColumn, panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
    }

    // Define inverted filter
    @Override
    public int applyFilter(int argb, double factor) {

        Color color = convertARGBtoColor(argb).invert();

        return convertColorToARGB(color);
    }
}
