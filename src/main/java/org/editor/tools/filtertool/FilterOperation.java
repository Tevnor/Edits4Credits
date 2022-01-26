package org.editor.tools.filtertool;

import org.editor.tools.filtertool.filtercontrol.Filter;
import org.editor.tools.filtertool.filtercontrol.FilterInputAttributes;
import org.editor.tools.filtertool.filtercontrol.filter.FilterType;
import org.editor.tools.imagetool.ImageGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilterOperation {

    private final int runs;
    private final int width;
    private final int blockWidth;
    private final int blockHeight;
    private final int panelWidth;
    private final int panelHeight;
    private final int[] pixelArray;
    private final int[] pixelArrayNew;
    private final List<FilterType> filterTypeList;

    private final double factor;
    private final double factorX;
    private final double factorY;
    private final boolean isComplement;
    private final boolean isSilhouette;
    /**
     * FILTER OPERATION
     * */
    public FilterOperation(ImageGrid imageGrid, FilterInputAttributes inputAttributes) {
        this.runs = inputAttributes.getRuns();
        this.pixelArray = imageGrid.getPixelArray();
        this.pixelArrayNew = new int[pixelArray.length];
        this.filterTypeList = inputAttributes.getFilterTypeList();
        this.factor = inputAttributes.getFactor();
        this.factorX = inputAttributes.getFactorX();
        this.factorY = inputAttributes.getFactorY();
        this.isComplement = inputAttributes.isComplementToggle();
        this.isSilhouette = inputAttributes.isSilhouetteToggle();

        this.width = imageGrid.getWidth();
        int height = imageGrid.getHeight();
        this.blockWidth = width / runs;
        this.blockHeight = height / runs;
        this.panelWidth = width / (runs * 2);
        this.panelHeight = height / (runs * 2);
    }

    /**
     * Start Filter factory for multi-threading
     * */
    public int[] startFilter() {
        int index = 0;

        for (int blockRow = 0; blockRow < runs; blockRow++) {
            CountDownLatch blockFinish = new CountDownLatch(runs);
            ExecutorService blockExecs = Executors.newFixedThreadPool(runs);
            ArrayList<Runnable> blockRunnableList = new ArrayList<>();

            for (int blockCol = 0; blockCol < runs; blockCol++) {
                Runnable blockRunnable = new BlockOperation(blockFinish, index);
                blockRunnableList.add(blockRunnable);
                index += blockWidth;
            }

            for (Runnable r: blockRunnableList) {
                blockExecs.execute(r);
            }
            try {
                blockFinish.await();
            } catch (InterruptedException eb) {
                eb.printStackTrace();
            }
            blockRunnableList.clear();
            index = (blockRow + 1) * (blockHeight * width);
        }
        return pixelArrayNew;
    }

    /**
     * BLOCK OPERATION
     * */
    class BlockOperation implements Runnable {
        private final int index;
        private final CountDownLatch blockFinish;

        BlockOperation(CountDownLatch blockFinish, int index) {
            this.index = index;
            this.blockFinish = blockFinish;
        }

        @Override
        public void run() {
            startBlock();
            blockFinish.countDown();
        }

        public void startBlock() {
            CountDownLatch panelFinish = new CountDownLatch(4);
            ExecutorService panelExecs = Executors.newFixedThreadPool(4);
            ArrayList<Runnable> panelRunnableList = new ArrayList<>();

            int listIndex = 0;
            for (FilterType filterType : filterTypeList) {
                int panelIndex = getPanelStartingIndex(index, listIndex);
                Filter filter = FilterType.TYPE_TO_FILTER_ENUM_MAP.get(filterType);
                Runnable panelRunnable = new PanelOperation(panelIndex, filter, panelFinish);
                panelRunnableList.add(panelRunnable);
                listIndex++;
            }

            for (Runnable rP: panelRunnableList) {
                panelExecs.execute(rP);
            }
            try {
                panelFinish.await();
            } catch (InterruptedException ep) {
                ep.printStackTrace();
            }
            panelRunnableList.clear();
        }

        public int getPanelStartingIndex(int index, int ordinal) {
            switch (ordinal) {
                case 0:
                    // Index stays the same
                    break;
                case 1:
                    index += panelWidth;
                    break;
                case 2:
                    index += (panelHeight * width);
                    break;
                case 3:
                    index += (panelWidth + (panelHeight) * width);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + ordinal);
            }
            return index;
        }
    }

    /**
     * PANEL OPERATION
     * */
    class PanelOperation implements Runnable {
        private int pixelIndex;
        private final Filter filter;
        private final CountDownLatch panelFinish;

        public PanelOperation(int pixelIndex, Filter filter, CountDownLatch panelFinish) {
            this.pixelIndex = pixelIndex;
            this.filter = filter;
            this.panelFinish = panelFinish;
        }
        @Override
        public void run() {
            startPanel();
            panelFinish.countDown();
        }

        public void startPanel() {
            int rowIndex = pixelIndex;

            for (int row = 0; row < panelHeight; row++) {
                for (int col = 0; col < panelWidth; col++) {
                    int argbOriginal = pixelArray[pixelIndex];

                    //TODO new
                    double factorxy = factorX / factorY;

                    int argb = filter.applyFilter(pixelArray[pixelIndex], factorxy);

                    if (isSilhouette) {
                        argb = (short) argb;
                        if (isComplement) {
                            argb = ~argb;
                        }

                        int alpha = (argb & 0xFF0000);
                        if (alpha == 0) {
                            argb = argbOriginal;
                        }
                    }
                    pixelArrayNew[pixelIndex] = argb;
                    pixelIndex++;
                }
                rowIndex += width;
                pixelIndex = rowIndex;
            }
        }
    }
}
