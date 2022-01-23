package org.editor.tools.drawingtool.graphiccontrol.attributes;

public class EraserAttributes extends AbstractGeneral {
    private final double size;
    private final boolean circle;

    public EraserAttributes(AbstractGeneral abstractGeneral, double size, boolean circle) {
        super(abstractGeneral);
        this.size = size;
        this.circle = circle;
    }

    public double getSize() {
        return size;
    }
    public boolean isCircle() {
        return circle;
    }
}
