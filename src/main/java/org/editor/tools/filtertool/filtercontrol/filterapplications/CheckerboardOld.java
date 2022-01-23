//package org.controller.tools.filtertool.filtercontrol.filterapplications;
//
//import org.controller.tools.filtertool.filtercontrol.ImageGridOld;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class CheckerboardOld {
//
//    private final ImageGridOld imageGridOld;
//    private int[] pixelArrayNew;
//
//    private int runs = 2;
//
//    public CheckerboardOld(ImageGridOld imageGridOld) {
//        this.imageGridOld = imageGridOld;
//        this.pixelArrayNew = new int[imageGridOld.getPixelArray().length];
//
////        this.pixelWriter = imageGrid.getPixelWriter();
////        this.writablePixelFormat = imageGrid.getWritablePixelFormat();
////        this.pixelArray = imageGrid.getPixelArray();
////        this.width = imageGrid.getWidth();
////        this.height = imageGrid.getHeight();
////        this.blockWidth = imageGrid.getWidth() / runs;
////        this.blockHeight = imageGrid.getHeight() / runs;
////        this.panelWidth = imageGrid.getWidth() / (runs * 2);
////        this.panelHeight = imageGrid.getHeight() / (runs * 2);
//    }
//
////    public int[] chooseCheckerboard() {
////        this.pixelArrayNew = applyCheckerBoard();
////        return pixelArrayNew;
////    }
//
//    public int[] applyCheckerBoard() {
//        // Index of the image's array of pixels
//        int index = 0;
//
//        int panelRow = 0;
//        int panelColumn = 0;
//
//        for (int blockRow = 0; blockRow < runs; blockRow++) {
//
//            // Manage thread status with ExecutorService
//            ExecutorService blockExecutors = Executors.newFixedThreadPool(runs * 4);
//            ArrayList<Runnable> blockRunnableList = new ArrayList<Runnable>();
//
//            for (int blockColumn = 0; blockColumn < runs; blockColumn++) {
//                applyQuarteredGroupFilter(index, imageGridOld, panelRow, panelColumn, blockRunnableList);
//                index += imageGridOld.getBlockWidth();
//            }
//            for (Runnable r: blockRunnableList) {
//                blockExecutors.execute(r);
//            }
//
//            blockExecutors.shutdown();
//            while(!blockExecutors.isTerminated()) {
//            }
//            blockRunnableList.clear();
//
//            index = (blockRow + 1) * (imageGridOld.getBlockHeight() * imageGridOld.getWidth());
//
//        }
//        return pixelArrayNew;
//    }
//
//    public void applyQuarteredGroupFilter(int index, ImageGridOld imageGridOld, int panelRow, int panelColumn, ArrayList<Runnable> runnableArrayList) {
//
//        int width = imageGridOld.getWidth();
//        int height = imageGridOld.getHeight();
//        int panelWidth = imageGridOld.getPanelWidth();
//        int panelHeight = imageGridOld.getPanelHeight();
//
//        int indexA = index;
//        int indexB = index + panelWidth;
//        int indexC = index + (panelHeight * width);
//        int indexD = index + (panelWidth + (panelHeight * width));
//
//
//
//
//
////        for (FilterTypes f: filterTypesList) {
////            switch (f) {
////                case ORIGINAL:
////                    Runnable originalRunnable = new Original(indexC, imageGrid, panelRow, (panelColumn + panelHeight), pixelArrayNew);
////                    runnableArrayList.add(originalRunnable);
////                    break;
////                case GLITCH:
////                    Runnable glitchRunnable = new GlitchFilter(indexA, imageGrid, panelRow, panelColumn, pixelArrayNew);
////                    runnableArrayList.add(glitchRunnable);
////                case GREYSCALE:
////                    Runnable greyscaleRunnable = new GreyscaleFilter(indexB, imageGrid.getWidth(), imageGrid.getHeight(), (panelRow + panelWidth), panelColumn, panelWidth, panelHeight, imageGrid.getFactor(), pixelArrayNew);
////                    runnableArrayList.add(greyscaleRunnable);
////                case INVERTED:
////                    Runnable invertedRunnable = new InvertedFilter(indexD, imageGrid.getWidth(), imageGrid.getHeight(), (panelRow + panelWidth), (panelColumn + panelHeight), panelWidth, panelHeight, pixelArrayNew);
////                    runnableArrayList.add(invertedRunnable);
////                    break;
////            }
////        }
//    }
//
//
//
////    public void decrementPanels() {
////        if(runs <= 1) {
////            runs = 1;
////        } else {
////            runs /= 2;
////        }
////        chooseCheckerboard();
////    }
////    public void incrementPanels() {
////        if (runs >= 8) {
////            runs = 8;
////        } else {
////            runs *= 2;
////        }
////        chooseCheckerboard();
////    }
//}
