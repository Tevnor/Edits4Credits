package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

public class Line extends Shapes {

    private double xOne, yOne, xTwo, yTwo;
    private double lineWidth = 1;

    public Line(double xOne, double yOne, double xTwo, double yTwo, Paint color){

        this.xOne = xOne;
        this.yOne = yOne;
        this.xTwo = xTwo;
        this.yTwo = yTwo;
        this.color = color;
        setDims();
        this.type = LINE;

    }

    @Override
    public javafx.scene.shape.Line getShapeRepresentation(){

        javafx.scene.shape.Line line = new javafx.scene.shape.Line(xOne,yOne,xTwo,yTwo);
        line.setRotate(r.getAngle());
        return line;
    }

    @Override
    public Shapes reposition(Point2D point) {
        Point2D oldMin, newMin, connect, newStart, newEnd;
        oldMin = new Point2D(this.minX, this.minY);
        newMin = new Point2D(point.getX(), point.getY());
        connect = oldMin.subtract(newMin);
        newStart = getStart().subtract(connect);
        newEnd = getEnd().subtract(connect);

        Line l = new Line(xOne,yOne,xTwo,yTwo,color);

        l.xOne = newStart.getX();
        l.yOne = newStart.getY();
        l.xTwo = newEnd.getX();
        l.yTwo = newEnd.getY();
        l.setDims();
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
        return projNormalAbs <= 3 && dps + dpe <= absStartEnd;
    }


    @Override
    public void draw(GraphicsContext gc) {
        writeBeforeARGB(gc);
        gc.setStroke(color);
        setAttributes(gc);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(xOne, yOne, xTwo, yTwo);
        writeChangeARGB(gc);
    }

    @Override
    public void setRotation(double angle) {

        double xMid = minX+width/2;
        double yMid = minY+height/2;

        this.r = new Rotate(angle, xMid, yMid);
    }

    private void setDims(){
        this.width = Math.abs(xTwo-xOne);
        this.height = Math.abs(yTwo-yOne);
        this.minX = Math.min(xOne,xTwo);
        this.minY = Math.min(yOne,yTwo);
    }

    public Point2D getStart(){
        return new Point2D(xOne,yOne);
    }
    public Point2D getEnd(){
        return new Point2D(xTwo,yTwo);
    }
    public double getLineWidth() {
        return lineWidth;
    }
    public void setLineWidth(double v){
        this.lineWidth = v;
    }

}
