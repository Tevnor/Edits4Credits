package basic.editor.tools.drawtool.graphiccontrol.objects;

import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

public class Ellipse extends DrawOperation {

    private final double minX, minY;
    private final double width;
    private final double height;


    public Ellipse(double minX, double minY, double width, double height, Paint color) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.color = color;
    }


    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        setAttributes(gc);
        gc.strokeOval(minX, minY, width, height);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        setAttributes(gc);
        gc.fillOval(minX, minY, width, height);
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
}
