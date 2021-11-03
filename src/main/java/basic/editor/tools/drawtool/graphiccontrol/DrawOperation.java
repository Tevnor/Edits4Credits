package basic.editor.tools.drawtool.graphiccontrol;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

public abstract class DrawOperation {
    
    public static int TYPE_STROKE = 0, TYPE_FILL = 1, ARCTYPE_OPEN = 0, ARCTYPE_ROUND = 1, ARCTYPE_CHORD = 2;
    protected Paint color;
    protected Rotate r = new Rotate(0,0,0);
    public abstract void draw(GraphicsContext gc);
    protected abstract void setRotation(double angle);
    protected int type = TYPE_STROKE;

    protected void resetRotation(GraphicsContext gc){
        gc.setTransform(0,0,0,0,0,0);
    }

    protected void applyRotate(GraphicsContext gc,Rotate r){
        gc.setTransform(r.getMxx(),r.getMyx(),r.getMxy(),r.getMyy(),r.getTx(),r.getTy());
    }
    public void changeType(int changedType){

        if(changedType == DrawOperation.TYPE_STROKE){
            this.type = DrawOperation.TYPE_STROKE;
        }else{
            this.type = DrawOperation.TYPE_FILL;
        }
    }

}
