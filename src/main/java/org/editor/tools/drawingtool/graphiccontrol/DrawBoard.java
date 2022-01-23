package org.editor.tools.drawingtool.graphiccontrol;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.shape.Shape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.drawingtool.graphiccontrol.objects.Clear;
import org.editor.tools.drawingtool.graphiccontrol.objects.Shapes;

import java.util.*;

/**
 *  This class is for organizing and manipulating of the draw methods called by the different graphic objects.
 *  As the name suggests it implements methods such as "clear", "undo", "redo",...  and so on.
 *  It is one of the three main control classes which the DrawingTool uses to draw all sorts of objects etc. on the canvas.
 *
 *  @author Yannick Kebbe
 */
public class DrawBoard {
    private static final Logger DB_LOGGER = LogManager.getLogger(DrawBoard.class.getName());

    private final ArrayList<DrawOp> operations = new ArrayList<>();
    private final GraphicsContext gc;
    private int historyIndex = -1;
    private final PixelWriter pw;
    public DrawBoard(GraphicsContext gc) {
        this.gc = gc;
        pw = gc.getPixelWriter();
        DB_LOGGER.info("successfully created draw board");
    }

    public List<DrawOp> getOperations(){
        return Collections.unmodifiableList(operations);
    }

    /**
     * Adds a {@link DrawOp} to the DrawBoard history and draws it on the canvas.
     * Resets undo/redo sublist if #undoCalls != #redoCalls.
     * @param op DrawOperation which should be added to the DrawBoard
     */
    public void addDrawOperation(DrawOp op) {
        operations.subList(historyIndex+1, operations.size()).clear();  // clear history after current position
        if(op.getOpType() == DrawOp.OpType.DRAW){
            draw(op);
        }else if(op.getOpType() == DrawOp.OpType.MOVE) {
            move(op);
        }
    }

    /**
     * Undos last add DrawOperation.
     */
    public void undo() {
        if (historyIndex >= 0) {
            operations.get(historyIndex).setVisible(false);
            update();
            historyIndex--;
            DB_LOGGER.debug("undo");
        }
    }

    /**
     * Reverses last undo call if it follows directly after the undo call.
     */
    public void redo() {
        if (historyIndex < operations.size()-1) {
            historyIndex++;
            DrawOp op = operations.get(historyIndex);
            if(op.getOpType() == DrawOp.OpType.DRAW){
                op.setVisible(true);
                op.draw(gc);
            }else if(op.getOpType() == DrawOp.OpType.MOVE){
                op.setVisible(true);
                writeSnapshot(operations.get(op.getMoveReference()).getPixelsBelow());
                operations.get(op.getMoveReference()).setVisible(false);
                op.draw(gc);
            }
            DB_LOGGER.debug("redo");
        }
    }

    /**
     * Adds Clear DrawOp to the history and clears the canvas.
     */
    public void clearBoard(){
        addDrawOperation(new Clear());
        DB_LOGGER.debug("clear board");
    }

    /**
     * Draws DrawOp.
     * @param op DrawOp with Type "DRAW"
     */
    private void draw(DrawOp op){
        operations.add(op);                                         // add new drawing operation
        historyIndex++;
        op.draw(gc);
        op.setReference(historyIndex);
        DB_LOGGER.debug("add draw operation");
    }

    /**
     * Draws moved DrawOp and erases moving reference of the DrawOp
     * @param op DrawOp with Type "MOVE"
     */
    private void move(DrawOp op){
            int ref = op.getMoveReference();
            if(ref != historyIndex){                                    //checks if ref is not last drawn shape
                writeSnapshot(getPixelsOverMovedShapes(ref));
                operations.get(ref).setVisible(false);                  //sets ref invisible
                getShapesOver(ref).forEach(s-> s.drawAfterMove(gc));    //draws each overlaying shape again
                DB_LOGGER.debug("write move snap(ref)");
            }else{
                writeSnapshot(operations.get(historyIndex).getPixelsBelow());
                operations.get(historyIndex).setVisible(false);
                DB_LOGGER.debug("write move snap(history)");
            }
            historyIndex++;
            operations.add(op);
            op.draw(gc);
            DB_LOGGER.debug("add move operation");
    }

    /**
     * Updates canvas after undo is triggered.
     */
    private void update() {
        DrawOp op = operations.get(historyIndex);
        if(op.getOpType() == DrawOp.OpType.DRAW){
            writeSnapshot(op.getPixelsBelow());
        }else if(op.getOpType() == DrawOp.OpType.MOVE){
            writeSnapshot(op.getPixelsBelow());
            operations.get(op.getMoveReference()).draw(gc);
            operations.get(op.getMoveReference()).setVisible(true);
            getShapesOver(op.getMoveReference()).forEach(t -> t.drawAfterMove(gc));
        }
    }

    /**
     * Checks if Shape with index "ref" in operations lays over moved/invisible
     * shapes and writes the updated pixels of the ShapePixelMap with the underlying pixels
     * @param ref index of the Shape in the operations history
     * @return {@link ShapePixelMap} of the correct underlying pixels
     */
    private ShapePixelMap getPixelsOverMovedShapes(int ref){
        DrawOp op = operations.get(ref);
        ShapePixelMap tmp = op.getPixelsBelow();
        try {
            Shapes shape = (Shapes) op;
            Shape base = shape.getShapeRepresentation();
            for(int i = ref - 1; i >= 0; i--){
                if(operations.get(i) instanceof Clear){
                    break;
                }
                Shapes under = (Shapes) operations.get(i);
                Shape below = under.getShapeRepresentation();

                if(!(Shape.intersect(base,below)).getBoundsInLocal().isEmpty()){
                    updateSnapshotMoved(under, tmp);
                    DB_LOGGER.debug("updated tmp");
                }
            }
        }catch (ClassCastException e) {
            DB_LOGGER.error("clear draw operation passed to checkIfOverMovedShape(): " + e.getMessage());
        }
        return tmp;
    }

    /**
     * Checks for Shapes which lay above
     * @param ref index of the Shape in the operations history
     * @return ArrayList of shapes which lay over the ref in ascending order
     */
    private ArrayList<Shapes> getShapesOver(int ref){
        ArrayList<Shapes> over = new ArrayList<>();
        try{
            Shapes base = (Shapes) operations.get(ref);
            Shape base2 = base.getShapeRepresentation();
            for(int i = ref+1; i < operations.size(); i++){
                if(operations.get(i).isVisible() ){
                    Shapes step = (Shapes) operations.get(i);
                    Shape step2 = step.getShapeRepresentation();
                    if(!(Shape.intersect(base2,step2)).getBoundsInLocal().isEmpty()){
                        over.add((Shapes) operations.get(i));
                    }
                }
            }
        }catch (ClassCastException e) {
            DB_LOGGER.error("clear passed to method: " + e.getMessage());
        }
        return over;
    }


    /**
     * Writes pixels provided by the {@link ShapePixelMap} into the canvas.
     * @param values {@link ShapePixelMap} containing (location,value) of the pixels
     */
    private void writeSnapshot(ShapePixelMap values){
        values.forEach((k,v) -> pw.setArgb((int)(k % gc.getCanvas().getWidth()), (int)(k / gc.getCanvas().getWidth()), v));
        DB_LOGGER.debug("write snapshot on canvas");
    }

    /**
     * @param under
     * @param tmp
     */
    private void updateSnapshotMoved(Shapes under, ShapePixelMap tmp){
        if(under.isVisible()){
            tmp.addLockPixels(tmp.getIntersect(under.getPixelsBelow()));
        }
        tmp.replace(under.getPixelsBelow());
    }


}
