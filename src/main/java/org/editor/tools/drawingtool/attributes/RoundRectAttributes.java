package org.editor.tools.drawingtool.attributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoundRectAttributes extends AbstractGeneral {
    private static final Logger RRA_LOGGER = LogManager.getLogger(RoundRectAttributes.class.getName());
    private final double arcHeight;
    private final double arcWidth;

    public RoundRectAttributes(AbstractGeneral base, double arcWidth, double arcHeight) {
        super(base);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        RRA_LOGGER.debug("RoundRectAttributes object created");
    }

    public double getArcHeight() {
        return arcHeight;
    }
    public double getArcWidth() {
        return arcWidth;
    }
}
