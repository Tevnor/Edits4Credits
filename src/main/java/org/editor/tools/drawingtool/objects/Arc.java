package org.editor.tools.drawingtool.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.editor.tools.drawingtool.attributes.ArcAttributes;

import static org.editor.tools.drawingtool.objects.Shapes.ShapeType.ARC;

public class Arc extends Shapes {

    private final ArcAttributes attributes;
    private Point2D getCenter(){
        return new Point2D(getMinX()+getWidth()/2,getMinY()+getHeight()/2);
    }

    public Arc(double minX, double minY, double width, double height, ArcAttributes attributes){
        super(minX,minY,width,height);
        this.attributes = attributes;
        setRotation(attributes.getRotation());
        this.shapeType = ARC;
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(attributes.getColor());
        setAttributesOfGc(gc);
        gc.fillArc(getMinX(), getMinY(), getWidth(), getHeight(),
                attributes.getStartAngle(), attributes.getArcExtent(), attributes.getArcType());

    }
    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(attributes.getColor());
        setAttributesOfGc(gc);
        gc.strokeArc(getMinX(), getMinY(), getWidth(), getHeight(),
                attributes.getStartAngle(), attributes.getArcExtent(), attributes.getArcType());

    }
    @Override
    protected void draw(GraphicsContext gc, GraphicsContext tmp) {
        int[] before = getPixelsBefore(gc);
        if(attributes.isFill()){
            drawFill(gc);
            drawFill(tmp);
        }else{
            drawStroke(gc);
            drawStroke(tmp);
        }
        writePixelsBelow(tmp, before);
    }
    @Override
    protected void drawAfterMove(GraphicsContext gc) {
        if(attributes.isFill()){
            drawFill(gc);
        }else{
            drawStroke(gc);
        }
    }

    @Override
    protected void setRotation(double angle) {
        this.r = new Rotate(angle,getMinX()+(getWidth()/2), getMinY()+(getHeight()/2));
    }
    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.shape.Arc arc = new javafx.scene.shape.Arc(getCenter().getX(), getCenter().getY(), getWidth()/2, getHeight()/2,
                attributes.getStartAngle(), attributes.getArcExtent());
        arc.setType(attributes.getArcType());
        arc.setRotate(this.r.getAngle());
        return arc;
    }
    @Override
    public Shapes reposition(Point2D point) {
        Arc a = new Arc(point.getX(),point.getY(),getWidth(),getHeight(),
                new ArcAttributes(attributes,attributes.getArcType(),attributes.getStartAngle(),attributes.getArcExtent()));
        a.setOpType(OpType.MOVE);
        return a;
    }
    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        javafx.scene.shape.Arc arc = (javafx.scene.shape.Arc) getShapeRepresentation();
        if(attributes.getArcType() == ArcType.OPEN){
            arc.setType(ArcType.ROUND);
        }else{
            arc.setType(attributes.getArcType());
        }

        return arc.contains(postRotate);
    }
    @Override
    public ArcAttributes getAttributes(){
        return new ArcAttributes(attributes,attributes.getArcType(),attributes.getStartAngle(),attributes.getArcExtent());
    }
}
