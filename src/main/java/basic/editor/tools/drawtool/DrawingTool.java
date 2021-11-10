package basic.editor.tools.drawtool;

import basic.editor.tools.EditingTools;
import basic.editor.tools.drawtool.graphiccontrol.DrawBoard;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;




public class DrawingTool implements EditingTools {
    private Canvas canvas;
    private GraphicsContext gc;
    private DrawBoard db;

    public DrawingTool(Canvas canvas){

        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.db = new DrawBoard(gc);

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
