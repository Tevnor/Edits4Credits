package basic.editor.tools.drawtool.graphiccontrol.objects;

import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;

public class Arc extends DrawOperation {

    private double minX;
    private double minY;
    private double width;
    private double height;
    private double startAngle;
    private double arcExtent;
    private ArcType closure = null;


    public Arc(double minX, double minY, double width, double height, double startAngle, double arcExtent, Paint color){
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.startAngle = startAngle;
        this.arcExtent = arcExtent;
        this.color = color;

    }

    public void changeClosure(int arctype){

        switch(arctype){

            case(0):
                this.closure = ArcType.OPEN;
                break;
            case(1):
                this.closure = ArcType.ROUND;
                break;
            case(2):
                this.closure = ArcType.CHORD;
                break;
        }
    }



    private void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        applyRotate(gc,r);
        gc.fillArc(minX, minY, width, height, startAngle, arcExtent, closure);
        resetRotation(gc);
    }
    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        applyRotate(gc,r);
        gc.strokeArc(minX, minY, width, height, startAngle, arcExtent, closure);
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
    protected void setRotation(double angle) {
        this.r = new Rotate(angle,minX+(width/2), minY+(height/2));
    }
}
