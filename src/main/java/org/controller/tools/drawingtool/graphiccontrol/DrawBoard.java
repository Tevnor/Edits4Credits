package org.controller.tools.drawingtool.graphiccontrol;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import org.controller.tools.drawingtool.graphiccontrol.objects.Clear;

import java.util.ArrayList;
import java.util.HashMap;

public class DrawBoard {
    private final ArrayList<DrawOp> operations = new ArrayList<>();
    private final GraphicsContext gc;
    private int historyIndex = -1;
    private PixelWriter pw;

    public DrawBoard(GraphicsContext gc) {
        this.gc = gc;
        pw = gc.getPixelWriter();
    }

    public void redraw() {
        DrawOp op = operations.get(historyIndex+1);
        if(op.getOpType() == DrawOp.OpType.DRAW){
            writeUndo(pw,op);
        }else if(op.getOpType() == DrawOp.OpType.MOVE){
            writeUndo(pw,op);
            operations.get(historyIndex).draw(gc);
            operations.get(historyIndex).setVisible(true);
        }



    }

    public void addDrawOperation(DrawOp op) {
        // clear history after current position
        operations.subList(historyIndex+1, operations.size()).clear();
        if(op.getOpType() == DrawOp.OpType.DRAW){
            // add new drawing operation
            operations.add(op);
            historyIndex++;
            op.draw(gc);
        }else if(op.getOpType() == DrawOp.OpType.MOVE){
            int ref = op.getMoveReference();
            if(ref != historyIndex && ref != 0){
                writeUndo(pw,operations.get(ref));
                operations.get(ref).setVisible(false);
                for(int i = ref+1; i < operations.size();i++){
                    if(operations.get(i).isVisible()){
                        operations.get(i).draw(gc);
                    }
                }
            }else{
                writeUndo(pw,operations.get(historyIndex));
                operations.get(historyIndex).setVisible(false);
            }
            historyIndex++;
            operations.add(op);
            op.draw(gc);

        }

    }

    public void undo() {
        if (historyIndex >= 0) {
            operations.get(historyIndex).setVisible(false);
            historyIndex--;
            operations.get(historyIndex).setVisible(true);
            redraw();
        }
    }

    public void redo() {
        if (historyIndex < operations.size()-1) {
            historyIndex++;
            DrawOp op = operations.get(historyIndex);
            if(op.getOpType() == DrawOp.OpType.DRAW){
                operations.get(historyIndex).setVisible(true);
                op.draw(gc);
            }else if(op.getOpType() == DrawOp.OpType.MOVE){
                operations.get(historyIndex).setVisible(true);
                writeUndo(pw,operations.get(historyIndex-1));
                operations.get(historyIndex-1).setVisible(false);
                op.draw(gc);
            }

        }
    }

    private void writeUndo(PixelWriter pw, DrawOp op){
        HashMap<Integer, Integer> values = op.getChangeValues();
        values.forEach((k,v) -> pw.setArgb((int)(k % gc.getCanvas().getWidth()), (int)(k / gc.getCanvas().getWidth()), v));
    }

    public void clearBoard(){
        addDrawOperation(new Clear());
    }
    public ArrayList<DrawOp> getOperations(){
        return this.operations;
    }
}
