package org.editor.tools.drawingTool.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.editor.tools.drawingTool.attributes.AbstractGeneral;

public class Rectangle extends Shapes {

    public Rectangle(double minX, double minY, double width, double height, AbstractGeneral attributes){
        super(minX,minY,width,height,attributes);
        setRotation(attributes.getRotation());
        this.shapeType = ShapeType.RECTANGLE;
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
    protected void draw(GraphicsContext gc, GraphicsContext tmp) {
        int[] before = getPixelsBefore(gc);
        if(getAttributes().isFill()){
            drawFill(gc);
            drawFill(tmp);
        }else{
            drawStroke(gc);
            drawStroke(tmp);
        }
        writePixelsBelow(tmp, before);
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
    protected void setRotation(double angle) {
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
