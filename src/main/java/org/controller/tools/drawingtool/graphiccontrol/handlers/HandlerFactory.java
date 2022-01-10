package org.controller.tools.drawingtool.graphiccontrol.handlers;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.controller.tools.drawingtool.DrawingTool;

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
    public EventHandler<MouseEvent> getHandler(Handler handler){
        EventHandler<MouseEvent> drawer= null;
        switch(handler){

            case ARC:
                drawer = new ArcDrawer(dt);
                break;
            case CIRCLE:
                drawer = new CircleDrawer(dt);
                break;
            case ELLIPSES:
                drawer = new EllipsesDrawer(dt);
                break;
            case LINE:
                drawer = new LineDrawer(dt);
                break;
            case MOVE:
                drawer = new MoveHandler(dt);
                break;
            case POLYGON:
                drawer = new PolygonDrawer(dt);
                break;
            case RECTANGLE:
                drawer = new RectangleDrawer(dt);
                break;
            case ROUNDED_RECTANGLE:
                drawer = new RoundedRectDrawer(dt);
                break;
            case TEXT:
                drawer = new TextDrawer(dt);
                break;
        }

        return drawer;
    }


}
