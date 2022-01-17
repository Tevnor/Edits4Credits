package org.controller.tools.filtertool.filtercontrol.filterapplications;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import org.controller.tools.filtertool.filtercontrol.ImageGrid;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Checkerboard {

    private final ImageGrid imageGrid;
    private final PixelWriter pixelWriter;
    private final WritablePixelFormat<IntBuffer> writablePixelFormat;
    private final int width;
    private final int height;
    private final int blockWidth;
    private final int blockHeight;
    private final int panelWidth;
    private final int panelHeight;
    private final int factor = 5;
    private final int[] pixelArray;
    private int[] pixelArrayNew;

    private int runs = 2;

    public Checkerboard(ImageGrid imageGrid) {
        this.imageGrid = imageGrid;
        this.pixelWriter = imageGrid.getPixelWriter();
        this.writablePixelFormat = imageGrid.getWritablePixelFormat();
        this.pixelArray = imageGrid.getPixelArray();
        this.pixelArrayNew = new int[pixelArray.length];
        this.width = imageGrid.getWidth();
        this.height = imageGrid.getHeight();
        this.blockWidth = imageGrid.getWidth() / runs;
        this.blockHeight = imageGrid.getHeight() / runs;
        this.panelWidth = imageGrid.getWidth() / (runs * 2);
        this.panelHeight = imageGrid.getHeight() / (runs * 2);
    }

    public int[] chooseCheckerboard() {
        this.pixelArrayNew = applyCheckerBoard();
        return pixelArrayNew;
    }

    public int[] applyCheckerBoard() {
        // Index of the image's array of pixels
        int index = 0;

        int panelRow = 0;
        int panelColumn = 0;

        for (int blockRow = 0; blockRow < runs; blockRow++) {

            // Manage thread status with ExecutorService
            ExecutorService blockExecutors = Executors.newFixedThreadPool(runs * 4);
            ArrayList<Runnable> blockRunnableList = new ArrayList<Runnable>();

            for (int blockColumn = 0; blockColumn < runs; blockColumn++) {
                applyQuarteredGroupFilter(index, width, height, panelRow, panelColumn, panelWidth, panelHeight, factor, pixelArray, pixelArrayNew, blockRunnableList);
                index += blockWidth;
            }
            for (Runnable r: blockRunnableList) {
                blockExecutors.execute(r);
            }

            blockExecutors.shutdown();
            while(!blockExecutors.isTerminated()) {
            }
            blockRunnableList.clear();

            index = (blockRow + 1) * (blockHeight * width);

        }
        return pixelArrayNew;
    }

    public void applyQuarteredGroupFilter(int index, int width, int height, int panelRow, int panelColumn, int panelWidth, int panelHeight, double factor, int[] pixelArray, int[] pixelArrayNew, ArrayList<Runnable> runnableArrayList) {

        int indexA = index;
        int indexB = index + panelWidth;
        int indexC = index + (panelHeight * width);
        int indexD = index + (panelWidth + (panelHeight * width));

        Runnable glitchRunnable = new GlitchFilter(indexA, width, height, panelRow, panelColumn, panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
        runnableArrayList.add(glitchRunnable);

        Runnable greyscaleRunnable = new GreyscaleFilter(indexB, width, height, (panelRow + panelWidth), panelColumn, panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
        runnableArrayList.add(greyscaleRunnable);

        Runnable originalRunnable = new Original(indexC, width, height, panelRow, (panelColumn + panelHeight), panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
        runnableArrayList.add(originalRunnable);

        Runnable invertedRunnable = new InvertedFilter(indexD, width, height, (panelRow + panelWidth), (panelColumn + panelHeight), panelWidth, panelHeight, factor, pixelArray, pixelArrayNew);
        runnableArrayList.add(invertedRunnable);
    }

    public void decrementPanels() {
        if(runs <= 1) {
            runs = 1;
        } else {
            runs /= 2;
        }
        chooseCheckerboard();
    }
    public void incrementPanels() {
        if (runs >= 8) {
            runs = 8;
        } else {
            runs *= 2;
        }
        chooseCheckerboard();
    }
}
