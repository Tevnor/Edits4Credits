package org.controller.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;

public class Circle extends Shapes {

    private final double radius, diameter;


    public Point2D getCenter(){
        return new Point2D(minX+radius,minY+radius);
    }
    public Circle(double minX, double minY, double radius, Attributes attributes) {
        this.minX = minX;
        this.minY = minY;
        this.radius = radius;
        this.attributes = attributes;
        this.diameter = 2*radius;
        this.width = diameter;
        this.height = diameter;
        this.type = CIRCLE;
    }


    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(attributes.getColor());
        setAttributes(gc);
        gc.strokeOval(minX, minY, diameter, diameter);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(attributes.getColor());
        setAttributes(gc);
        gc.fillOval(minX, minY, diameter, diameter);
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
    public void setRotation(double angle) {
    }


    public javafx.scene.shape.Circle getShapeRepresentation(){
        return new javafx.scene.shape.Circle(radius,color);
    }

    @Override
    public Shapes reposition(Point2D point) {
        Circle c = new Circle(minX,minY,radius,attributes);
        c.minX = point.getX();
        c.minY = point.getY();
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
