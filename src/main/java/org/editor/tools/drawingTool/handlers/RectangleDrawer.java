package org.editor.tools.drawingTool.handlers;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.editor.tools.drawingTool.DrawingTool;
import org.editor.tools.drawingTool.attributes.AbstractGeneral;
import org.editor.tools.drawingTool.attributes.General;
import org.editor.tools.drawingTool.objects.Rectangle;


public class RectangleDrawer implements DrawHandler {

    private Point2D point1;
    private final DrawingTool dt;
    private General attributes;

    public RectangleDrawer(DrawingTool dt){
        this.dt = dt;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            this.point1 = new Point2D(mouseEvent.getX(),mouseEvent.getY());
        }else if(MouseEvent.MOUSE_RELEASED.equals(mouseEvent.getEventType())){
            double[] dims = dt.getDc().calculateMinXMinYWidthHeight(point1,
                    new Point2D(mouseEvent.getX(),mouseEvent.getY()));

            dt.getDb().addDrawOperation(new Rectangle(dims[0], dims[1], dims[2], dims[3], attributes));

        }



    }

    @Override
    public void setAttributes(AbstractGeneral attributes) {
        this.attributes = (General) attributes;
    }
}
