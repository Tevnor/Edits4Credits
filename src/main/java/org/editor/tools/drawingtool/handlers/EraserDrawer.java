package org.editor.tools.drawingtool.handlers;

import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.editor.tools.drawingtool.DrawingTool;
import org.editor.tools.drawingtool.attributes.AbstractGeneral;
import org.editor.tools.drawingtool.attributes.EraserAttributes;


public class EraserDrawer implements DrawHandler {
    private final DrawingTool dt;
    private Shape floater;
    private double offset = 0;
    private EraserAttributes attributes;
    private final PixelWriter pw;


    public EraserDrawer(DrawingTool dt){
        this.dt = dt;
        this.pw = dt.getGcBrush().getPixelWriter();
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType()) || MouseEvent.MOUSE_DRAGGED.equals(mouseEvent.getEventType())) {
            double width = attributes.getSize();
            if (attributes.isCircle()) {
                deleteCircle(mouseEvent.getX(), mouseEvent.getY(), width / 2);
            } else {
                deleteRectangle(mouseEvent.getX(), mouseEvent.getY(), width);
            }
        }
        if(MouseEvent.MOUSE_MOVED.equals(mouseEvent.getEventType()) || MouseEvent.MOUSE_DRAGGED.equals(mouseEvent.getEventType())){
            floater.setTranslateX(mouseEvent.getX()-offset);
            floater.setTranslateY(mouseEvent.getY()-offset);
        }
        if(MouseEvent.MOUSE_ENTERED.equals(mouseEvent.getEventType())){
            floater.setVisible(true);
        }else if(MouseEvent.MOUSE_EXITED.equals(mouseEvent.getEventType())){
            floater.setVisible(false);
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
        System.out.println("circ");
    }
    private void deleteRectangle(double pX, double pY, double width){
        int minX = (int)Math.round(pX-width/2);
        int minY = (int)Math.round(pY-width/2);

        for(int y = 0; y < width; y++){
            for(int x = 0; x < width; x++){
                    pw.setColor(x + minX,y + minY,Color.TRANSPARENT);
            }
        }

    }
    @Override
    public void setAttributes(AbstractGeneral attributes) {
        this.attributes = (EraserAttributes) attributes;
        if(this.attributes.isCircle()){
            floater = new Circle(this.attributes.getSize()/2);
        }else{
            floater = new Rectangle(this.attributes.getSize(),this.attributes.getSize());
        }
        offset = this.attributes.getSize()/2;
        floater.setFill(Color.TRANSPARENT);
        floater.setStroke(Color.rgb(180,180,180,0.5));
        dt.getStack().getChildren().add(floater);
    }
    public void reset(){
        if(floater != null){
            dt.getStack().getChildren().removeAll(floater);
        }
    }

}
