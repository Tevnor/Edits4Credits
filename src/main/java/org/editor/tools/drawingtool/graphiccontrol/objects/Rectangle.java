package org.editor.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.editor.tools.drawingtool.graphiccontrol.attributes.AbstractGeneral;

public class Rectangle extends Shapes {

    public Rectangle(double minX, double minY, double width, double height, AbstractGeneral attributes){
        super(minX,minY,width,height,attributes);
        setRotation(attributes.getRotation());
        this.type = Type.RECTANGLE;
    }
    Rectangle(double minX, double minY, double width, double height) {
        super(minX, minY, width, height);
    }

    void drawStroke(GraphicsContext gc) {
        gc.setStroke(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.strokeRect(getMinX(), getMinY(), getWidth(), getHeight());
    }
    void drawFill(GraphicsContext gc) {
        gc.setFill(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.fillRect(getMinX(), getMinY(), getWidth(), getHeight());
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
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(getWidth(),getHeight());
        rect.setRotate(this.r.getAngle());
        return rect;
    }
    @Override
    public Shapes reposition(Point2D point) {
        Rectangle r = new Rectangle(point.getX(),point.getY(),getWidth(),getHeight(),getAttributes());
        r.setOpType(OpType.MOVE);
        return r;
    }
    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        return postRotate.getX() >= getMinX() && postRotate.getX() <= getMinX() + getWidth()
                && postRotate.getY() >= getMinY() && postRotate.getY() <= getMinY() + getHeight();
    }


}
