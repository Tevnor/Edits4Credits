package org.editor.tools.drawingtool.objects;

import javafx.scene.canvas.GraphicsContext;
import org.editor.tools.drawingtool.DrawOp;

public class Clear extends DrawOp {

    public Clear(){}

    @Override
    public void draw(GraphicsContext gc) {
        int[] before = getPixelsBefore(gc);
        gc.clearRect(0,0,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
        writePixelsBelow(gc,before);
    }

    @Override
    public void drawAfterMove(GraphicsContext gc) {
        gc.clearRect(0,0,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
    }

}
