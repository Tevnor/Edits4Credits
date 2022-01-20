package org.controller.tools.drawingtool;

import javafx.geometry.Pos;
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

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final DrawBoard db;
    private final StackPane stack;
    private final DrawingControl dc;


    public DrawingTool(StackPane stack){

        this.canvas = new Canvas(stack.getPrefWidth(),stack.getPrefHeight());
        this.gc = canvas.getGraphicsContext2D();
        this.db = new DrawBoard(gc);
        this.stack = stack;
        this.stack.getChildren().add(this.canvas);
        StackPane.setAlignment(canvas, Pos.CENTER);

        this.dc = new DrawingControl();
        this.dc.initMarkingRect(this.stack);
        this.stack.addEventHandler(MouseEvent.ANY, dc.getMarking());

        DT_LOGGER.debug("Successfully created drawing tool");
    }


    public WritableImage getScaledDrawingSnap(SnapshotParameters sp){
       int scale = 2;
       WritableImage wi = new WritableImage(
               (int) Math.round(canvas.getWidth() * scale),
               (int) Math.round(canvas.getHeight() * scale)
       );
       sp.setTransform(Transform.scale(scale,scale));
       DT_LOGGER.debug("Successfully created scaled snapshot scaled by " + scale);
       return canvas.snapshot(sp, wi);
    }

    public DrawBoard getDb(){
        return this.db;
    }
    public GraphicsContext getGc(){
        return this.gc;
    }
    public Canvas getCanvas() {
        return canvas;
    }
    public StackPane getStack() {
        return stack;
    }
    public DrawingControl getDc() {
        return dc;
    }

    @Override
    public void apply() {

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
