package org.editor.tools.drawingTool.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.effect.Effect;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.drawingTool.DrawOp;
import org.editor.tools.drawingTool.attributes.AbstractGeneral;
import org.editor.tools.drawingTool.attributes.General;


public abstract class Shapes extends DrawOp {
    private static final Logger SH_LOGGER = LogManager.getLogger(Shapes.class.getName());
    public enum ShapeType {
        ARC, CIRCLE, ELLIPSES, LINE, POLYGON, RECTANGLE, ROUNDED_RECT, TEXT
    }
    private final double minX, minY, width, height;
    protected Rotate r = new Rotate(0,0,0);
    protected ShapeType shapeType;
    private AbstractGeneral attributes;

    protected Shapes(double minX, double minY, double width, double height, AbstractGeneral attributes){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.attributes = attributes;
    }
    protected Shapes(double minX, double minY, double width, double height){
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
    public ShapeType getType(){return shapeType;}
    public Rotate getR(){
        return new Rotate(r.getAngle(),r.getPivotX(),r.getPivotY());
    }
}
