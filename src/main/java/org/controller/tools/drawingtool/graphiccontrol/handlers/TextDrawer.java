package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.objects.Text;

public class TextDrawer implements EventHandler<MouseEvent> {

    private DrawingTool dt;
    protected TextDrawer(DrawingTool dt){
        this.dt = dt;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_CLICKED.equals(mouseEvent.getEventType())){
            dt.getDb().addDrawOperation(new Text(mouseEvent.getX(),mouseEvent.getY(),
                    "TEST", "Arial", 40, Color.BLACK));
        }

    }
}
