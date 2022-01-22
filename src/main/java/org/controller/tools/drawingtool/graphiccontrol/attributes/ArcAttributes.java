package org.controller.tools.drawingtool.graphiccontrol.attributes;

import javafx.scene.shape.ArcType;

public class ArcAttributes extends AbstractGeneral {

    private final ArcType arcType;
    private final double startAngle, arcExtent;

    public ArcAttributes(AbstractGeneral base, ArcType arcType, double startAngle, double arcExtent){
        super(base);
        this.arcType = arcType;
        this.startAngle = startAngle;
        this.arcExtent = arcExtent;
    }

    public ArcType getArcType() {
        return arcType;
    }
    public double getStartAngle() {
        return startAngle;
    }
    public double getArcExtent() {
        return arcExtent;
    }
}
