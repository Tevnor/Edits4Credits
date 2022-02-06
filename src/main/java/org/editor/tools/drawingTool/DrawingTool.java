package org.editor.tools.drawingTool;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.project.Project;


public class DrawingTool{
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

        stack.getChildren().addAll(blend);
        StackPane.setAlignment(blend,Pos.CENTER);

        dc = new DrawingControl();
        dc.initMarkingRect(stack);
        dc.addMarkingHandler(stack);

        DT_LOGGER.debug("Successfully created drawing tool");
    }

    public int[] getPixelBufferOfDrawing(Project project){
       SnapshotParameters sp = new SnapshotParameters();
       sp.setFill(Color.TRANSPARENT);

       int originalWidth = (int)project.getProjectWidth();
       int originalHeight = (int)project.getProjectHeight();
       int[] export = new int[originalWidth*originalHeight];
       WritableImage wi = new WritableImage(originalWidth, originalHeight);
       double scale;

       if(project.getProjectAspectRatio() >= 1){
           scale = originalWidth/canvasShapes.getWidth();
       }else{
           scale = originalHeight/canvasShapes.getHeight();
       }

       sp.setTransform(Transform.scale(scale,scale));
       blend.snapshot(sp, wi).getPixelReader().getPixels(0,0, originalWidth,originalHeight,
                WritablePixelFormat.getIntArgbInstance(), export,0, originalWidth);
        DT_LOGGER.debug("Successfully created scaled buffer scaled by " + scale);
        System.out.println();
       return export;
    }

    public DrawBoard getDb(){
        return db;
    }
    public GraphicsContext getGcBrush(){
        return gcBrush;
    }
    public Node getCanvasShapes() {
        return canvasShapes;
    }
    public Node getCanvasBrush(){
        return canvasBrush;
    }
    public StackPane getStack() {
        return stack;
    }
    public DrawingControl getDc() {
        return dc;
    }

    public void clearShapes(){
        db.clearBoard();
        DT_LOGGER.debug("cleared shapes succesfully");
    }
    public void clearBrush(){
        gcBrush.clearRect(0,0,canvasBrush.getWidth(),canvasBrush.getHeight());
        DT_LOGGER.debug("cleared shapes succesfully");
    }
    public void backward() {
        db.undo();
        DT_LOGGER.debug("undid succesfully");
    }
    public void forward() {
        db.redo();
        DT_LOGGER.debug("redid succesfully");
    }



}
