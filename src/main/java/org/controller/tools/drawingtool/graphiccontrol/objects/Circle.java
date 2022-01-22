package org.controller.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.controller.tools.drawingtool.graphiccontrol.attributes.AbstractGeneral;

import static org.controller.tools.drawingtool.graphiccontrol.objects.Shapes.Type.CIRCLE;

public class Circle extends Shapes {

    private final double radius, diameter;


    private Point2D getCenter(){
        return new Point2D(getMinX()+radius,getMinY()+radius);
    }
    public Circle(double minX, double minY, double radius, AbstractGeneral attributes) {
        super(minX,minY,2*radius,2*radius, attributes);
        this.radius = radius;
        this.diameter = 2*radius;
        this.type = CIRCLE;
    }


    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.strokeOval(getMinX(), getMinY(), diameter, diameter);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.fillOval(getMinX(), getMinY(), diameter, diameter);
    }


    @Override
    public void draw(GraphicsContext gc) {
        writeBeforeARGB(gc);
        if(getDirectAttributes().isFill()){
            drawFill(gc);
        }else{
            drawStroke(gc);
        }
        writeChangeARGB(gc);
    }

    @Override
    public void drawAfterMove(GraphicsContext gc) {
        if(getDirectAttributes().isFill()){
            drawFill(gc);
        }else{
            drawStroke(gc);
        }
    }

    @Override
    public void setRotation(double angle) {
    }

    public javafx.scene.shape.Circle getShapeRepresentation(){
        return new javafx.scene.shape.Circle(radius, getDirectAttributes().getColor());
    }

    @Override
    public Shapes reposition(Point2D point) {
        Circle c = new Circle(point.getX(),point.getY(), radius, getAttributes());
        c.setOpType(OpType.MOVE);
        return c;
    }
    @Override
    public boolean pointInside(Point2D point) {
        double a = point.getX() - getCenter().getX();
        double b = point.getY() - getCenter().getY();
        return radius * radius >= (a * a + b * b);
    }
}
