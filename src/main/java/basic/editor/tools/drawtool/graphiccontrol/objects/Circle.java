package basic.editor.tools.drawtool.graphiccontrol.objects;

import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Circle extends DrawOperation {

    private final double minX, minY, radius, diameter;


    public Circle(double minX, double minY, double radius, Paint color) {
        this.minX = minX;
        this.minY = minY;
        this.radius = radius;
        this.color = color;
        this.diameter = 2*radius;
    }


    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        gc.strokeOval(minX, minY, diameter, diameter);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(minX, minY, diameter, diameter);
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

    }
}
