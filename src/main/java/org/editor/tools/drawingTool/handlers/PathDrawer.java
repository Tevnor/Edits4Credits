package org.editor.tools.drawingTool.handlers;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import org.editor.tools.drawingTool.DrawingTool;
import org.editor.tools.drawingTool.attributes.AbstractGeneral;
import org.editor.tools.drawingTool.attributes.General;

public class PathDrawer implements DrawHandler {
    private final DrawingTool dt;
    private General attributes;


    public PathDrawer(DrawingTool dt){
        this.dt = dt;
    }

    private void setPathAttributes(){
        dt.getGcBrush().setFill(attributes.getColor());
        dt.getGcBrush().setStroke(attributes.getColor());
        dt.getGcBrush().setLineWidth(attributes.getLineWidth());
        dt.getGcBrush().setGlobalBlendMode(attributes.getBm());
        dt.getGcBrush().setGlobalAlpha(attributes.getAlpha());
        dt.getGcBrush().setLineCap(StrokeLineCap.ROUND);
        dt.getGcBrush().setLineJoin(StrokeLineJoin.ROUND);
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

    @Override
    public void setAttributes(AbstractGeneral attributes) {
        this.attributes = (General) attributes;
    }
}
