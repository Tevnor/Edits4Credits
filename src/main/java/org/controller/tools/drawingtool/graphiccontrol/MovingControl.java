package org.controller.tools.drawingtool.graphiccontrol;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.controller.tools.drawingtool.graphiccontrol.objects.*;

import java.util.ArrayList;

public class MovingControl {


    private Shapes preSelectedShape;
    private Shape movingShape;
    private Point2D offset, end;

    public MovingControl(){
        this.preSelectedShape = null;
        this.movingShape = null;
    }


    public boolean overShape(Point2D point, DrawBoard db){
        boolean overShape = false;
        ArrayList<DrawOp> op = db.getOperations();
        double minX, maxX, minY, maxY;
        int i = op.size()-1;

        if(db.getOperations().size() == 0){
            return false;
        }

        while(!overShape){
            if(!(op.get(i) instanceof Clear)) {
                Shapes shape = (Shapes) op.get(i);

                if(shape instanceof Text) {
                    Text txt = (Text) shape;
                    minX = txt.getMinX();
                    maxX = minX + txt.getWidth();
                    minY = txt.getMinY() - txt.getFm().getAscent();
                    maxY = txt.getMinY() + txt.getFm().getDescent();
                }else{
                    minX = shape.getMinX();
                    maxX = minX + shape.getWidth();
                    minY = shape.getMinY();
                    maxY = minY + shape.getHeight();
                }

                if (point.getX() >= minX && point.getX() <= maxX &&
                        point.getY() >= minY && point.getY() <= maxY && shape.isVisible()) {
                    overShape = true;
                    preSelectedShape = (Shapes) op.get(i);
                    if (!overTrueShape(point)) {
                        overShape = false;
                    }
                }
            }

            i--;

            if(i == -1){
                break;
            }

        }

        return overShape;
    }
    public boolean overTrueShape(Point2D point){
        switch(preSelectedShape.getType()){
            case Shapes.ARC:
                return checkArc(point);
            case Shapes.CIRCLE:
                return checkCircle(point);
            case Shapes.CLEAR:
                return false;
            case Shapes.ELLIPSES:
                return checkEllipses(point);
            case Shapes.LINE:
                return checkLine(point);
            case Shapes.POLYGON:
                return checkPolygon(point);
            case Shapes.RECTANGLE:
                return checkRectangle(point);
            case Shapes.TEXT:
                return checkText(point);
            case Shapes.ROUNDED_RECT:
                return checkRoundRect(point);
        }
        return false;
    }

    private boolean checkCircle(Point2D point){
        Circle circ = (Circle) preSelectedShape;
        return circ.pointInside(point);
    }
    private boolean checkEllipses(Point2D point){
        Ellipses ell = (Ellipses) preSelectedShape;
        return ell.pointInside(point);
    }
    private boolean checkLine(Point2D point){
        Line line = (Line) preSelectedShape;
        return line.pointInside(point);
    }
    private boolean checkRectangle(Point2D point){
        Rectangle rect = (Rectangle) preSelectedShape;
        return rect.pointInside(point);
    }
    private boolean checkRoundRect(Point2D point){
        RoundedRectangle roundRect = (RoundedRectangle) preSelectedShape;
        return roundRect.pointInside(point);
    }
    private boolean checkArc(Point2D point){
        Arc arc = (Arc) preSelectedShape;
        return arc.pointInside(point);
    }
    private boolean checkPolygon(Point2D point){
        Polygon poly = (Polygon) preSelectedShape;
        return poly.pointInside(point);
    }
    private boolean checkText(Point2D point){
        Text txt = (Text) preSelectedShape;
        return txt.pointInside(point);
    }

    public void setOffset(Point2D point){
        this.offset = point.subtract(preSelectedShape.getMinX(), preSelectedShape.getMinY());
        this.end = new Point2D(preSelectedShape.getMinX(), preSelectedShape.getMinY());
    }

    public void initMovingShape(){
        movingShape = preSelectedShape.getShapeRepresentation();
        movingShape.setFill(Color.TRANSPARENT);
        movingShape.setStroke(Color.MAGENTA);
    }
    public void positionMovingShape(Point2D p){
        movingShape.setTranslateX(p.getX() - offset.getX());
        movingShape.setTranslateY(p.getY() - offset.getY());
        end = p.subtract(offset);
    }

    public void reset(){
        this.preSelectedShape = null;
        this.movingShape = null;
        this.offset = null;
    }

    public Shapes getSelectedShape(){
        return preSelectedShape;
    }
    public Shape getMovingShape() {
        return movingShape;
    }


    public Shapes getPostSelectedShape() {
        return preSelectedShape.reposition(end);
    }
}

