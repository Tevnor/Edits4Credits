package org.editor.tools.drawingtool.attributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EraserAttributes extends AbstractGeneral {
    private static final Logger EA_LOGGER = LogManager.getLogger(EraserAttributes.class.getName());
    private final double size;
    private final boolean circle;

    public EraserAttributes(AbstractGeneral abstractGeneral, double size, boolean circle) {
        super(abstractGeneral);
        this.size = size;
        this.circle = circle;
        EA_LOGGER.debug("EraserAttributes object created");
    }

    public double getSize() {
        return size;
    }
    public boolean isCircle() {
        return circle;
    }
}
