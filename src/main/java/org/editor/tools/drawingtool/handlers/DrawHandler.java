package org.editor.tools.drawingtool.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.editor.tools.drawingtool.attributes.AbstractGeneral;

public interface DrawHandler extends EventHandler<MouseEvent> {
    void setAttributes(AbstractGeneral attributes);
}
