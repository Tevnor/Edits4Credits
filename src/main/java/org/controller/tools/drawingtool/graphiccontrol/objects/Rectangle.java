package org.controller.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public class Rectangle extends Shapes {



    public Rectangle(double minX, double minY, double width, double height, Paint color){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.color = color;
        this.type = RECTANGLE;
    }

    void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        setAttributes(gc);
        gc.strokeRect(minX, minY, width, height);
    }

    void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        setAttributes(gc);
        gc.fillRect(minX, minY, width, height);
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
        this.r = new Rotate(angle, minX+(width/2), minY+(height/2));
    }

    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(minX,minY,width,height);
        rect.setRotate(this.r.getAngle());
        return rect;
    }

    @Override
    public Shapes reposition(Point2D point) {
        Rectangle r = new Rectangle(minX,minY,width,height,color);
        r.minX = point.getX();
        r.minY = point.getY();
        r.setOpType(OpType.MOVE);
        return r;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        return postRotate.getX() >= minX && postRotate.getX() <= minX + width
                && postRotate.getY() >= minY && postRotate.getY() <= minY + height;
    }


}
