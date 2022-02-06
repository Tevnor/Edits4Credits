package org.editor.tools.filterTool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.filterTool.filterControl.Filter;
import org.editor.tools.filterTool.filterControl.FilterInputAttributes;
import org.editor.tools.filterTool.filterControl.filter.FilterType;
import org.editor.tools.imageTool.ImageGrid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *     The actual filter operations on a pixel level.
 *     Mapping of filter selections to their respective functionalities.
 *     Calculation of panel size, position, and array index hops to enable concurrency by geometric partition.
 * </p>
 *     To determine the starting index of each panel within a certain block:
 *     Notation:
 * <ul>
 *     <li>W_t = width of the total image</li>
 *     <li>W_p = width of a panel</li>
 *     <li>H_p = height of a panel</li>
 *     <li>i_s = starting index of the block</li>
 *     <li>i_0 = starting index of the top-left panel</li>
 *     <li>i_1 = starting index of the top-right panel</li>
 *     <li>i_2 = starting index of the bottom-left panel</li>
 *     <li>i_3 = starting index of the bottom-right panel</li>
 * </ul>
 * <p>
 *     i_0 = i_s
 *     i_1 = W_p + i_s
 *     i_2 = W_t * H_p + i_s
 *     i_3 = W_t * H_p + W_p + i_s
 * </p>
 * <p>
 *     The of calculations to determine the dimensions of the last panel summarized in one equation:
 *     Notation:
 * </p>
 * <ul>
 *     P_last = width|height of the last panel of the column/row
 *     T_dim = width|height of the total image
 *     N_p = Total amount of panels in column/row
 * </ul>
 * <p>
 *     P_last = T_dim - ⌊(T_dim / N_p)⌉ * (N_p - 1)
 * </p>
 */
public class FilterOperation {

    private static final Logger FO_LOGGER = LogManager.getLogger(FilterOperation.class);

    private final int runs;
    private final int totalWidth;
    private final int blockWidth;
    private final int blockHeight;
    private final int panelWidth;
    private final int panelWidthLast;
    private final int panelHeight;
    private final int panelHeightLast;
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
     * @see ImageGrid
     * @see FilterInputAttributes
     */
    public FilterOperation(ImageGrid imageGrid, FilterInputAttributes inputAttributes) {
        runs = inputAttributes.getRuns();
        int panelsOnAxis = runs * 2;
        pixelArray = imageGrid.getPixelArray();
        pixelArrayNew = new int[pixelArray.length];

        filterTypeList = inputAttributes.getFilterTypeList();
        factorX = inputAttributes.getFactorX();
        factorY = inputAttributes.getFactorY();
        isComplement = inputAttributes.isComplementToggle();
        isSilhouette = inputAttributes.isSilhouetteToggle();

        totalWidth = imageGrid.getWidth();
        int height = imageGrid.getHeight();

        panelWidth = calculatePanelDimension(totalWidth, panelsOnAxis);
        panelWidthLast = calculatePanelDimensionLast(totalWidth, panelWidth, panelsOnAxis);
        blockWidth = calculateBlockDimension(panelWidth);

        panelHeight = calculatePanelDimension(height, panelsOnAxis);
        panelHeightLast = calculatePanelDimensionLast(height, panelHeight, panelsOnAxis);
        blockHeight = calculateBlockDimension(panelHeight);

        FO_LOGGER.debug("New FilterOperation object instantiated.");
    }

    /**
     * <p>
     *     Checks if image dimensions are cleanly divisible by the number of panels on each axis.
     *     Calculates the dimensions for each panel but the last by rounding the division of the total dimension by the amount of panels on the axis.
     * </p>
     * @param dimension int value for either the total width or the total height of the image
     * @param panelsOnAxis int value for the number of panels on the x or y axis
     * @return the rounded integer for the dimensions of all panels but the last
     * @see Math#round(double)
     * */
    public int calculatePanelDimension(int dimension, int panelsOnAxis) {
        return (int) Math.round((double) dimension / panelsOnAxis);
    }
    /**
     * <p>
     *     Calculate the dimensions of the last panel by subtracting the combined dimension of all panels but the last from the dimension of the total image.
     * </p>
     * @param dimension int value for either the total width or the total height of the image
     * @param panelDimension the previously calculated dimensions of all panels but the last
     * @param panelsOnAxis int value for the number of panels on the x or y axis
     * @return the rounded integer for the dimensions of the last panel
     * */
    public int calculatePanelDimensionLast(int dimension, int panelDimension, int panelsOnAxis) {
        return dimension - (panelDimension * (panelsOnAxis - 1));
    }
    /**
     * <p>
     *     Calculate the dimensions for all blocks.
     * </p>
     * @param panelDimension the dimensions of the inherent panels
     * @return the dimensions of a block
     * */
    public int calculateBlockDimension(int panelDimension) {
        return panelDimension * 2;
    }

    /**
     * <p>
     *     Start the filter process by dividing the grid into the chosen number of partitions 'blocks'.
     *     Set up and manage multi-threading functionalities via ExecutorServices, Runnable Lists, and CountDownLatches.
     * </p>
     * @return the integer array containing the new argb values
     * @see CountDownLatch
     * @see ExecutorService
     * @see ArrayList
     * @see Runnable
     */
    public int[] startFilter() {
        int index = 0;
        boolean isLastHorizontalBlock = false;
        boolean isLastVerticalBlock = false;

        for (int blockRow = 0; blockRow < runs; blockRow++) {
            if (blockRow == runs - 1) {
                isLastVerticalBlock = true;
            }

            CountDownLatch blockFinish = new CountDownLatch(runs);
            ExecutorService blockExecs = Executors.newFixedThreadPool(runs);
            ArrayList<Runnable> blockRunnableList = new ArrayList<>();

            for (int blockCol = 0; blockCol < runs; blockCol++) {
                if (blockCol == runs - 1) {
                    isLastHorizontalBlock = true;
                }

                Runnable blockRunnable = new BlockOperation(index, isLastHorizontalBlock, isLastVerticalBlock, blockFinish);
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
            index = (blockRow + 1) * (blockHeight * totalWidth);
            isLastHorizontalBlock = false;
            blockExecs.shutdown();
        }
        return pixelArrayNew;
    }

    /**
     * <p>
     *     Operations within each mid-level partition/block are managed here.
     *     Further multi-threading management is set up to facilitate dynamic, fine-grained concurrency.
     * </p>
     * Dimensions and index positions of the low-level are set according to the amount of partitions to be made.
     */
    public class BlockOperation implements Runnable {
        private final int index;
        private final boolean isLastHorizontalBlock;
        private final boolean isLastVerticalBlock;
        private final CountDownLatch blockFinish;

        /**
         * Instantiates a new Block operation.
         *
         * @param blockFinish the block finish
         * @param index       the index
         */
        BlockOperation(int index, boolean isLastHorizontalBlock, boolean isLastVerticalBlock, CountDownLatch blockFinish) {
            this.index = index;
            this.isLastHorizontalBlock = isLastHorizontalBlock;
            this.isLastVerticalBlock = isLastVerticalBlock;
            this.blockFinish = blockFinish;
        }

        @Override
        public void run() {
            try {
                startBlock(isLastHorizontalBlock, isLastVerticalBlock);
            } catch (Exception e) {
                e.printStackTrace();
            }
            blockFinish.countDown();
        }

        /**
         * Start the sequence to subdivide each partition/block into four panels, each with their own thread.
         * Map the user selected filters to their functions with the FILTER_TO_ENUM_MAP.
         *
         */
        private void startBlock(boolean isLastHorizontalBlock, boolean isLastVerticalBlock) {
            CountDownLatch panelFinish = new CountDownLatch(4);
            ExecutorService panelExecs = Executors.newFixedThreadPool(4);
            ArrayList<Runnable> panelRunnableList = new ArrayList<>();

            int listIndex = 0;
            for (FilterType filterType : filterTypeList) {
                int panelIndex = getPanelStartingIndex(index, listIndex);
                Filter filter = FilterType.TYPE_TO_FILTER_ENUM_MAP.get(filterType);
                Runnable panelRunnable = new PanelOperation(panelIndex, isLastHorizontalBlock, isLastVerticalBlock, filter, panelFinish);
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
            panelExecs.shutdown();
        }

        /**
         * Gets panel starting index.
         *
         * @param index   the starting index of the governing block
         * @param ordinal the ordinal of the panel
         * @return the panel starting index
         */
        public int getPanelStartingIndex(int index, int ordinal) {
            switch (ordinal) {
                case 0:
                    // Index stays the same
                    break;
                case 1:
                    index += panelWidth;
                    break;
                case 2:
                    index += (panelHeight * totalWidth);
                    break;
                case 3:
                    index += (panelWidth + (panelHeight) * totalWidth);
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
        private final boolean isLastHorizontalPanel;
        private final boolean isLastVerticalPanel;
        private final Filter filter;
        private final CountDownLatch panelFinish;

        /**
         * Instantiates a new Panel operation.
         *
         * @param pixelIndex  the pixel index
         * @param filter      the filter
         * @param panelFinish the panel finish
         */
        public PanelOperation(int pixelIndex, boolean isLastHorizontalPanel, boolean isLastVerticalPanel, Filter filter, CountDownLatch panelFinish) {
            this.pixelIndex = pixelIndex;
            this.isLastHorizontalPanel = isLastHorizontalPanel;
            this.isLastVerticalPanel = isLastVerticalPanel;
            this.filter = filter;
            this.panelFinish = panelFinish;
        }
        @Override
        public void run() {
            startPanel(isLastHorizontalPanel, isLastVerticalPanel);
            panelFinish.countDown();
        }

        /**
         * Integer values in the array are overwritten.
         * Filter's method is called with the user specified input factors.
         *
         * If silhouette is toggled on, integer values are cast to shorts, effectively manipulating some pixels into falling into their alpha channel's zero range, thus being set invisible.
         * If complement is toggled on, all these pixel values are shifted bitwise to create a negative.
         */
        private void startPanel(boolean isLastHorizontalPanel, boolean isLastVerticalPanel) {
            int rowIndex = pixelIndex;
            int width = !isLastHorizontalPanel ? panelWidth : panelWidthLast;
            int height = !isLastVerticalPanel ? panelHeight : panelHeightLast;

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int argbOriginal = pixelArray[pixelIndex];

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
                rowIndex += totalWidth;
                pixelIndex = rowIndex;
            }
        }
    }
}
