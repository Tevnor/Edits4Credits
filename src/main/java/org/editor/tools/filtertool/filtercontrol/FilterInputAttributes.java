package org.editor.tools.filtertool.filtercontrol;

import org.editor.tools.filtertool.filtercontrol.filter.FilterType;

import java.util.List;

public class FilterInputAttributes {

    /**
     * Default values
     * */
    private double factor = 0;
    private double factorX = 0;
    private double factorY = 0;
    private boolean complementToggle = false;
    private boolean silhouetteToggle = false;
    private List<FilterType> filterTypeList;
    private int runs = 2;

    /**
     * Getters and setters
     * */

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public double getFactorX() {
        return factorX;
    }
    public void setFactorX(double factorX) {
        this.factorX = factorX;
    }

    public double getFactorY() {
        return factorY;
    }
    public void setFactorY(double factorY) {
        this.factorY = factorY;
    }

    public boolean isComplementToggle() {
        return complementToggle;
    }

    public void setComplementToggle(boolean complementToggle) {
        this.complementToggle = complementToggle;
    }

    public boolean isSilhouetteToggle() {
        return silhouetteToggle;
    }

    public void setSilhouetteToggle(boolean silhouetteToggle) {
        this.silhouetteToggle = silhouetteToggle;
    }

    public List<FilterType> getFilterTypeList() {
        return filterTypeList;
    }

    public void setFilterTypeList(List<FilterType> filterTypeList) {
        this.filterTypeList = filterTypeList;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void increaseRuns() {
        if (runs >= 8) {
            runs = 8;
        } else {
            runs *= 2;
        }
    }

    public void decreaseRuns() {
        if(runs <= 2) {
            runs = 2;
        } else {
            runs /= 2;
        }
    }
}
