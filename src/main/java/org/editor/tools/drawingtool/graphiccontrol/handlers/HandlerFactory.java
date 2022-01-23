package org.editor.tools.drawingtool.graphiccontrol.handlers;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.editor.tools.drawingtool.DrawingTool;

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
        TEXT,
        PATH,
        ERASER
    }

    public HandlerFactory(DrawingTool dt){
        this.dt = dt;
    }
    public DrawHandler getHandler(Handler handler){
        switch(handler){

            case ARC:
                return new ArcDrawer(dt);
            case CIRCLE:
                return new CircleDrawer(dt);
            case ELLIPSES:
                return new EllipsesDrawer(dt);
            case ERASER:
                return new EraserDrawer(dt);
            case LINE:
                return new LineDrawer(dt);
            case PATH:
                return new PathDrawer(dt);
            case POLYGON:
                return new PolygonDrawer(dt);
            case RECTANGLE:
                return new RectangleDrawer(dt);
            case ROUNDED_RECTANGLE:
                return new RoundedRectDrawer(dt);
            case TEXT:
                return new TextDrawer(dt);
            default:
                return null;
        }
    }
    public EventHandler<MouseEvent> getMove(){
        return new MoveHandler(dt);
    }


}
