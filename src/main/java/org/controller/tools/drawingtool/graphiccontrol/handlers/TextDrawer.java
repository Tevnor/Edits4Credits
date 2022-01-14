package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;
import org.controller.tools.drawingtool.graphiccontrol.objects.Text;

public class TextDrawer implements EventHandler<MouseEvent> {

    private final DrawingTool dt;
    private final Attributes attributes;
    protected TextDrawer(DrawingTool dt, Attributes attributes){
        this.dt = dt;
        this.attributes = attributes;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_CLICKED.equals(mouseEvent.getEventType())){
            dt.getDb().addDrawOperation(new Text(mouseEvent.getX(),mouseEvent.getY(),
                    attributes));
        }

    }
}
