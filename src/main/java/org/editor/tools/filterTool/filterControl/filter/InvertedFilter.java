package org.editor.tools.filterTool.filterControl.filter;

import javafx.scene.paint.Color;
import org.editor.tools.filterTool.filterControl.Filter;

public class InvertedFilter implements Filter {
    @Override
    public int applyFilter(int argb, double factor) {
        // Getting the inverted color of the pixel of the loaded image
        Color color = convertARGBtoColor(argb).invert();

        // Return greyscale ARGB value
        return convertColorToARGB(color);
    }
}
