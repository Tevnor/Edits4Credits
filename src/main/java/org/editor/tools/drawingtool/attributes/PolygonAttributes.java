package org.editor.tools.drawingtool.attributes;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolygonAttributes extends AbstractGeneral {
    private static final Logger PA_LOGGER = LogManager.getLogger(PolygonAttributes.class.getName());
    private final boolean polyClose;

    public PolygonAttributes(AbstractGeneral base, boolean polyClose) {
        super(base);
        this.polyClose = polyClose;
        PA_LOGGER.debug("PolygonAttributes object created");
    }

    public boolean isPolyClose() {
        return polyClose;
    }

}
