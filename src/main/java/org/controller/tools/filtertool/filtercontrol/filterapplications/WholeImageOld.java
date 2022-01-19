package org.controller.tools.filtertool.filtercontrol.filterapplications;

public class WholeImageOld {
    private final double variance;


    public WholeImageOld(double sliderValue, int factor) {
        this.variance = sliderValue / factor;
    }

    public void apply() {
        //TODO
    }
}
