package org.editor.tools.drawingtool.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.editor.tools.drawingtool.attributes.TextAttributes;

import static org.editor.tools.drawingtool.objects.Shapes.Type.TEXT;

public class Text extends Shapes {

    private final TextAttributes attributes;

    public Text(double minX, double minY, TextAttributes attributes){
        super(minX,minY,attributes.getFm().computeStringWidth(attributes.getContent()),
                attributes.getFm().getLineHeight());
        this.attributes = attributes;
        setRotation(attributes.getRotation());
        this.type = TEXT;
    }

    private void drawStroke(GraphicsContext gc) {
        setTextAttributes(gc);
        setAttributesOfGc(gc);
        gc.setStroke(attributes.getColor());
        gc.strokeText(attributes.getContent(), getMinX(),getMinY());
    }

    private void drawFill(GraphicsContext gc) {
        setTextAttributes(gc);
        setAttributesOfGc(gc);
        gc.setFill(attributes.getColor());
        gc.fillText(attributes.getContent(), getMinX(),getMinY());
    }

    private void setTextAttributes(GraphicsContext gc){
        gc.setFont(attributes.getFont());
        gc.setTextAlign(attributes.getTxtAlignment());
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
    public void drawAfterMove(GraphicsContext gc) {
        if(attributes.isFill()){
            drawFill(gc);
        }else{
            drawStroke(gc);
        }
    }
    @Override
    public void setRotation(double angle) {
        this.r = new Rotate(angle, getMidX(),getMidY());
    }
    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.text.Text txt = new javafx.scene.text.Text(attributes.getContent());
        txt.setFont(attributes.getFont());
        txt.setRotate(this.r.getAngle());
        return txt;
    }
    @Override
    public Shapes reposition(Point2D point) {
        Text txt = new Text(point.getX(), point.getY(), new TextAttributes(attributes, attributes.getContent(),
                attributes.getFont(), attributes.getTxtAlignment()));
        txt.setOpType(OpType.MOVE);
        return txt;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        double realMinY, realMaxY;
        realMinY = getMinY() - attributes.getFm().getAscent();
        realMaxY = getMinY() + attributes.getFm().getDescent();
        return postRotate.getX() >= getMinX() && postRotate.getX() <= getMinX() + getWidth()
                && postRotate.getY() >= realMinY && postRotate.getY() <= realMaxY;
    }

    @Override
    public TextAttributes getAttributes(){
        return new TextAttributes(attributes, attributes.getContent(),
                attributes.getFont(), attributes.getTxtAlignment());
    }
    private double getMidX(){
        return getMinX() + attributes.getFm().computeStringWidth(attributes.getContent())/2;
    }
    private double getMidY(){
        return getMinY() + attributes.getFm().getDescent() - attributes.getFm().getLineHeight()/2;
    }


}
