package org.editor.tools.drawingtool.objects;

import javafx.scene.canvas.GraphicsContext;
import org.editor.tools.drawingtool.DrawOp;

public class Clear extends DrawOp {

    public Clear(){}

    @Override
    protected void draw(GraphicsContext gc, GraphicsContext tmp) {
        int[] before = getPixelsBefore(gc);
        gc.clearRect(0,0,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
        tmp.clearRect(0,0,tmp.getCanvas().getWidth(),tmp.getCanvas().getHeight());
        writePixelsBelowClear(gc,before);
    }

    @Override
    protected void drawAfterMove(GraphicsContext gc) {
        gc.clearRect(0,0,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
    }

}
