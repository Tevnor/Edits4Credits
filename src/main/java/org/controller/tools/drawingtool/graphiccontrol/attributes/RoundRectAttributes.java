package org.controller.tools.drawingtool.graphiccontrol.attributes;

public class RoundRectAttributes extends AbstractGeneral {
    private final double arcHeight;
    private final double arcWidth;

    public RoundRectAttributes(AbstractGeneral base, double arcWidth, double arcHeight) {
        super(base);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    public double getArcHeight() {
        return arcHeight;
    }
    public double getArcWidth() {
        return arcWidth;
    }
}
