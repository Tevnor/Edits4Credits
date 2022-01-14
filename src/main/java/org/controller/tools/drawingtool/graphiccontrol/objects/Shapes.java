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
    public static final int TYPE_STROKE = 0, TYPE_FILL = 1,
            ARC = 0, CIRCLE = 1, CLEAR = 2, ELLIPSES = 3, LINE = 4, POLYGON = 5, RECTANGLE = 6,
            ROUNDED_RECT = 7, TEXT = 8;
    protected double minX, minY, width, height;
    protected Paint color;
    protected Rotate r = new Rotate(0,0,0);
    protected int type;
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

    public double getMinX(){ return this.minX; }
    public double getMinY(){ return this.minY; }
    public double getWidth(){ return this.width; }
    public double getHeight(){ return this.height; }
    public Paint getColor(){
        return this.attributes.getColor();
    }
    public int getType(){return this.type;}


}
