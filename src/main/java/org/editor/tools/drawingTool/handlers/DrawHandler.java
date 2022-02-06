package org.editor.tools.drawingTool.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.editor.tools.drawingTool.attributes.AbstractGeneral;

public interface DrawHandler extends EventHandler<MouseEvent> {
    void setAttributes(AbstractGeneral attributes);
}
