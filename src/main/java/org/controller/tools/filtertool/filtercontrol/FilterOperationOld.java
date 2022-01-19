package org.controller.tools.filtertool.filtercontrol;

import javafx.scene.paint.Color;

public abstract class FilterOperationOld implements Runnable {

    public enum Type{
        ORIGINAL,
        GLITCH,
        GREYSCALE,
        INVERTED
    }

    private Thread thread;
    private final int index;
    private final int width;
    private final int panelWidth;
    private final int panelHeight;
    private final int panelRow;
    private final int panelColumn;
    private final int[] pixelArray;
    private int[] pixelArrayNew;

    public FilterOperationOld(int index, ImageGridOld imageGridOld, int panelRow, int panelColumn, int[] pixelArrayNew) {
        this.index = index;
        this.width = imageGridOld.getWidth();
        this.panelWidth = imageGridOld.getPanelWidth();
        this.panelHeight = imageGridOld.getPanelHeight();
        this.panelRow = panelRow;
        this.panelColumn = panelColumn;
        this.pixelArray = imageGridOld.getPixelArray();
        this.pixelArrayNew = pixelArrayNew;
    }

    // Apply version of filter
    public abstract int applyFilter(int argb, double factor);

    // Apply version of filter on given section
    public void applyOnSection(int i, int width, int panelRow, int panelColumn, int panelWidth, int panelHeight, double factor, int[] pixelArray, int[] pixelArrayNew) {
        int yIndex = i;
        for (int yy = 0; yy < panelHeight; panelRow++) {
            for (int xx = 0; xx < panelWidth; panelColumn++) {
                int argb = applyFilter(pixelArray[i], factor);
                pixelArrayNew[i] = argb;
                i++;
                xx++;
            }
            yIndex += width;
            i = yIndex;
            yy++;
        }
    }

//    public void applyOnWholeImage() {
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                int argb = applyFilter(pixelArray[index], factor);
//                pixelArrayNew[index] = argb;
//            }
//        }
//    }

    public int convertColorToARGB(Color color) {

        int a = (int) Math.round(color.getOpacity() * 255);
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public Color convertARGBtoColor(int argb) {

        int a = ((argb >> 24) & 0xff) / 255;
        int r = ((argb >> 16) & 0xff);
        int g = ((argb >> 8) & 0xff);
        int b = (argb & 0xff);

        return Color.rgb(r, g, b, a);
    }


    // For multithreading
    public void run() {
//        applyOnSection(index, width, panelRow, panelColumn, panelWidth, panelHeight, pixelArray, pixelArrayNew);
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public int getWidth() {
        return width;
    }
}
