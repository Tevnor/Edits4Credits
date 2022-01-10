package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.scene.canvas.GraphicsContext;
import org.controller.tools.drawingtool.graphiccontrol.DrawOp;

public class Clear extends DrawOp {

    public Clear(){

    }

    @Override
    public void draw(GraphicsContext gc) {
        writeBeforeARGB(gc);
        gc.clearRect(0,0,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
        writeChangeARGB(gc);
    }

}