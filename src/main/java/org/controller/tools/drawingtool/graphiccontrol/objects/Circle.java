package org.controller.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 *
 */
public class Circle extends Shapes {

    private final double radius, diameter;


    public double getRadius() {
        return radius;
    }
    public double getDiameter() {
        return diameter;
    }
    public Point2D getCenter(){
        return new Point2D(minX+radius,minY+radius);
    }
    public Circle(double minX, double minY, double radius, Paint color) {
        this.minX = minX;
        this.minY = minY;
        this.radius = radius;
        this.color = color;
        this.diameter = 2*radius;
        this.width = diameter;
        this.height = diameter;
        this.type = CIRCLE;
    }


    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        setAttributes(gc);
        gc.strokeOval(minX, minY, diameter, diameter);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        setAttributes(gc);
        gc.fillOval(minX, minY, diameter, diameter);
    }


    @Override
    public void draw(GraphicsContext gc) {
        writeBeforeARGB(gc);
        if(drawingType == Shapes.TYPE_STROKE){
            drawStroke(gc);
        }else if(drawingType == Shapes.TYPE_FILL){
            drawFill(gc);
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
        Circle c = new Circle(minX,minY,radius,color);
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
