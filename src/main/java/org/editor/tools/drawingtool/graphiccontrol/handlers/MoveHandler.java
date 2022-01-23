package org.editor.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import org.editor.tools.drawingtool.DrawingTool;
import org.editor.tools.drawingtool.graphiccontrol.MovingControl;

public class MoveHandler implements EventHandler<MouseEvent> {

    private final DrawingTool dt;
    private final MovingControl mc;
    private boolean overShape;

    public MoveHandler(DrawingTool dt){
        this.dt = dt;
        this.mc = new MovingControl();
        this.overShape = false;
    }
    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())){
            Point2D point1 = new Point2D(mouseEvent.getX(), mouseEvent.getY());

            if(mc.overShape(point1,dt.getDb())){
                overShape = true;
                mc.setOffset(point1);
                mc.initMovingShape();
                mc.positionMovingShape(point1);
                dt.getStack().getChildren().add(mc.getMovingShape());
                dt.getStack().setCursor(Cursor.CLOSED_HAND);
            }
        }else if(MouseEvent.MOUSE_DRAGGED.equals(mouseEvent.getEventType())){
            if(overShape){
                Point2D current = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                mc.positionMovingShape(current);
            }
        }else if(MouseEvent.MOUSE_RELEASED.equals(mouseEvent.getEventType())){
            if(overShape){
                dt.getDb().addDrawOperation(mc.getPostSelectedShape());
                dt.getStack().getChildren().removeAll(mc.getMovingShape());
                overShape = false;
                mc.reset();
                dt.getStack().setCursor(Cursor.OPEN_HAND);
            }
        }
    }
}
