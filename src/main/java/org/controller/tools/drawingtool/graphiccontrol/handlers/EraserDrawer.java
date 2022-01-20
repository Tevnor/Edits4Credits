package org.controller.tools.drawingtool.graphiccontrol.handlers;

import javafx.event.EventHandler;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.controller.tools.drawingtool.DrawingTool;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;


public class EraserDrawer implements EventHandler<MouseEvent> {

    private final DrawingTool dt;
    private final Attributes attributes;
    private final PixelWriter pw;

    public EraserDrawer(DrawingTool dt, Attributes attributes){
        this.dt = dt;
        this.attributes = attributes;
        this.pw = dt.getGc().getPixelWriter();
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType()) || MouseEvent.MOUSE_DRAGGED.equals(mouseEvent.getEventType())) {
            double width = attributes.getEraserSize();
            if (attributes.isEraserSquare()) {
                dt.getGc().clearRect(mouseEvent.getX() - width / 2, mouseEvent.getY() - width / 2, width, width);
            } else {
                deleteCircle(mouseEvent.getX(), mouseEvent.getY(), width / 2);
            }
        }
    }

    private void deleteCircle(double pX, double pY, double radius){
        int minX = (int)Math.round(pX-radius);
        int minY = (int)Math.round(pY-radius);

        for(int y = 0; y < 2* radius; y++){
            for(int x = 0; x < 2* radius; x++){
                if(radius * radius >= (x-radius) * (x-radius) + (y-radius) * (y-radius)){
                    pw.setColor(x + minX,y + minY,Color.TRANSPARENT);
                }
            }
        }
    }
}
