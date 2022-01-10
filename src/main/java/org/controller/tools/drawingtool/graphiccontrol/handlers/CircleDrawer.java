package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.objects.Circle;


public class CircleDrawer implements EventHandler<MouseEvent> {

    private Point2D point1;
    private DrawingTool dt;

    public CircleDrawer(DrawingTool dt){
        this.dt = dt;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            this.point1 = new Point2D(mouseEvent.getX(),mouseEvent.getY());
        }else if(MouseEvent.MOUSE_RELEASED.equals(mouseEvent.getEventType())){
            double[] dims = dt.getDc().calculateMinXMinYRadius(point1,
                    new Point2D(mouseEvent.getX(),mouseEvent.getY()));

            dt.getDb().addDrawOperation(new Circle(dims[0], dims[1], dims[2], Color.BLACK));

        }



    }
}
