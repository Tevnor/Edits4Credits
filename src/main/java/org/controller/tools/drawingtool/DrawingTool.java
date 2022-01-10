package org.controller.tools.drawingtool;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.controller.tools.EditingTools;
import org.controller.tools.drawingtool.graphiccontrol.DrawBoard;
import org.controller.tools.drawingtool.graphiccontrol.DrawingControl;


public class DrawingTool implements EditingTools {
    private Canvas canvas;
    private GraphicsContext gc;
    private DrawBoard db;
    private StackPane stack;
    private DrawingControl dc;


    public DrawingTool(Canvas canvas, StackPane stack){

        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.db = new DrawBoard(gc);
        this.stack = stack;
        this.dc = new DrawingControl();

        this.dc.initMarkingRect(this.stack);
        this.stack.addEventHandler(MouseEvent.ANY, dc.getMarking());
    }

    public DrawBoard getDb(){
        return this.db;
    }
    public GraphicsContext getGc(){
        return this.gc;
    }
    public Canvas getCanvas() {
        return canvas;
    }
    public StackPane getStack() {
        return stack;
    }
    public DrawingControl getDc() {
        return dc;
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
