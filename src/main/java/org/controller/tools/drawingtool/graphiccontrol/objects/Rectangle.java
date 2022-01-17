package org.controller.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;

import static org.controller.tools.drawingtool.graphiccontrol.objects.Shapes.Type.RECTANGLE;

public class Rectangle extends Shapes {



    public Rectangle(double minX, double minY, double width, double height, Attributes attributes){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.attributes = attributes;
        setRotation(attributes.getRotation());
        this.type = RECTANGLE;
    }

    void drawStroke(GraphicsContext gc) {
        gc.setStroke(attributes.getColor());
        setAttributes(gc);
        gc.strokeRect(minX, minY, width, height);
    }

    void drawFill(GraphicsContext gc) {
        gc.setFill(attributes.getColor());
        setAttributes(gc);
        gc.fillRect(minX, minY, width, height);
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
    public void drawAfterMove(GraphicsContext gc) {
        if(attributes.isFill()){
            drawFill(gc);
        }else{
            drawStroke(gc);
        }
    }

    @Override
    public void setRotation(double angle) {
        this.r = new Rotate(angle, minX+(width/2), minY+(height/2));
    }

    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(width,height);
        rect.setRotate(this.r.getAngle());
        return rect;
    }

    @Override
    public Shapes reposition(Point2D point) {
        Rectangle r = new Rectangle(point.getX(),point.getY(),width,height,attributes);
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
