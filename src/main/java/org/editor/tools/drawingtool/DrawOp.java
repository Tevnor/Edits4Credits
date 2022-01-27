package org.editor.tools.drawingtool;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DrawOp {
    private static final Logger DO_LOGGER = LogManager.getLogger(DrawOp.class.getName());
    public enum OpType{
        DRAW,
        MOVE
    }

    private final ShapePixelMap pixelsBelow = new ShapePixelMap();
    private OpType opType = OpType.DRAW;
    private boolean visible = true;
    private int moveReference = -1;

    protected abstract void draw(GraphicsContext gc);
    protected abstract void drawAfterMove(GraphicsContext gc);

    protected int[] getPixelsBefore(GraphicsContext gc){
        return getPixelsOfCanvas(gc);
    }
    protected void writePixelsBelow(GraphicsContext gc, int[] before){
        int[] after = getPixelsOfCanvas(gc);

        for(int i = 0; i < after.length; i++){
            if(after[i] != before[i]) {
                pixelsBelow.put(i, before[i]);
            }
        }
        DO_LOGGER.debug("written pixels below");
    }
    private int[] getPixelsOfCanvas(GraphicsContext gc){
        //init pixels and canvas ref
        Canvas c = gc.getCanvas();
        int[] pixels = new int[getCanWidth(gc )* getCanHeight(gc)];
        //write snapshot of canvas
        WritableImage snap = new WritableImage(getCanWidth(gc), getCanHeight(gc));
        snap = c.snapshot(getSp(),snap);
        PixelReader pr = snap.getPixelReader();
        //write pixels array
        pr.getPixels(0, 0, getCanWidth(gc), getCanHeight(gc),
                WritablePixelFormat.getIntArgbInstance(), pixels,0 , getCanWidth(gc));
        return pixels;
    }


    private int getCanWidth(GraphicsContext gc){
        return (int) Math.round(gc.getCanvas().getWidth());
    }
    private int getCanHeight(GraphicsContext gc){
        return (int) Math.round(gc.getCanvas().getHeight());
    }
    private SnapshotParameters getSp(){
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        return sp;
    }
    protected ShapePixelMap getPixelsBelow() {
        return new ShapePixelMap(pixelsBelow);
    }
    protected OpType getOpType(){
        return opType;
    }
    protected int getMoveReference(){
        return this.moveReference;
    }
    protected boolean isVisible() {
        return visible;
    }

    protected void setOpType(OpType opType) {
        this.opType = opType;
    }
    protected void setVisible(boolean vis){
        this.visible = vis;
    }
    protected void setReference(int i){
        this.moveReference = i;
    }

}
