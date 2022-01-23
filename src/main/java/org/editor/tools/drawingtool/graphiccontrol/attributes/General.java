package org.editor.tools.drawingtool.graphiccontrol.attributes;

import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

public class General extends AbstractGeneral{
    public General(double rotation, BlendMode bm, double lineWidth, Paint color, double alpha, boolean fill) {
        super(rotation, bm, lineWidth, color, alpha, fill);
    }
    public General(AbstractGeneral abstractGeneral) {
        super(abstractGeneral);
    }
}
