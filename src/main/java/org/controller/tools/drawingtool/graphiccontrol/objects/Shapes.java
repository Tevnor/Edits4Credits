package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;
import org.controller.tools.drawingtool.graphiccontrol.DrawOp;


public abstract class Shapes extends DrawOp {
    public enum Type{
        ARC, CIRCLE, ELLIPSES, LINE, POLYGON, RECTANGLE, ROUNDED_RECT, TEXT
    }
    protected double minX, minY, width, height;
    protected Paint color;
    protected Rotate r = new Rotate(0,0,0);
    protected Type type;
    protected Attributes attributes;




    protected abstract void setRotation(double angle);
    public abstract Shape getShapeRepresentation();
    public abstract Shapes reposition(Point2D point);
    public abstract boolean pointInside(Point2D point);

    protected void applyRotate(GraphicsContext gc, Rotate r){
        gc.setTransform(r.getMxx(),r.getMyx(),r.getMxy(),r.getMyy(),r.getTx(),r.getTy());
    }

    public void setAttributes(GraphicsContext gc){
        applyRotate(gc,r);
        gc.setGlobalAlpha(attributes.getAlpha());
        gc.setGlobalBlendMode(attributes.getBm());
        gc.setLineWidth(attributes.getLineWidth());
        if(attributes.getEffects().size() != 0){
            for(Effect e : attributes.getEffects()){
                gc.setEffect(e);
            }
        }
    }

    public double getMinX(){ return minX; }
    public double getMinY(){ return minY; }
    public double getWidth(){ return width; }
    public double getHeight(){ return height; }
    public Paint getColor(){
        return attributes.getColor();
    }
    public Type getType(){return type;}
    public Rotate getR(){
        return r;
    }

}
