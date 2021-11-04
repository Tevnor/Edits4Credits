package basic.editor.tools.drawtool.graphiccontrol.objects;

import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

public class Polygon extends DrawOperation {

    private double[] xPoints;
    private double[] yPoints;
    private int nPoints;
    private double minX, maxX, minY, maxY;
    private boolean closed = true;

    public Polygon(double[] xPoints, double[] yPoints, int nPoints, Paint color) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.nPoints = nPoints;
        this.color = color;

        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;

        for(double x : xPoints){
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        this.minX = minX;
        this.maxX = maxX;

        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for(double y : yPoints){
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        this.minY = minY;
        this.maxY = maxY;


    }



    public void changeClosedAttribute(){
        this.closed = !closed;
    }

    private double getWidth(){
        return (maxX-minX);
    }
    private double getHeight(){
        return (maxY-minY);
    }


    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        applyRotate(gc,r);

        if(closed){
            gc.strokePolygon(xPoints,yPoints,nPoints);
        }else{
            gc.strokePolyline(xPoints,yPoints,nPoints);
        }

        resetRotation(gc);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        applyRotate(gc,r);
        gc.fillPolygon(xPoints,yPoints,nPoints);
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
        this.r = new Rotate(angle, minX+(getWidth()/2), minY+(getHeight()/2));
    }
}
