package org.editor.tools.drawingtool.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import org.editor.tools.drawingtool.attributes.AbstractGeneral;

public class Ellipses extends Shapes {

    public Ellipses(double minX, double minY, double width, double height, AbstractGeneral attributes) {
        super(minX,minY,width,height,attributes);
        setRotation(attributes.getRotation());
        this.type = Type.ELLIPSES;
    }

    private Point2D getCenter(){
        return new Point2D(getMinX()+getWidth()/2,getMinY()+getHeight()/2);
    }

    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.strokeOval(getMinX(), getMinY(), getWidth(), getHeight());
    }
    private void drawFill(GraphicsContext gc) {
        gc.setFill(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.fillOval(getMinX(), getMinY(), getWidth(), getHeight());
    }

    @Override
    public void draw(GraphicsContext gc) {
        int[] before = getPixelsBefore(gc);
        if(getDirectAttributes().isFill()){
            drawFill(gc);
        }else{
            drawStroke(gc);
        }
        writePixelsBelow(gc, before);
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
        this.r = new Rotate(angle, getMinX()+(getWidth()/2), getMinY()+(getHeight()/2));
    }
    @Override
    public Shape getShapeRepresentation() {
        Ellipse ell = new Ellipse(getWidth()/2,getHeight()/2);
        ell.setRotate(r.getAngle());
        return ell;
    }
    @Override
    public Shapes reposition(Point2D point) {
        Ellipses e = new Ellipses(point.getX(),point.getY(),getWidth(),getHeight(),getAttributes());
        e.setOpType(OpType.MOVE);
        return e;
    }
    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        double a = postRotate.getX() - getCenter().getX();
        double b = postRotate.getY() - getCenter().getY();
        double radX = getWidth()/2;
        double radY = getHeight()/2;
        return 1 >= (a*a)/(radX*radX) + (b*b)/(radY*radY);
    }


}
