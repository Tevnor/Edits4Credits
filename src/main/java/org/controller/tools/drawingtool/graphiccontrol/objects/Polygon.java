package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public class Polygon extends Shapes {

    private double[] xPoints;
    private double[] yPoints;
    private int nPoints;
    private double maxX,maxY;
    private boolean closed = true;

    public double[] points(){
        double[] points = new double[xPoints.length+yPoints.length];
        int i = 0;
        int j = 1;

        for(double x : xPoints){
            points[i] = x;
            i += 2;
        }
        for(double y : yPoints){
            points[j] = y;
            j += 2;
        }
        return points;
    }


    public Polygon(double[] xPoints, double[] yPoints, int nPoints, Paint color) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.nPoints = nPoints;
        this.color = color;
        this.type = POLYGON;

        double minX = Integer.MAX_VALUE;
        double maxX = -Integer.MAX_VALUE;

        for(double x : xPoints){
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        this.minX = minX;
        this.maxX = maxX;

        double minY = Integer.MAX_VALUE;
        double maxY = -Integer.MAX_VALUE;

        for(double y : yPoints){
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        this.minY = minY;
        this.maxY = maxY;

        this.width = maxX-minX;
        this.height = maxY-minY;

    }



    public void changeClosedAttribute(){
        this.closed = !closed;
    }

    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(color);
        setAttributes(gc);
        if(closed){
            gc.strokePolygon(xPoints,yPoints,nPoints);
        }else{
            gc.strokePolyline(xPoints,yPoints,nPoints);
        }

    }

    private void drawFill(GraphicsContext gc) {
        gc.setFill(color);
        setAttributes(gc);
        gc.fillPolygon(xPoints,yPoints,nPoints);
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
        this.r = new Rotate(angle, minX+(getWidth()/2), minY+(getHeight()/2));
    }

    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.shape.Polygon poly = new javafx.scene.shape.Polygon(this.points());
        poly.setRotate(this.r.getAngle());
        return poly;
    }

    @Override
    public Shapes reposition(Point2D point) {
        Point2D repos = point.subtract(new Point2D(minX,minY));
        double[] reposX = new double[xPoints.length], reposY = new double[yPoints.length];

        for(int i = 0;i < xPoints.length; i++){
            reposX[i] = xPoints[i] - repos.getX();
        }
        for(int i = 0;i < yPoints.length; i++){
            reposY[i] = yPoints[i] - repos.getY();
        }

        Polygon p = new Polygon(reposX,reposY, nPoints, color);
        p.setOpType(OpType.MOVE);
        return p;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        javafx.scene.shape.Polygon poly2 = new javafx.scene.shape.Polygon(points());
        return poly2.contains(postRotate);
    }
}