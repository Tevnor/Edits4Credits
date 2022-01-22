package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.effect.Effect;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.DrawOp;
import org.controller.tools.drawingtool.graphiccontrol.attributes.AbstractGeneral;
import org.controller.tools.drawingtool.graphiccontrol.attributes.General;


public abstract class Shapes extends DrawOp {
    public enum Type{
        ARC, CIRCLE, ELLIPSES, LINE, POLYGON, RECTANGLE, ROUNDED_RECT, TEXT
    }
    private final double minX, minY, width, height;
    protected Rotate r = new Rotate(0,0,0);
    protected Type type;
    private AbstractGeneral attributes;

    public Shapes(double minX, double minY, double width, double height, AbstractGeneral attributes){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.attributes = attributes;
    }
    public Shapes(double minX, double minY, double width, double height){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }


    protected abstract void setRotation(double angle);
    public abstract Shape getShapeRepresentation();
    public abstract Shapes reposition(Point2D point);
    public abstract boolean pointInside(Point2D point);

    private void applyRotate(GraphicsContext gc, Rotate r){
        gc.setTransform(r.getMxx(),r.getMyx(),r.getMxy(),r.getMyy(),r.getTx(),r.getTy());
    }
    void setAttributesOfGc(GraphicsContext gc){
        applyRotate(gc,r);
        gc.setGlobalAlpha(getAttributes().getAlpha());
        gc.setGlobalBlendMode(getAttributes().getBm());
        gc.setLineWidth(getAttributes().getLineWidth());
        if(getAttributes().getEffects().size() != 0){
            for(Effect e : getAttributes().getEffects()){
                gc.setEffect(e);
            }
        }
    }

    public double getMinX(){ return minX; }
    public double getMinY(){ return minY; }
    public double getWidth(){ return width; }
    public double getHeight(){ return height; }
    AbstractGeneral getDirectAttributes(){
        return attributes;
    }
    public AbstractGeneral getAttributes(){
        return new General(attributes);
    }
    public Type getType(){return type;}
    public Rotate getR(){
        return r;
    }
}
