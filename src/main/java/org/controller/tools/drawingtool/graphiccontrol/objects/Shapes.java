package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.DrawOp;


public abstract class Shapes extends DrawOp {
    public static final int TYPE_STROKE = 0, TYPE_FILL = 1, ARCTYPE_OPEN = 0, ARCTYPE_ROUND = 1, ARCTYPE_CHORD = 2,
            ARC = 0, CIRCLE = 1, CLEAR = 2, ELLIPSES = 3, LINE = 4, POLYGON = 5, RECTANGLE = 6,
            ROUNDED_RECT = 7, TEXT = 8;
    protected double minX, minY, width, height;
    protected Paint color;
    protected Rotate r = new Rotate(0,0,0);
    protected int drawingType = TYPE_FILL;
    protected int type;
    private double transparency = 1;
    private BlendMode bm = BlendMode.SRC_OVER;
    private Effect effect = null;





    protected abstract void setRotation(double angle);
    public abstract Shape getShapeRepresentation();
    public abstract Shapes reposition(Point2D point);
    public abstract boolean pointInside(Point2D point);

    protected void applyRotate(GraphicsContext gc, Rotate r){
        gc.setTransform(r.getMxx(),r.getMyx(),r.getMxy(),r.getMyy(),r.getTx(),r.getTy());
    }

    public void changeType(int changedType){
        if(changedType == Shapes.TYPE_STROKE){
            this.drawingType = Shapes.TYPE_STROKE;
        }else if (changedType == Shapes.TYPE_FILL){
            this.drawingType = Shapes.TYPE_FILL;
        }
    }

    public void setTransparency(double transparency){
        if(transparency >= 0 && transparency <= 1){
            this.transparency = transparency;
        }
    }

    public void setBm(BlendMode b){
        this.bm = b;
    }
    public void setEffect(Effect e){
        this.effect = e;
    }


    public void setAttributes(GraphicsContext gc){
        applyRotate(gc,r);
        gc.setGlobalAlpha(transparency);
        gc.setGlobalBlendMode(bm);
        gc.setEffect(effect);
    }

    public double getMinX(){ return this.minX; }
    public double getMinY(){ return this.minY; }
    public double getWidth(){ return this.width; }
    public double getHeight(){ return this.height; }
    public Paint getColor(){
        return this.color;
    }
    public Rotate getR(){return this.r;}
    public int getType(){return this.type;}


}
