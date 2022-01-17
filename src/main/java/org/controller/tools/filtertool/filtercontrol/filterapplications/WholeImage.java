package org.controller.tools.filtertool.filtercontrol.filterapplications;

public class WholeImage {
    private final double variance;


    public WholeImage(double sliderValue, int factor) {
        this.variance = sliderValue / factor;
    }

    public void apply() {
        //TODO
    }
}
