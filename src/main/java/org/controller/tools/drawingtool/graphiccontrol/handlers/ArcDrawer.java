package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.attributes.AbstractGeneral;
import org.controller.tools.drawingtool.graphiccontrol.attributes.ArcAttributes;
import org.controller.tools.drawingtool.graphiccontrol.objects.Arc;

public class ArcDrawer implements DrawHandler {
    private Point2D point1;
    private final DrawingTool dt;
    private ArcAttributes attributes;

    public ArcDrawer(DrawingTool dt){
        this.dt = dt;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            this.point1 = new Point2D(mouseEvent.getX(),mouseEvent.getY());
        }else if(MouseEvent.MOUSE_RELEASED.equals(mouseEvent.getEventType())){
            double[] dims = dt.getDc().calculateMinXMinYWidthHeight(point1,
                    new Point2D(mouseEvent.getX(),mouseEvent.getY()));

            dt.getDb().addDrawOperation(new Arc(dims[0], dims[1], dims[2], dims[3],
                    attributes));

        }

    }

    @Override
    public void setAttributes(AbstractGeneral attributes) {
        this.attributes = (ArcAttributes) attributes;
    }
}
