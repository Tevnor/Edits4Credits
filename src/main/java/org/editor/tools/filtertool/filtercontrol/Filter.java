package org.editor.tools.filtertool.filtercontrol;

import javafx.scene.paint.Color;

/**
 * The Filter interface.
 */
public interface Filter {

    /**
     * Method to be overridden by each individual filter implementation.
     *
     * @param argb   the integer argb value on the index position of the previous array
     * @param factor the factor which the operation may be manipulated with
     * @return the int argb value post-processing to be written into the new array
     */
    int applyFilter(int argb, double factor);

    /**
     * Convert JavaFX Color objects to argb int values.
     *
     * @param color the color
     * @return the int
     */
    default int convertColorToARGB(Color color) {
        int a = (int) Math.round(color.getOpacity() * 255);
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Convert arg int values to JavaFX Color objects.
     *
     * @param argb the argb
     * @return the color
     */
    default Color convertARGBtoColor(int argb) {
        int a = ((argb >> 24) & 0xff) / 255;
        int r = ((argb >> 16) & 0xff);
        int g = ((argb >> 8) & 0xff);
        int b = (argb & 0xff);

        return Color.rgb(r, g, b, a);
    }
}
