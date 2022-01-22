package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.scene.input.MouseEvent;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.attributes.AbstractGeneral;
import org.controller.tools.drawingtool.graphiccontrol.attributes.TextAttributes;
import org.controller.tools.drawingtool.graphiccontrol.objects.Text;

public class TextDrawer implements DrawHandler {

    private final DrawingTool dt;
    private TextAttributes attributes;
    protected TextDrawer(DrawingTool dt){
        this.dt = dt;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_CLICKED.equals(mouseEvent.getEventType())){
            dt.getDb().addDrawOperation(new Text(mouseEvent.getX(),mouseEvent.getY(),
                    attributes));
        }

    }

    @Override
    public void setAttributes(AbstractGeneral attributes) {
        this.attributes = (TextAttributes) attributes;
    }
}
