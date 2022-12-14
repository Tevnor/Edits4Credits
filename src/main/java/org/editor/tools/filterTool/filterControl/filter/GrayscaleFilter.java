package org.editor.tools.filterTool.filterControl.filter;

import javafx.scene.paint.Color;
import org.editor.tools.filterTool.filterControl.Filter;

public class GrayscaleFilter implements Filter {
    @Override
    public int applyFilter(int argb, double factor) {
        // Retrieving the color of the pixel of the loaded image
        Color color = convertARGBtoColor(argb).grayscale();

        // Return greyscale ARGB value
        return convertColorToARGB(color);
    }
}
