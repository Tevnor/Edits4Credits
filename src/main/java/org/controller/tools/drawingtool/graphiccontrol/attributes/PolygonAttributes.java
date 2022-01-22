package org.controller.tools.drawingtool.graphiccontrol.attributes;


public class PolygonAttributes extends AbstractGeneral {
    private final boolean polyClose;

    public PolygonAttributes(AbstractGeneral base, boolean polyClose) {
        super(base);
        this.polyClose = polyClose;
    }

    public boolean isPolyClose() {
        return polyClose;
    }

}
