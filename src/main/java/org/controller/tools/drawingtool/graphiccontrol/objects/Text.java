package org.controller.tools.drawingtool.graphiccontrol.objects;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import org.controller.tools.drawingtool.graphiccontrol.FontMetrics;

public class Text extends Shapes {

    private String content;
    private Font font;
    private FontMetrics fm;


    public Text(double minX, double minY, String content, String fontfamily , double fontsize, Paint color){
        this.minX = minX;
        this.minY = minY;
        this.content = content;
        this.font = new Font(fontfamily,fontsize);
        this.color = color;
        this.fm = new FontMetrics(this.font);
        this.width = fm.computeStringWidth(content);
        this.height = fm.getLineHeight();
        this.type = TEXT;
    }
    private Text(double minX, double minY, String content, Font font, Paint color){
        this.minX = minX;
        this.minY = minY;
        this.content = content;
        this.font = font;
        this.color = color;
        this.fm = new FontMetrics(this.font);
        this.width = fm.computeStringWidth(content);
        this.height = fm.getLineHeight();
        this.type = TEXT;
    }

    public void changeFont(String fontfamily , double fontsize){
        this.font = new Font(fontfamily,fontsize);
        this.fm = new FontMetrics(this.font);
    }

    public void changeContent(String content){
        this.content = content;

    }

    public Font getFont(){
        return this.font;
    }
    public String getContent(){
        return content;
    }

    private void drawStroke(GraphicsContext gc) {
        gc.setFont(font);
        gc.setStroke(color);
        setAttributes(gc);
        gc.strokeText(content,minX,minY);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFont(font);
        gc.setFill(color);
        setAttributes(gc);
        gc.fillText(content,minX,minY);
    }

    private void outline(GraphicsContext gc){
        drawStroke(gc);
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
        this.r = new Rotate(angle, getMidX(),getMidY());
    }

    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.text.Text txt = new javafx.scene.text.Text(content);
        txt.setFont(font);
        txt.setRotate(this.r.getAngle());
        return txt;
    }

    @Override
    public Shapes reposition(Point2D point) {
        Text txt = new Text(minX,minY,content,font,color);
        txt.minX = point.getX();
        txt.minY = point.getY();
        txt.setOpType(OpType.MOVE);
        return txt;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        double realMinY, realMaxY;
        realMinY = minY - this.fm.getAscent();
        realMaxY = minY + this.fm.getDescent();
        return postRotate.getX() >= minX && postRotate.getX() <= minX + width
                && postRotate.getY() >= realMinY && postRotate.getY() <= realMaxY;
    }

    private double getMidX(){
        return minX + fm.computeStringWidth(content)/2;
    }
    private double getMidY(){
        return minY - fm.getLineHeight()/2;

    }
    public FontMetrics getFm() {
        return fm;
    }

}
