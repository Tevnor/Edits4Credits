package basic.editor.tools.drawtool.graphiccontrol.objects;



import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import basic.editor.tools.drawtool.graphiccontrol.FontMetrics;

public class Text extends DrawOperation {

    private String content;
    private Font font;
    private final double minX, minY;
    private FontMetrics fm;

    public Text(double minX, double minY, String content, String fontfamily ,double fontsize, Paint color){
        this.content = content;
        this.font = new Font(fontfamily,fontsize);
        this.color = color;
        this.fm = new FontMetrics(this.font);
        this.minX = minX;
        this.minY = minY;
        this.type = DrawOperation.TYPE_FILL;
    }

    public void changeFont(String fontfamily , double fontsize){
        this.font = new Font(fontfamily,fontsize);
        this.fm = new FontMetrics(this.font);
    }

    public void changeContent(String content){
        this.content = content;

    }

    public Font getFont(){
        return this.font;
    }
    public String getContent(){
        return content;
    }
    public Paint getColor(){ return color;}

    private void drawStroke(GraphicsContext gc) {
        gc.setFont(font);
        gc.setStroke(color);
        applyRotate(gc,r);
        gc.strokeText(content,minX,minY);
        resetRotation(gc);
    }

    private void drawFill(GraphicsContext gc) {
        gc.setFont(font);
        gc.setFill(color);
        applyRotate(gc,r);
        gc.fillText(content,minX,minY);
        resetRotation(gc);
    }

    private void outline(GraphicsContext gc){
        drawStroke(gc);
    }

    @Override
    public void draw(GraphicsContext gc) {

        if(type == DrawOperation.TYPE_STROKE){
            drawStroke(gc);
        }else if(type == DrawOperation.TYPE_FILL){
            drawFill(gc);
        }
    }

    @Override
    public void setRotation(double angle) {
        this.r = new Rotate(angle, getMidX(),getMidY());
    }

    private double getMidX(){
        return minX + fm.computeStringWidth(content)/2;
    }
    private double getMidY(){
        return minY - fm.getLineHeight()/2;

    }
}
