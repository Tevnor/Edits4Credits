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

/**
 * The actual filter operations on a pixel level.
 * Mapping of filter selections to their respective functionalities.
 * Calculation of panel size, position, and array index hops to enable concurrency by geometric partition.
 *
 */
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

    private final double factorX;
    private final double factorY;
    private final boolean isComplement;
    private final boolean isSilhouette;

    /**
     * Instantiates a new Filter operation.
     *
     * @param imageGrid       the image grid
     * @param inputAttributes the input attributes
     */
    public FilterOperation(ImageGrid imageGrid, FilterInputAttributes inputAttributes) {
        this.runs = inputAttributes.getRuns();
        this.pixelArray = imageGrid.getPixelArray();
        this.pixelArrayNew = new int[pixelArray.length];
        this.filterTypeList = inputAttributes.getFilterTypeList();
        double factor = inputAttributes.getFactor();
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
     * Start the filter process by dividing the grid into the chosen number of partitions.
     * Set up and manage multi-threading functionalities via ExecutorServices, Runnable Lists, and CountDownLatches.
     *
     * @return the integer array containing the new argb values
     */
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
     * Operations within each mid-level partition/block are managed here.
     * Further multi-threading management is set up to facilitate dynamic, fine-grained concurrency.
     *
     * Dimensions and index positions of the low-level are set according to the amount of partitions to be made.
     */
    class BlockOperation implements Runnable {
        private final int index;
        private final CountDownLatch blockFinish;

        /**
         * Instantiates a new Block operation.
         *
         * @param blockFinish the block finish
         * @param index       the index
         */
        BlockOperation(CountDownLatch blockFinish, int index) {
            this.index = index;
            this.blockFinish = blockFinish;
        }

        @Override
        public void run() {
            startBlock();
            blockFinish.countDown();
        }

        /**
         * Start the sequence to subdivide each partition/block into four panels, each with their own thread.
         * Map the user selected filters to their functions with the FILTER_TO_ENUM_MAP.
         *
         */
        private void startBlock() {
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

        /**
         * Gets panel starting index.
         *
         * @param index   the starting index of the governing block
         * @param ordinal the ordinal of the panel
         * @return the panel starting index
         */
        private int getPanelStartingIndex(int index, int ordinal) {
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
     * The bottom-level filter operations where the filters' respective functions alter the integer argb values of the image's pixels.
     */
    class PanelOperation implements Runnable {
        private int pixelIndex;
        private final Filter filter;
        private final CountDownLatch panelFinish;

        /**
         * Instantiates a new Panel operation.
         *
         * @param pixelIndex  the pixel index
         * @param filter      the filter
         * @param panelFinish the panel finish
         */
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

        /**
         * Integer values in the array are overwritten.
         * Filter's method is called with the user specified input factors.
         *
         * If silhouette is toggled on, integer values are cast to shorts, effectively manipulating some pixels into falling into their alpha channel's zero range, thus being set invisible.
         * If complement is toggled on, all these pixel values are shifted bitwise to create a negative.
         */
        private void startPanel() {
            int rowIndex = pixelIndex;

            for (int row = 0; row < panelHeight; row++) {
                for (int col = 0; col < panelWidth; col++) {
                    int argbOriginal = pixelArray[pixelIndex];

                    //TODO new
                    double factorXY = factorX / factorY;

                    int argb = filter.applyFilter(pixelArray[pixelIndex], factorXY);

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
