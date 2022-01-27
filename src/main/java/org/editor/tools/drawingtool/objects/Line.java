package org.editor.tools.drawingtool.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;
import org.editor.tools.drawingtool.attributes.AbstractGeneral;

public class Line extends Shapes {

    private final double xOne, yOne, xTwo, yTwo;

    public Line(double xOne, double yOne, double xTwo, double yTwo, AbstractGeneral attributes){
        super(Math.min(xOne,xTwo), Math.min(yOne,yTwo), Math.abs(xTwo-xOne),Math.abs(yTwo-yOne),attributes);
        this.xOne = xOne;
        this.yOne = yOne;
        this.xTwo = xTwo;
        this.yTwo = yTwo;
        setRotation(attributes.getRotation());
        this.type = Type.LINE;
    }

    @Override
    public javafx.scene.shape.Line getShapeRepresentation(){

        javafx.scene.shape.Line line = new javafx.scene.shape.Line(xOne,yOne,xTwo,yTwo);
        line.setRotate(r.getAngle());
        line.setStrokeWidth(getAttributes().getLineWidth());
        return line;
    }

    @Override
    public Shapes reposition(Point2D point) {
        Point2D oldMin, newMin, connect, newStart, newEnd;
        oldMin = new Point2D(getMinX(), getMinY());
        newMin = new Point2D(point.getX(), point.getY());
        connect = oldMin.subtract(newMin);
        newStart = getStart().subtract(connect);
        newEnd = getEnd().subtract(connect);

        Line l = new Line(newStart.getX(),newStart.getY(),newEnd.getX(),newEnd.getY(), getAttributes());
        l.setOpType(OpType.MOVE);

        return l;
    }

    @Override
    public boolean pointInside(Point2D point) {
        Point2D startEnd, startPoint, projection, projectionNormal;
        double absStartEndSq, dotSPSE, projNormalAbs, dps, dpe, absStartEnd;

        Point2D postRotate = r.inverseTransform(point.getX(),point.getY());           //if line is rotated inverse rotate (x,y) to fit coord calculation


        startEnd = getEnd().subtract(getStart());               //line start to line end vector
        startPoint = postRotate.subtract(getStart());           //line start to (x,y) vector
        absStartEnd = startEnd.magnitude();

        dotSPSE = startPoint.dotProduct(startEnd);              //dot product of startPoint and startEnd vector
        absStartEndSq = absStartEnd * absStartEnd;              //magnitude of startEnd vector

        projection = startEnd.multiply(dotSPSE/absStartEndSq);  //project startPoint onto startEnd
        projectionNormal = startPoint.subtract(projection);     //normal vector of projection
        projNormalAbs = projectionNormal.magnitude();           //magnitude of normalVector
        dps = Math.abs(postRotate.subtract(projectionNormal).distance(getStart()));   //distance projection to line start
        dpe = Math.abs(postRotate.subtract(projectionNormal).distance(getEnd()));     //distance projection to line end

        //check if (x,y) is on line with heed to line width
        return projNormalAbs <= getAttributes().getLineWidth() + 5 && dps + dpe <= absStartEnd;
    }
    @Override
    public void draw(GraphicsContext gc) {
        int[] before = getPixelsBefore(gc);
        gc.setStroke(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.strokeLine(xOne, yOne, xTwo, yTwo);
        writePixelsBelow(gc, before);
    }
    @Override
    public void drawAfterMove(GraphicsContext gc) {
        gc.setStroke(getDirectAttributes().getColor());
        setAttributesOfGc(gc);
        gc.strokeLine(xOne, yOne, xTwo, yTwo);
    }
    @Override
    public void setRotation(double angle) {

        double xMid = getMinX()+getWidth()/2;
        double yMid = getMinY()+getHeight()/2;

        this.r = new Rotate(angle, xMid, yMid);
    }
    private Point2D getStart(){
        return new Point2D(xOne,yOne);
    }
    private Point2D getEnd(){
        return new Point2D(xTwo,yTwo);
    }

}
