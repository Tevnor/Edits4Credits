package org.editor.tools.filterTool.filterControl.filter;

import javafx.scene.paint.Color;
import org.editor.tools.filterTool.filterControl.Filter;

public class NoiseFilter implements Filter {
    @Override
    public int applyFilter(int argb, double noiseStrength) {
        Color color = convertARGBtoColor(argb);

        double noise = Math.random() / (noiseStrength * 100);

        Color newColor = new Color(Math.min(color.getRed() + noise, 1),
                Math.min(color.getGreen() + noise, 1),
                Math.min(color.getBlue() + noise, 1),
                1);

        // Return ARGB value of the new color
        return convertColorToARGB(newColor);
    }
}