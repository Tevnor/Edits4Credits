package org.editor.tools.drawingtool.handlers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.drawingtool.DrawingTool;

public class HandlerFactory {
    private static final Logger HF_LOGGER = LogManager.getLogger(HandlerFactory.class.getName());

    public enum DrawHandlerType {
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

    public HandlerFactory(){

    }
    public org.editor.tools.drawingtool.handlers.DrawHandler getHandler(DrawingTool dt, DrawHandlerType drawHandlerType){
        switch(drawHandlerType){

            case ARC:
                HF_LOGGER.debug("returned ArcDrawer");
                return new ArcDrawer(dt);
            case CIRCLE:
                HF_LOGGER.debug("returned CircleDrawer");
                return new CircleDrawer(dt);
            case ELLIPSES:
                HF_LOGGER.debug("returned EllipsesDrawer");
                return new EllipsesDrawer(dt);
            case ERASER:
                HF_LOGGER.debug("returned EraserDrawer");
                return new EraserDrawer(dt);
            case LINE:
                HF_LOGGER.debug("returned LineDrawer");
                return new LineDrawer(dt);
            case PATH:
                HF_LOGGER.debug("returned PathDrawer");
                return new PathDrawer(dt);
            case POLYGON:
                HF_LOGGER.debug("returned PolygonDrawer");
                return new PolygonDrawer(dt);
            case RECTANGLE:
                HF_LOGGER.debug("returned RectangleDrawer");
                return new RectangleDrawer(dt);
            case ROUNDED_RECTANGLE:
                HF_LOGGER.debug("returned RoundedRectDrawer");
                return new RoundedRectDrawer(dt);
            case TEXT:
                HF_LOGGER.debug("returned TextDrawer");
                return new TextDrawer(dt);
            default:
                HF_LOGGER.error("Invalid Handler of HandlerFactory");
                return null;
        }
    }
    public MoveHandler getMove(DrawingTool dt){
        HF_LOGGER.debug("returned MoveHandler");
        return new MoveHandler(dt);
    }


}
