package org.editor.tools.drawingTool;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DrawingControl {
    private static final Logger DC_LOGGER = LogManager.getLogger(DrawingControl.class.getName());

    private javafx.scene.shape.Rectangle markingRectIn, markingRectOut;
    private Group markingRect;
    private Point2D first;
    private final EventHandler<MouseEvent> marking = new EventHandler<>() {

        @Override
        public void handle(MouseEvent event) {
            if(MouseEvent.MOUSE_PRESSED.equals(event.getEventType())){
                first = new Point2D(event.getX(),event.getY());
            }else if(MouseEvent.MOUSE_DRAGGED.equals(event.getEventType())){
                Point2D second = new Point2D(event.getX(),event.getY());
                translateMarkingRect(first,second);
            }else if(MouseEvent.MOUSE_RELEASED.equals(event.getEventType())){
                resetMarkingRect();
            }
        }
    };
    void initMarkingRect(StackPane stack){
        DC_LOGGER.debug("initMarkingRect() entered");
        markingRectIn = new javafx.scene.shape.Rectangle();
        markingRectIn.setFill(Color.rgb(0,120,215));
        markingRectIn.setOpacity(0.1);
        markingRectOut = new javafx.scene.shape.Rectangle();
        markingRectOut.setStroke(Color.rgb(0,102,204));
        markingRectOut.setFill(Color.TRANSPARENT);
        markingRectOut.setStrokeWidth(0.8);
        markingRect = new Group();
        markingRect.getChildren().addAll(markingRectOut,markingRectIn);
        stack.getChildren().addAll(markingRect);
        markingRect.toFront();
    }
    void translateMarkingRect(Point2D first, Point2D current){
        DC_LOGGER.debug("translateMarkingRect() entered");
        double[] dims = calculateMinXMinYWidthHeight(first,current);
        markingRectIn.setWidth(dims[2]);
        markingRectIn.setHeight(dims[3]);
        markingRectOut.setWidth(dims[2]);
        markingRectOut.setHeight(dims[3]);
        markingRect.setTranslateX(dims[0]);
        markingRect.setTranslateY(dims[1]);
    }
    void resetMarkingRect(){
        DC_LOGGER.debug("resetMarkingRect() entered");
        markingRectIn.setWidth(0);
        markingRectIn.setHeight(0);
        markingRectOut.setWidth(0);
        markingRectOut.setHeight(0);
    }

    public void addMarkingHandler(Node s){
        DC_LOGGER.debug("addMarkingHandler() entered");
        s.addEventHandler(MouseEvent.ANY,marking);
    }
    public void removeMarkingHandler(Node s){
        DC_LOGGER.debug("removeMarkingHandler() entered");
        s.removeEventHandler(MouseEvent.ANY, marking);
    }
    
    public double[] calculateMinXMinYWidthHeight(Point2D first, Point2D second){
        DC_LOGGER.debug("calculateMinXMinYWidthHeight() entered");
        double minX, minY, width, height;
        minX = Math.min(first.getX(),second.getX());
        width = Math.max(first.getX(),second.getX())-minX;
        minY = Math.min(first.getY(),second.getY());
        height = Math.max(first.getY(),second.getY()) - minY;
        return new double[]{minX, minY, width, height};
    }
    public double[] calculateMinXMinYRadius(Point2D first, Point2D second){
        DC_LOGGER.debug("calculateMinXMinYRadius() entered");
        double minX, minY, radius;
        minX = Math.min(first.getX(),second.getX());
        minY = Math.min(first.getY(),second.getY());
        if(minX == first.getX() && minY == first.getY()){
            radius = Math.min(second.getX() - minX, second.getY() - minY)/2;
            return new double[]{minX,minY,radius};
        }else if(minX == first.getX()){
            radius = (Math.min(second.getX() - minX, first.getY() - minY))/2;
            minY = first.getY() - 2*radius;
            return new double[]{minX,minY,radius};
        }else if(minY == first.getY()){
            radius = Math.min(first.getX() - minX, second.getY() - minY)/2;
            minX = first.getX() - 2*radius;
            return new double[]{minX,minY,radius};
        }
        radius = (Math.min(first.getX() - second.getX(), first.getY() - second.getY()))/2;
        minX = first.getX()-2*radius;
        minY = first.getY()-2*radius;
        return new double[]{minX,minY,radius};
    }
}
