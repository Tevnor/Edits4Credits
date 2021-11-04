package basic.editor.tools.drawtool.graphiccontrol.objects;

import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

public class Rectangle extends DrawOperation {

    private final double minX;
    private final double minY;
    private final double width;
    private final double height;

    public Rectangle(double minX, double minY, double width, double height, Paint color){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        applyRotate(gc,r);
        gc.strokeRect(minX, minY, width, height);
        resetRotation(gc);
    }

    void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        applyRotate(gc,r);
        gc.fillRect(minX, minY, width, height);
        resetRotation(gc);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if(type == DrawOperation.TYPE_STROKE){
            drawStroke(gc);
        }else if(type == DrawOperation.TYPE_FILL){
            drawFill(gc);
        }
    }

    @Override
    public void setRotation(double angle) {
        this.r = new Rotate(angle, minX+(width/2), minY+(height/2));
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Paint getColor() {
        return color;
    }

    public int getType() {
        return type;
    }


    public Rotate getR() {
        return r;
    }

}
