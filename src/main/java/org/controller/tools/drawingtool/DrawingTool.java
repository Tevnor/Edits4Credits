package org.controller.tools.drawingtool;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Transform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controller.tools.EditingTools;
import org.controller.tools.drawingtool.graphiccontrol.DrawBoard;
import org.controller.tools.drawingtool.graphiccontrol.DrawingControl;


public class DrawingTool implements EditingTools {
    private static final Logger DT_LOGGER = LogManager.getLogger(DrawingTool.class.getName());

    private final Canvas canvasShapes;
    private final Canvas canvasBrush;
    private final GraphicsContext gcShapes;
    private final GraphicsContext gcBrush;
    private final Group blend;
    private final DrawBoard db;
    private final StackPane stack;
    private final DrawingControl dc;


    public DrawingTool(StackPane stack){

        canvasShapes = new Canvas(stack.getPrefWidth(),stack.getPrefHeight());
        canvasBrush = new Canvas(stack.getPrefWidth(),stack.getPrefHeight());
        gcShapes = canvasShapes.getGraphicsContext2D();
        gcBrush = canvasBrush.getGraphicsContext2D();
        blend = new Group(canvasShapes, canvasBrush);
        db = new DrawBoard(gcShapes);
        this.stack = stack;

        stack.getChildren().add(canvasShapes);
        stack.getChildren().add(canvasBrush);

        StackPane.setAlignment(canvasShapes, Pos.CENTER);
        StackPane.setAlignment(canvasBrush, Pos.CENTER);

        dc = new DrawingControl();
        dc.initMarkingRect(stack);
        stack.addEventHandler(MouseEvent.ANY, dc.getMarking());

        DT_LOGGER.debug("Successfully created drawing tool");
    }


    public WritableImage getScaledDrawingSnap(SnapshotParameters sp){
       int scale = 2;
       WritableImage wi = new WritableImage(
               (int) Math.round(canvasShapes.getWidth() * scale),
               (int) Math.round(canvasShapes.getHeight() * scale)
       );

       sp.setTransform(Transform.scale(scale,scale));
       DT_LOGGER.debug("Successfully created scaled snapshot scaled by " + scale);
       return blend.snapshot(sp, wi);
    }

    public DrawBoard getDb(){
        return db;
    }
    public GraphicsContext getGcBrush(){
        return gcBrush;
    }
    public Canvas getCanvas() {
        return canvasShapes;
    }
    public StackPane getStack() {
        return stack;
    }
    public DrawingControl getDc() {
        return dc;
    }

    public void clear(){
        db.clearBoard();
        DT_LOGGER.debug("cleared succesfully");
    }

    @Override
    public void apply() {
        DT_LOGGER.info("applied succesfully");
    }
    @Override
    public void backward() {
        db.undo();
        DT_LOGGER.debug("undid succesfully");
    }
    @Override
    public void forward() {
        db.redo();
        DT_LOGGER.debug("redid succesfully");
    }



}
