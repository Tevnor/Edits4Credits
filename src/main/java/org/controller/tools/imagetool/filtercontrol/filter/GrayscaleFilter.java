package org.controller.tools.imagetool.filtercontrol.filter;

import javafx.scene.paint.Color;
import org.controller.tools.imagetool.filtercontrol.Filter;

public class GrayscaleFilter implements Filter {
    @Override
    public int applyFilter(int argb, double factor) {
        // Retrieving the color of the pixel of the loaded image
        Color color = convertARGBtoColor(argb).grayscale();

        // Return greyscale ARGB value
        return convertColorToARGB(color);
    }
}
