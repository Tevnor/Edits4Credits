package org.editor.tools.drawingtool.attributes;

import javafx.scene.shape.ArcType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcAttributes extends AbstractGeneral {
    private static final Logger AA_LOGGER = LogManager.getLogger(ArcAttributes.class.getName());
    private final ArcType arcType;
    private final double startAngle, arcExtent;

    public ArcAttributes(AbstractGeneral base, ArcType arcType, double startAngle, double arcExtent){
        super(base);
        this.arcType = arcType;
        this.startAngle = startAngle;
        this.arcExtent = arcExtent;
        AA_LOGGER.debug("ArcAttributes object created");
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
