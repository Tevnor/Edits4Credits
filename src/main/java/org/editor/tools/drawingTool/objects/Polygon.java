package org.editor.tools.drawingTool.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.editor.tools.drawingTool.attributes.PolygonAttributes;

import java.util.Arrays;

public class Polygon extends Shapes {

    private final double[] xPoints;
    private final double[] yPoints;
    private final int nPoints;
    private final double[] dims;
    private final PolygonAttributes attributes;
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


    public Polygon(double[] dims,
                   double[] xPoints, double[] yPoints, int nPoints, PolygonAttributes attributes) {
        super(dims[0],dims[1],dims[2],dims[3]);
        this.dims = dims;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.nPoints = nPoints;
        this.attributes = attributes;
        setRotation(attributes.getRotation());
        this.shapeType = ShapeType.POLYGON;
    }

    private void drawStroke(GraphicsContext gc) {
        gc.setStroke(attributes.getColor());
        setAttributesOfGc(gc);
        if(attributes.isPolyClose()){
            gc.strokePolygon(xPoints,yPoints,nPoints);
        }else{
            gc.strokePolyline(xPoints,yPoints,nPoints);
        }

    }
    private void drawFill(GraphicsContext gc) {
        gc.setFill(attributes.getColor());
        setAttributesOfGc(gc);
        gc.fillPolygon(xPoints,yPoints,nPoints);
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
    protected void setRotation(double angle) {
        this.r = new Rotate(angle, getMinX()+(getWidth()/2), getMinY()+(getHeight()/2));
    }
    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.shape.Polygon poly = new javafx.scene.shape.Polygon(this.points());
        poly.setRotate(this.r.getAngle());
        return poly;
    }
    @Override
    public Shapes reposition(Point2D point) {
        Point2D repos = point.subtract(new Point2D(getMinX(),getMinY()));
        double[] reposDims = getReposDims(point);
        double[] reposX = new double[xPoints.length], reposY = new double[yPoints.length];

        for(int i = 0;i < xPoints.length; i++){
            reposX[i] = xPoints[i] + repos.getX();
        }
        for(int i = 0;i < yPoints.length; i++){
            reposY[i] = yPoints[i] + repos.getY();
        }
        Polygon p = new Polygon(reposDims, reposX, reposY, nPoints,
                new PolygonAttributes(attributes, attributes.isPolyClose()));
        p.setOpType(OpType.MOVE);
        return p;
    }
    private double[] getReposDims(Point2D point){
        double[] reposDims = Arrays.copyOf(dims,dims.length);
        reposDims[0] = point.getX();
        reposDims[1] = point.getY();
        return reposDims;
    }
    @Override
    public boolean pointInside(Point2D point) {
        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());
        javafx.scene.shape.Polygon poly2 = new javafx.scene.shape.Polygon(points());
        return poly2.contains(postRotate);
    }
    @Override
    public PolygonAttributes getAttributes(){
        return new PolygonAttributes(attributes, attributes.isPolyClose());
    }
}
