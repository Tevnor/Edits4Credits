package org.controller.tools.drawingtool.graphiccontrol.handlers;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;

public class HandlerFactory {
    private final DrawingTool dt;

    public enum Handler{
        ARC,
        CIRCLE,
        ELLIPSES,
        LINE,
        MOVE,
        POLYGON,
        RECTANGLE,
        ROUNDED_RECTANGLE,
        TEXT
    }

    public HandlerFactory(DrawingTool dt){
        this.dt = dt;
    }
    public EventHandler<MouseEvent> getHandler(Handler handler, Attributes attributes){
        EventHandler<MouseEvent> drawer= null;
        switch(handler){

            case ARC:
                drawer = new ArcDrawer(dt,attributes);
                break;
            case CIRCLE:
                drawer = new CircleDrawer(dt,attributes);
                break;
            case ELLIPSES:
                drawer = new EllipsesDrawer(dt,attributes);
                break;
            case LINE:
                drawer = new LineDrawer(dt,attributes);
                break;
            case MOVE:
                drawer = new MoveHandler(dt);
                break;
            case POLYGON:
                drawer = new PolygonDrawer(dt,attributes);
                break;
            case RECTANGLE:
                drawer = new RectangleDrawer(dt,attributes);
                break;
            case ROUNDED_RECTANGLE:
                drawer = new RoundedRectDrawer(dt,attributes);
                break;
            case TEXT:
                drawer = new TextDrawer(dt,attributes);
                break;
        }

        return drawer;
    }


}
