package org.editor.tools.filtertool.filtercontrol;

import org.editor.tools.filtertool.filtercontrol.filter.FilterType;

import java.util.List;

public class FilterInputAttributes {

    /**
     * Default values
     * */
//    private boolean original = false;
    private double factor = 0;
    private boolean complementToggle = false;
    private boolean silhouetteToggle = false;
    private List<FilterType> filterTypeList;
    private int runs = 1;
//    private FilterType filterType = FilterType.ORIGINAL;
    private FilterApplicationType appType = FilterApplicationType.STANDARD;

    /**
     * Getters and setters
     * */

    public FilterApplicationType getAppType() {
        return appType;
    }

    public void setAppType(FilterApplicationType appType) {
        this.appType = appType;
    }

//    public void setOriginal(boolean original) {
//        this.original = original;
//    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
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

}
