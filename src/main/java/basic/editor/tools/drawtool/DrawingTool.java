package basic.editor.tools.drawtool;

import basic.editor.tools.EditingTools;
import basic.editor.tools.drawtool.graphiccontrol.DrawBoard;
import basic.editor.tools.drawtool.graphiccontrol.DrawOperation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;




public class DrawingTool implements EditingTools {

    private int id;
    private Canvas canvas;
    private GraphicsContext gc;
    private DrawBoard db;

    public DrawingTool(int id, double width, double height){

        this.id = id;
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        this.db = new DrawBoard(gc);

    }


    public int getId(){
        return this.id;
    }

    @Override
    public void apply() {

    }

    @Override
    public void backward() {
        db.undo();
    }

    @Override
    public void forward() {
        db.redo();
    }
}
