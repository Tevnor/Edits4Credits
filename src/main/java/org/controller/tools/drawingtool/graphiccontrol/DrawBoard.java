package org.controller.tools.drawingtool.graphiccontrol;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controller.tools.drawingtool.graphiccontrol.objects.Clear;
import org.controller.tools.drawingtool.graphiccontrol.objects.Shapes;

import java.util.ArrayList;
import java.util.HashMap;

public class DrawBoard {
    private static final Logger DB_LOGGER = LogManager.getLogger(DrawBoard.class.getName());

    private final ArrayList<DrawOp> operations = new ArrayList<>();
    private final GraphicsContext gc;
    private int historyIndex = -1;
    private final PixelWriter pw;
    private Shapes temp;
    private ArrayList<Shapes> tmp = new ArrayList<>();

    public DrawBoard(GraphicsContext gc) {
        this.gc = gc;
        pw = gc.getPixelWriter();
        DB_LOGGER.info("successfully created draw board");
    }

    private HashMap<Integer,Integer> compareSnapshots(HashMap<Integer,Integer> top, HashMap<Integer,Integer> bottom){
        HashMap<Integer,Integer> intersection = new HashMap<>(bottom);
        intersection.keySet().retainAll(top.keySet());
        return intersection;
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
        operations.subList(historyIndex+1, operations.size()).clear();  // clear history after current position
        if(op.getOpType() == DrawOp.OpType.DRAW){
            operations.add(op);                                         // add new drawing operation
            historyIndex++;
            op.draw(gc);
            DB_LOGGER.debug("add draw operation");
        }else if(op.getOpType() == DrawOp.OpType.MOVE){
            int ref = op.getMoveReference();                            //gets reference for moving operation

            if(ref != historyIndex && ref != -1){                       //checks if ref is not last drawn shape

                if(checkIfOverMovedShapes(operations.get(ref), ref)){         //checks if op is over moved shape
                    writeUndo(pw,operations.get(ref));
                    for(int i = 0; i < tmp.size()-1; i++){
                        writeSnapshot(pw,compareSnapshots(tmp.get(i).getChangeValues(),tmp.get(i+1).getChangeValues()));
                    }
                    tmp.clear();
                }else{
                    writeUndo(pw,operations.get(ref));                  //writes before snapshot of reference
                }

                operations.get(ref).setVisible(false);                  //sets ref invisible
                for(int i = ref+1; i < operations.size();i++){
                    if(operations.get(i).isVisible()){
                        operations.get(i).drawAfterMove(gc);
                    }
                }
            }else{
                writeUndo(pw,operations.get(historyIndex));
                operations.get(historyIndex).setVisible(false);
            }
            historyIndex++;
            operations.add(op);
            op.draw(gc);
            DB_LOGGER.debug("add move operation");
        }

    }


    public void undo() {
        if (historyIndex >= 0) {
            operations.get(historyIndex).setVisible(false);
            historyIndex--;
            operations.get(historyIndex).setVisible(true);
            redraw();
            DB_LOGGER.debug("undo");
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
            DB_LOGGER.debug("redo");
        }
    }
    private boolean checkIfOverMovedShapes(DrawOp op, int ref){
        try {
            Shapes shape = (Shapes) op;
            tmp.add(shape);
            Rectangle2D enclosedRect = new Rectangle2D(shape.getMinX(), shape.getMinY(),
                    shape.getMinX() + shape.getWidth(), shape.getMinY() + shape.getHeight());
            for(int i = ref - 1; i >= 0; i--){
                if(operations.get(i) instanceof Clear){
                    break;
                }
                if(!operations.get(i).isVisible()){
                    Shapes bottomShape = (Shapes) operations.get(i);

                    if(enclosedRect.intersects(new Rectangle2D(bottomShape.getMinX(), bottomShape.getMinY(),
                            bottomShape.getMinX() + bottomShape.getWidth(), bottomShape.getMinY() + bottomShape.getHeight()))){
                        temp = bottomShape;
                        tmp.add(bottomShape);
                    }
                }
            }
        }catch (ClassCastException e) {
            DB_LOGGER.error("clear object passed to checkIfOverMovedShape(): " + e.getMessage());
        }
        return tmp.size() > 1;
    }
    private void writeUndo(PixelWriter pw, DrawOp op){
        HashMap<Integer, Integer> values = op.getChangeValues();
        writeSnapshot(pw, values);
        DB_LOGGER.debug("write undo");
    }
    private void writeSnapshot(PixelWriter pw, HashMap<Integer, Integer> values){
        values.forEach((k,v) -> pw.setArgb((int)(k % gc.getCanvas().getWidth()), (int)(k / gc.getCanvas().getWidth()), v));
        DB_LOGGER.debug("write snapshot on canvas");
    }
    public void clearBoard(){
        addDrawOperation(new Clear());
        DB_LOGGER.debug("clear board");
    }
    public ArrayList<DrawOp> getOperations(){
        return this.operations;
    }
}
