package org.editor.tools.drawingtool;

import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.drawingtool.attributes.TextAttributes;
import org.editor.tools.drawingtool.objects.*;

import java.util.List;

public class MovingControl {
    private static final Logger MC_LOGGER = LogManager.getLogger(MovingControl.class.getName());

    private Shapes preSelectedShape;
    private Shape movingShape;
    private Point2D offset, end;
    private int moveRef;


    public boolean overShape(Point2D point, DrawBoard db){
        MC_LOGGER.debug("overShape() entered");
        boolean overShape = false;
        List<DrawOp> op = db.getOperations();
        double minX, maxX, minY, maxY;
        int i = op.size()-1;

        if(point == null){
            MC_LOGGER.warn("point passed to overShape() is null");
            return false;
        }
        if(db.getOperations().size() == 0){
            MC_LOGGER.debug("no shapes in history");
            return false;
        }
        while(!overShape){
            if(!(op.get(i) instanceof Clear)) {
                Shapes shape = (Shapes) op.get(i);
                Rotate r = shape.getR();

                if(shape instanceof Text) {
                    Text txt = (Text) shape;
                    minX = txt.getMinX();
                    maxX = minX + txt.getWidth();
                    minY = txt.getMinY() - txt.getAttributes().getFm().getAscent();
                    maxY = txt.getMinY() + txt.getAttributes().getFm().getDescent();
                }else{
                    minX = shape.getMinX();
                    maxX = minX + shape.getWidth();
                    minY = shape.getMinY();
                    maxY = minY + shape.getHeight();
                }

                Point2D postR = r.inverseTransform(point.getX(),point.getY());
                if (postR.getX() >= minX && postR.getX() <= maxX &&
                        postR.getY() >= minY && postR.getY() <= maxY && shape.isVisible()) {
                    overShape = true;
                    moveRef = i;
                    preSelectedShape = (Shapes) op.get(i);
                    if (!overTrueShape(point)) {
                        overShape = false;
                        moveRef = 0;
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
    private boolean overTrueShape(Point2D point){
        MC_LOGGER.debug("overTrueShape() entered");
        switch(preSelectedShape.getType()){
            case ARC:
                return checkArc(point);
            case CIRCLE:
                return checkCircle(point);
            case ELLIPSES:
                return checkEllipses(point);
            case LINE:
                return checkLine(point);
            case POLYGON:
                return checkPolygon(point);
            case RECTANGLE:
                return checkRectangle(point);
            case TEXT:
                return checkText(point);
            case ROUNDED_RECT:
                return checkRoundRect(point);
        }
        MC_LOGGER.warn("error in type of selected shape");
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
        if(preSelectedShape != null){
            this.offset = point.subtract(preSelectedShape.getMinX(), preSelectedShape.getMinY());
            MC_LOGGER.debug("setOffset() set to x: " + offset.getX() + " y: " + offset.getY());
            this.end = new Point2D(preSelectedShape.getMinX(), preSelectedShape.getMinY());
        }
    }

    public void initMovingShape(){
        MC_LOGGER.debug("initMovingShape() entered");
        if(preSelectedShape != null){
            movingShape = preSelectedShape.getShapeRepresentation();
            movingShape.setFill(Color.TRANSPARENT);
            movingShape.setStroke(Color.MAGENTA);
        }
    }
    public void positionMovingShape(Point2D p){
        MC_LOGGER.debug("positionMovingShape() entered");
        if(preSelectedShape != null) {
            if (preSelectedShape instanceof Text) {
                movingShape.setTranslateY(p.getY() - offset.getY() - ((TextAttributes) (preSelectedShape.getAttributes())).getFm().getAscent());
                movingShape.setTranslateX(p.getX() - offset.getX());
            } else {
                movingShape.setTranslateX(p.getX() - offset.getX());
                movingShape.setTranslateY(p.getY() - offset.getY());
            }
            end = p.subtract(offset);
        }
    }

    public void addMovingShape(StackPane s){
        MC_LOGGER.debug("addMovingShape() entered");
        if(movingShape != null){
            s.getChildren().addAll(movingShape);
        }
    }
    public void removeMovingShape(StackPane s){
        MC_LOGGER.debug("removeMovingShape() entered");
        if(movingShape != null){
            s.getChildren().removeAll(movingShape);
        }
    }

    public Shapes getPostSelectedShape() {
        MC_LOGGER.debug("getPostSelectedShape() entered");
            if(preSelectedShape != null) {
            Shapes shape = preSelectedShape.reposition(end);
            shape.setReference(moveRef);
            this.moveRef = 0;
            return shape;
            }
        return null;
    }
    public void resetMovingControl(){
        this.preSelectedShape = null;
        this.movingShape = null;
        this.offset = null;
        this.end = null;
        MC_LOGGER.debug("reset() fulfilled");
    }
}


