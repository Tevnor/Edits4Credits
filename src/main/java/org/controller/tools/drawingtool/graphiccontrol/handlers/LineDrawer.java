package org.controller.tools.drawingtool.graphiccontrol.handlers;


import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.objects.Line;

public class LineDrawer implements EventHandler<MouseEvent> {

    private Point2D point1;
    private final DrawingTool dt;

    public LineDrawer(DrawingTool dt){
        this.dt = dt;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            this.point1 = new Point2D(mouseEvent.getX(),mouseEvent.getY());
        }else if(MouseEvent.MOUSE_RELEASED.equals(mouseEvent.getEventType())){
            dt.getDb().addDrawOperation(new Line(point1.getX(),point1.getY(),
                    mouseEvent.getX(), mouseEvent.getY(), Color.BLACK));

        }



    }
}
