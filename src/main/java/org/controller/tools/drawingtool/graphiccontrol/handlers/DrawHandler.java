package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.controller.tools.drawingtool.graphiccontrol.attributes.AbstractGeneral;

public interface DrawHandler extends EventHandler<MouseEvent> {
    void setAttributes(AbstractGeneral attributes);
}
