package basic.editor.tools.drawtool.graphiccontrol.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RoundedRectangle extends Rectangle{

    private double arcWidth;
    private double arcHeigth;

    public RoundedRectangle(double minX, double minY, double width, double height,double arcWidth, double arcHeight, Paint color) {
        super(minX, minY, width, height, color);
        this.arcWidth = arcWidth;
        this.arcHeigth = arcHeight;
    }

    @Override
    void drawStroke(GraphicsContext gc) {
        gc.setStroke(getColor());
        applyRotate(gc,getR());
        gc.strokeRoundRect(getMinX(),getMinY(),getWidth(),getHeight(),this.arcWidth,this.arcHeigth);
        resetRotation(gc);
    }

    @Override
    void drawFill(GraphicsContext gc){
        gc.setFill(getColor());
        applyRotate(gc,getR());
        gc.fillRoundRect(getMinX(),getMinY(),getWidth(),getHeight(),this.arcWidth,this.arcHeigth);
        resetRotation(gc);
    }


}
