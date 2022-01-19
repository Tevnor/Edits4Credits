package org.controller.tools.imagetool.filtercontrol;

import javafx.scene.paint.Color;

public interface Filter {

    enum FilterTypeEnum {
        ORIGINAL,
        GLITCH,
        GRAYSCALE,
        INVERTED
    }

    int applyFilter(int argb, double factor);

    default int convertColorToARGB(Color color) {
        int a = (int) Math.round(color.getOpacity() * 255);
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    default Color convertARGBtoColor(int argb) {
        int a = ((argb >> 24) & 0xff) / 255;
        int r = ((argb >> 16) & 0xff);
        int g = ((argb >> 8) & 0xff);
        int b = (argb & 0xff);

        return Color.rgb(r, g, b, a);
    }
}
