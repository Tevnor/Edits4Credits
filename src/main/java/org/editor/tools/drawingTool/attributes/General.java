package org.editor.tools.drawingTool.attributes;

import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class General extends AbstractGeneral{
    private static final Logger G_LOGGER = LogManager.getLogger(General.class.getName());

    public General(double rotation, BlendMode bm, double lineWidth, Paint color, double alpha, boolean fill) {
        super(rotation, bm, lineWidth, color, alpha, fill);
        G_LOGGER.debug("General object created");
    }
    public General(AbstractGeneral abstractGeneral) {
        super(abstractGeneral);
        G_LOGGER.debug("General object created");
    }
}
