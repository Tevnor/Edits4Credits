package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;

public class Arc extends Shapes {

    private double startAngle;
    private double arcExtent;
    private ArcType closure;
    private Point2D getCenter(){
        return new Point2D(minX+width/2,minY+height/2);
    }

    public Arc(double minX, double minY, double width, double height, Attributes attributes){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.attributes = attributes;
        this.startAngle = attributes.getStartAngle();
        this.arcExtent = attributes.getArcExtent();
        this.closure = attributes.getArcType();
        setRotation(attributes.getRotation());
        this.type = ARC;
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(attributes.getColor());
        setAttributes(gc);
        gc.fillArc(minX, minY, width, height, startAngle, arcExtent, closure);

    }
    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(attributes.getColor());
        setAttributes(gc);
        gc.strokeArc(minX, minY, width, height, startAngle, arcExtent, closure);

    }


    @Override
    public void draw(GraphicsContext gc) {
        writeBeforeARGB(gc);
        if(attributes.isFill()){
            drawFill(gc);
        }else{
            drawStroke(gc);
        }
        writeChangeARGB(gc);
    }
    @Override
    protected void setRotation(double angle) {
        this.r = new Rotate(angle,minX+(width/2), minY+(height/2));
    }
    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.shape.Arc arc = new javafx.scene.shape.Arc(getCenter().getX(), getCenter().getY(), width/2, height/2, startAngle, arcExtent);
        arc.setType(this.closure);
        arc.setRotate(this.r.getAngle());
        return arc;
    }
    @Override
    public Shapes reposition(Point2D point) {
        Arc a = new Arc(minX,minY,width,height,attributes);
        a.minX = point.getX();
        a.minY = point.getY();
        a.setOpType(OpType.MOVE);
        return a;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        javafx.scene.shape.Arc arc = (javafx.scene.shape.Arc) getShapeRepresentation();

        if(this.closure == ArcType.OPEN){
            arc.setType(ArcType.ROUND);
        }else{
            arc.setType(this.closure);
        }

        return arc.contains(postRotate);
    }
}
