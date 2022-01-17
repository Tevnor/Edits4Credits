package org.controller.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;
import org.controller.tools.drawingtool.graphiccontrol.FontMetrics;

import static org.controller.tools.drawingtool.graphiccontrol.objects.Shapes.Type.TEXT;

public class Text extends Shapes {

    private final FontMetrics fm;


    public Text(double minX, double minY, Attributes attributes){
        this.minX = minX;
        this.minY = minY;
        this.fm = new FontMetrics(attributes.getFont());
        this.width = fm.computeStringWidth(attributes.getContent());
        this.height = fm.getLineHeight();
        this.attributes = attributes;
        setRotation(attributes.getRotation());
        this.type = TEXT;
    }

    private void drawStroke(GraphicsContext gc) {
        gc.setFont(attributes.getFont());
        gc.setStroke(attributes.getColor());
        setAttributes(gc);
        gc.strokeText(attributes.getContent(), minX,minY);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFont(attributes.getFont());
        gc.setFill(attributes.getColor());
        setAttributes(gc);
        gc.fillText(attributes.getContent(), minX,minY);
    }


    @Override
    public void draw(GraphicsContext gc) {
        writeBeforeARGB(gc);
        gc.setTextAlign(attributes.getTxtAlignment());
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
        Text txt = new Text(point.getX(),point.getY(),attributes);
        txt.setOpType(OpType.MOVE);
        return txt;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        double realMinY, realMaxY;
        realMinY = minY - fm.getAscent();
        realMaxY = minY + fm.getDescent();
        return postRotate.getX() >= minX && postRotate.getX() <= minX + width
                && postRotate.getY() >= realMinY && postRotate.getY() <= realMaxY;
    }

    private double getMidX(){
        return minX + fm.computeStringWidth(attributes.getContent())/2;
    }
    private double getMidY(){
        return minY + fm.getDescent() - fm.getLineHeight()/2;
    }
    public FontMetrics getFm() {
        return fm;
    }

}
