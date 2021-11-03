package basic.editor.tools.drawtool.graphiccontrol.objects;

import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

public class Line extends DrawOperation {

    private final double xOne;
    private final double yOne;
    private final double xTwo;
    private final double yTwo;

    public Line(double xOne, double yOne, double xTwo, double yTwo, Paint color){

        this.xOne = xOne;
        this.yOne = yOne;
        this.xTwo = xTwo;
        this.yTwo = yTwo;
        this.color = color;


    }


    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        applyRotate(gc,r);
        gc.strokeLine(xOne, yOne, xTwo, yTwo);
    }

    @Override
    public void setRotation(double angle) {

        double xMid;
        double yMid;

        if(xTwo >= xOne){
            xMid = xTwo-xOne;
        }else{
            xMid = xOne-xTwo;
        }
        if(yTwo >= yOne){
            yMid = yTwo-yOne;
        }else{
            yMid = yOne-yTwo;
        }


        this.r = new Rotate(angle, xMid, yMid);
    }


}
