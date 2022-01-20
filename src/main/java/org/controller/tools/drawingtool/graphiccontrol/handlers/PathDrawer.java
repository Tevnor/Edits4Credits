package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;

public class PathDrawer implements EventHandler<MouseEvent> {
    private final DrawingTool dt;
    private final Attributes attributes;


    public PathDrawer(DrawingTool dt, Attributes attributes){
        this.dt = dt;
        this.attributes = attributes;
    }



    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            dt.getGc().beginPath();
            dt.getGc().moveTo(mouseEvent.getX(), mouseEvent.getY());
            dt.getGc().stroke();
        }else if(MouseEvent.MOUSE_DRAGGED.equals(mouseEvent.getEventType())){
            dt.getGc().lineTo(mouseEvent.getX(), mouseEvent.getY());
            dt.getGc().stroke();
        }
    }
}
