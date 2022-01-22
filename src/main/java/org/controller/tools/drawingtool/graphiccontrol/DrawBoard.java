package org.controller.tools.drawingtool.graphiccontrol;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controller.tools.drawingtool.graphiccontrol.objects.Clear;
import org.controller.tools.drawingtool.graphiccontrol.objects.Shapes;

import java.util.*;

public class DrawBoard {
    private static final Logger DB_LOGGER = LogManager.getLogger(DrawBoard.class.getName());

    private final ArrayList<DrawOp> operations = new ArrayList<>();
    private final GraphicsContext gc;
    private int historyIndex = -1;
    private final PixelWriter pw;
    private ArrayList<Shapes> tmp = new ArrayList<>();
    private ShapePixelMap tmp2;
    public DrawBoard(GraphicsContext gc) {
        this.gc = gc;
        pw = gc.getPixelWriter();
        DB_LOGGER.info("successfully created draw board");
    }

    public List<DrawOp> getOperations(){
        return Collections.unmodifiableList(operations);

    }

    public void addDrawOperation(DrawOp op) {
        operations.subList(historyIndex+1, operations.size()).clear();  // clear history after current position
        if(op.getOpType() == DrawOp.OpType.DRAW){
            draw(op);
        }else if(op.getOpType() == DrawOp.OpType.MOVE) {
            move(op);
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
        //TODO updated undo
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
        //TODO updated redo
    }
    public void clearBoard(){
        addDrawOperation(new Clear());
        DB_LOGGER.debug("clear board");
    }

    private void draw(DrawOp op){
        operations.add(op);                                         // add new drawing operation
        historyIndex++;
        op.draw(gc);
        DB_LOGGER.debug("add draw operation");
    }
    private void move(DrawOp op){
            int ref = op.getMoveReference();                            //gets reference for moving operation

            if(ref != historyIndex && ref != -1){                       //checks if ref is not last drawn shape

                if(checkIfOverMovedShapes(operations.get(ref), ref)){         //checks if op is over moved shape
                    writeSnapshot(pw, tmp2);
                    tmp2 = null;
                    DB_LOGGER.debug("written cutout | over moved");
                }else{
                    writeUndo(pw,operations.get(ref));                  //writes before snapshot of reference
                    DB_LOGGER.debug("written cutout | ");
                }

                operations.get(ref).setVisible(false);                  //sets ref invisible
                getShapesOver(ref).forEach(s-> s.drawAfterMove(gc));
            }else{
                writeUndo(pw,operations.get(historyIndex));
                operations.get(historyIndex).setVisible(false);
            }
            historyIndex++;
            operations.add(op);
            op.draw(gc);
            DB_LOGGER.debug("add move operation");
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

    private boolean checkIfOverMovedShapes(DrawOp op, int ref){
        int counter = 0;
        try {
            Shapes shape = (Shapes) op;
            tmp2 = new ShapePixelMap(op.getPixelsBelow());
            Rectangle2D enclosedRect = new Rectangle2D(shape.getMinX(), shape.getMinY(),
                    shape.getMinX() + shape.getWidth(), shape.getMinY() + shape.getHeight());
            for(int i = ref - 1; i >= 0; i--){
                if(operations.get(i) instanceof Clear){
                    break;
                }
                Shapes under = (Shapes) operations.get(i);
                if(enclosedRect.intersects(new Rectangle2D(under.getMinX(), under.getMinY(),
                        under.getMinX() + under.getWidth(), under.getMinY() + under.getHeight()))){
                    counter++;
                    updateSnapshotMoved(under);
                }

            }
        }catch (ClassCastException e) {
            DB_LOGGER.error("clear draw operation passed to checkIfOverMovedShape(): " + e.getMessage());
        }
        return counter > 0;
    }
    private ArrayList<Shapes> getShapesOver(int ref){
        ArrayList<Shapes> over = new ArrayList<>();
        try{
            Shapes shape = (Shapes) operations.get(ref);
            Rectangle2D enclosedRect = new Rectangle2D(shape.getMinX(), shape.getMinY(),
                    shape.getMinX() + shape.getWidth(), shape.getMinY() + shape.getHeight());
            for(int i = ref+1; i < operations.size(); i++){
                Shapes under = (Shapes) operations.get(i);
                if(enclosedRect.intersects(new Rectangle2D(under.getMinX(), under.getMinY(),
                        under.getMinX() + under.getWidth(), under.getMinY() + under.getHeight()))){
                    over.add((Shapes) operations.get(i));
                }
            }
        }catch (ClassCastException e) {
            DB_LOGGER.error("clear passed to method: " + e.getMessage());
        }
        return over;
    }
    private void writeUndo(PixelWriter pw, DrawOp op){
        HashMap<Integer, Integer> values = op.getPixelsBelow();
        writeSnapshot(pw, values);
        DB_LOGGER.debug("write undo");
    }
    private void writeSnapshot(PixelWriter pw, HashMap<Integer,Integer> values){
        values.forEach((k,v) -> pw.setArgb((int)(k % gc.getCanvas().getWidth()), (int)(k / gc.getCanvas().getWidth()), v));
        DB_LOGGER.debug("write snapshot on canvas");
    }
    private void updateSnapshotMoved(Shapes under){
        if(under.isVisible()){
            tmp2.addLockPixels(tmp2.getIntersect(under.getPixelsBelow()));
        }
        tmp2.replace(under.getPixelsBelow());
    }


}
