package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;

public class Ellipses extends Shapes {

    public Point2D getCenter(){
        return new Point2D(minX+width/2,minY+height/2);
    }


    public Ellipses(double minX, double minY, double width, double height, Attributes attributes) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.attributes = attributes;
        setRotation(attributes.getRotation());
        this.type = ELLIPSES;
    }


    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(attributes.getColor());
        setAttributes(gc);
        gc.strokeOval(minX, minY, width, height);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(attributes.getColor());
        setAttributes(gc);
        gc.fillOval(minX, minY, width, height);
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
        this.r = new Rotate(angle, minX+(width/2), minY+(height/2));
    }

    @Override
    public Shape getShapeRepresentation() {
        Ellipse ell = new Ellipse(width/2,height/2);
        ell.setRotate(r.getAngle());
        return ell;
    }

    @Override
    public Shapes reposition(Point2D point) {
        Ellipses e = new Ellipses(minX,minY,width,height,attributes);
        e.minX = point.getX();
        e.minY = point.getY();
        e.setOpType(OpType.MOVE);
        return e;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        double a = postRotate.getX() - getCenter().getX();
        double b = postRotate.getY() - getCenter().getY();
        double radX = width/2;
        double radY = height/2;
        return 1 >= (a*a)/(radX*radX) + (b*b)/(radY*radY);
    }


}
