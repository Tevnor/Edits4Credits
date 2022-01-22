package org.controller.tools.drawingtool.graphiccontrol;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

import java.nio.IntBuffer;

public abstract class DrawOp {
    public enum OpType{
        DRAW,
        MOVE
    }

    private OpType opType = OpType.DRAW;
    private final SnapshotParameters sp = new SnapshotParameters();
    private WritableImage snapshot;
    private int[] before;
    private PixelReader pr;
    private final WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
    private ShapePixelMap pixelsBelow;
    private boolean visible = true;
    private int moveReference = -1;

    public abstract void draw(GraphicsContext gc);
    public abstract void drawAfterMove(GraphicsContext gc);

    public void writeBeforeARGB(GraphicsContext gc){
        sp.setFill(Color.TRANSPARENT);
        Canvas c = gc.getCanvas();
        snapshot = new WritableImage((int)c.getWidth(),(int)c.getHeight());
        WritableImage wi = c.snapshot(sp,snapshot);
        pr = wi.getPixelReader();

        int canWidth = (int) Math.round(gc.getCanvas().getWidth());
        int canHeight = (int) Math.round(gc.getCanvas().getHeight());

        before = new int[canWidth*canHeight];

        pr.getPixels(0, 0, canWidth, canHeight, format, before, 0, canWidth);
    }
    public void writeChangeARGB(GraphicsContext gc){
        Canvas c = gc.getCanvas();
        WritableImage wi = c.snapshot(sp,snapshot);
        pr = wi.getPixelReader();
        pixelsBelow = new ShapePixelMap();

        int canWidth = (int) Math.round(gc.getCanvas().getWidth());
        int canHeight = (int) Math.round(gc.getCanvas().getHeight());
        int[] after = new int[canWidth*canHeight];

        pr.getPixels(0, 0, canWidth, canHeight, format, after, 0, canWidth);

        for(int i = 0; i < after.length; i++){
            if(after[i]-before[i]!= 0) {
                pixelsBelow.put(i, before[i]);
            }
        }
        this.before = null;

    }

    public ShapePixelMap getPixelsBelow() {
        return new ShapePixelMap(pixelsBelow);
    }
    public OpType getOpType(){
        return opType;
    }
    public int getMoveReference(){
        return this.moveReference;
    }
    public boolean isVisible() {
        return visible;
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }
    protected void setVisible(boolean vis){
        this.visible = vis;
    }
    public void setReference(int i){
        this.moveReference = i;
    }

}
