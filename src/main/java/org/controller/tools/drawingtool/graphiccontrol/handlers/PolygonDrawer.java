package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.objects.Polygon;

import java.util.ArrayList;

public class PolygonDrawer implements EventHandler<MouseEvent> {

    private final ArrayList<Point2D> points = new ArrayList<>();
    private final DrawingTool dt;
    private final ArrayList<Circle> circles = new ArrayList<>();


    public PolygonDrawer(DrawingTool dt){
        this.dt = dt;

    }

    public void drawPolygon(){
        if(points.size() > 2){
            double[] xPoints = new double[points.size()],
                    yPoints = new double[points.size()];
            int nPoints = points.size();

            for(int i = 0; i < points.size(); i++){
                xPoints[i]= points.get(i).getX();
                yPoints[i]= points.get(i).getY();
            }
            dt.getDb().addDrawOperation(new Polygon(xPoints,yPoints,nPoints, Color.BLACK));
        }
        resetPoints();

    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_CLICKED.equals(mouseEvent.getEventType())){
            Point2D p = new Point2D(mouseEvent.getX(),mouseEvent.getY());
            points.add(p);
            Circle c = new Circle(2.5, Color.RED);
            c.setStroke(Color.BLACK);
            c.setTranslateX(mouseEvent.getX()-2.5);
            c.setTranslateY(mouseEvent.getY()-2.5);
            circles.add(c);
            dt.getStack().getChildren().addAll(c);
        }
    }
    public void resetPoints(){
        points.clear();
        dt.getStack().getChildren().removeAll(circles);
        circles.clear();
    }
}
