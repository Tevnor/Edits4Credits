package org.editor.tools.drawingTool.handlers;


import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.editor.tools.drawingTool.DrawingTool;
import org.editor.tools.drawingTool.attributes.AbstractGeneral;
import org.editor.tools.drawingTool.attributes.General;
import org.editor.tools.drawingTool.objects.Line;

public class LineDrawer implements DrawHandler {

    private Point2D point1;
    private final DrawingTool dt;
    private General attributes;

    public LineDrawer(DrawingTool dt){
        this.dt = dt;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            this.point1 = new Point2D(mouseEvent.getX(),mouseEvent.getY());
        }else if(MouseEvent.MOUSE_RELEASED.equals(mouseEvent.getEventType())){
            dt.getDb().addDrawOperation(new Line(point1.getX(),point1.getY(),
                    mouseEvent.getX(), mouseEvent.getY(), attributes));

        }



    }

    @Override
    public void setAttributes(AbstractGeneral attributes) {
        this.attributes = (General) attributes;
    }
}
