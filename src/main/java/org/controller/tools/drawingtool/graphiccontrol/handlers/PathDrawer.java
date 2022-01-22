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

    private void setPathAttributes(){
        dt.getGcBrush().setFill(attributes.getColor());
        dt.getGcBrush().setStroke(attributes.getColor());
        dt.getGcBrush().setLineWidth(attributes.getLineWidth());
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            setPathAttributes();
            dt.getGcBrush().beginPath();
            dt.getGcBrush().moveTo(mouseEvent.getX(), mouseEvent.getY());
            if(attributes.isFill()){
                dt.getGcBrush().fill();
            }else {
                dt.getGcBrush().stroke();
            }
        }else if(MouseEvent.MOUSE_DRAGGED.equals(mouseEvent.getEventType())){
            dt.getGcBrush().lineTo(mouseEvent.getX(), mouseEvent.getY());
            if(attributes.isFill()){
                dt.getGcBrush().fill();
            }else {
                dt.getGcBrush().stroke();
            }
        }
    }
}
