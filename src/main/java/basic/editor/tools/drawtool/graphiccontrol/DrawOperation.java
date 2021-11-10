package basic.editor.tools.drawtool.graphiccontrol;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

public abstract class DrawOperation {
    
    public static int TYPE_STROKE = 0, TYPE_FILL = 1, ARCTYPE_OPEN = 0, ARCTYPE_ROUND = 1, ARCTYPE_CHORD = 2;
    protected Paint color;
    protected Rotate r = new Rotate(0,0,0);
    protected int type = TYPE_STROKE;
    protected double transparency = 1;
    protected BlendMode bm = BlendMode.SRC_OVER;
    protected Effect effect = null;


    public abstract void draw(GraphicsContext gc);
    protected abstract void setRotation(double angle);
    protected void applyRotate(GraphicsContext gc,Rotate r){
        gc.setTransform(r.getMxx(),r.getMyx(),r.getMxy(),r.getMyy(),r.getTx(),r.getTy());
    }

    public void changeType(int changedType){

        if(changedType == DrawOperation.TYPE_STROKE){
            this.type = DrawOperation.TYPE_STROKE;
        }else if (changedType == DrawOperation.TYPE_FILL){
            this.type = DrawOperation.TYPE_FILL;
        }
    }

    public void setTransparency(double transparency){

        if(transparency >= 0 && transparency <= 1){
            this.transparency = transparency;
        }
    }

    public void setBm(BlendMode b){
        this.bm = b;
    }
    public void setEffect(Effect e){
        this.effect = e;
    }


    public void setAttributes(GraphicsContext gc){
        applyRotate(gc,r);
        gc.setGlobalAlpha(transparency);
        gc.setGlobalBlendMode(bm);
        gc.setEffect(effect);
    }

}
