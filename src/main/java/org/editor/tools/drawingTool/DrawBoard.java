package org.editor.tools.drawingTool;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.shape.Shape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.drawingTool.objects.Clear;
import org.editor.tools.drawingTool.objects.Shapes;

import java.util.*;

/**
 * <p>This class is for organizing and manipulating of the draw methods called by the different graphic objects.</p>
 * <p>As the name suggests it implements methods such as "clear", "undo", "redo",...  and so on.</p>
 * <p>It is one of the three main control classes which the DrawingTool uses to draw all sorts of objects etc. on the canvas.</p>
 *
 * @author Yannick Kebbe
 * @see java.lang.Object
 */
public class DrawBoard {
    private static final Logger DB_LOGGER = LogManager.getLogger(DrawBoard.class.getName());

    /**
     * {@link ArrayList} of generic {@link DrawOp} representing the draw operation history of the {@link DrawBoard}
     */
    private final ArrayList<DrawOp> operations = new ArrayList<>();
    /**
     * {@link GraphicsContext} of underlying canvas
     */
    private final GraphicsContext gc;
    /**
     * {@link PixelWriter} of {@link GraphicsContext}
     */
    private final PixelWriter pw;
    /**
     * history index of the operation list
     */
    private int historyIndex = -1;
    /**
     * temporary Canvas used to get determine changed pixels
     */
    private final Canvas cTmp;
    private final GraphicsContext gcTmp;


    /**
     * creates instance of {@link DrawBoard} with given {@link GraphicsContext}
     *
     * @param gc the graphicscontext of underlying canvas
     */
    public DrawBoard(GraphicsContext gc) {
        this.gc = gc;
        cTmp = new Canvas(gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gcTmp = cTmp.getGraphicsContext2D();
        pw = gc.getPixelWriter();
        DB_LOGGER.info("successfully created draw board");
    }

    /**
     * delivers unmodifiable arraylist "operations" for this drawboard
     *
     * @return operations from drawboard
     */
    public List<DrawOp> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    /**
     * Adds a {@link DrawOp} to the operations history and draws it on the canvas.
     * Resets undo/redo sublist if #undoCalls != #redoCalls.
     *
     * @param op DrawOperation which should be added to the DrawBoard
     */
    public void addDrawOperation(DrawOp op) {
        DB_LOGGER.debug("addDrawOperation() started");
        operations.subList(historyIndex + 1, operations.size()).clear();  // clear undo/redo history after current position
        if (op.getOpType() == DrawOp.OpType.DRAW) {
            DB_LOGGER.debug("add()/draw entered");
            draw(op);
        } else if (op.getOpType() == DrawOp.OpType.MOVE) {
            DB_LOGGER.debug("add()/move entered");
            move(op);
        }
    }

    /**
     * Undos last add DrawOperation.
     */
    public void undo() {
        DB_LOGGER.debug("undo() started");
        if (historyIndex >= 0) {
            operations.get(historyIndex).setVisible(false);
            DB_LOGGER.debug("update() entered");
            update();
            historyIndex--;
        }
    }

    /**
     * Reverses last undo call if it follows directly after the undo call.
     */
    public void redo() {
        DB_LOGGER.debug("redo() started");
        if (historyIndex < operations.size() - 1) {
            historyIndex++;
            DrawOp op = operations.get(historyIndex);
            if (op.getOpType() == DrawOp.OpType.DRAW) {
                DB_LOGGER.debug("redo/DRAW entered");
                op.setVisible(true);
                op.draw(gc, gcTmp);
            } else if (op.getOpType() == DrawOp.OpType.MOVE) {
                DB_LOGGER.debug("redo/MOVE entered");
                op.setVisible(true);
                writeSnapshot(operations.get(op.getMoveReference()).getPixelsBelow());
                operations.get(op.getMoveReference()).setVisible(false);
                op.draw(gc, gcTmp);
            }

        }
    }

    /**
     * Adds Clear DrawOp to the history and clears the canvas.
     */
    public void clearBoard() {
        DB_LOGGER.debug("clearBoard() entered");
        addDrawOperation(new Clear());
    }

    /**
     * Draws DrawOp.
     *
     * @param op DrawOp with Type "DRAW"
     */
    private void draw(DrawOp op) {
        DB_LOGGER.debug("draw() entered");
        addToOperations(op);
        op.setMoveReference(historyIndex);
    }

    /**
     * Draws moved DrawOp and erases moving reference of the DrawOp
     *
     * @param op DrawOp with Type "MOVE"
     */
    private void move(DrawOp op) {
        int ref = op.getMoveReference();
        if (ref != historyIndex) {                                            //checks if ref is not last drawn shape
            writeSnapshot(getPixelsOverMovedShapes(ref));                   //writes to canvas adjusted snapshot of reference
            operations.get(ref).setVisible(false);
            getShapesOver(ref).forEach(s -> s.drawAfterMove(gc));            //draws each overlaying shape again
            DB_LOGGER.debug("write move snap(ref)");
        } else {
            writeSnapshot(operations.get(historyIndex).getPixelsBelow());   //writes snapshot of historyIndex
            operations.get(historyIndex).setVisible(false);
            DB_LOGGER.debug("write move snap(history)");
        }
        addToOperations(op);                                                //adds and draws draw(move) op
    }

    /**
     * adds draw operation to internal operations lists and draws them
     *
     * @param op {@link DrawOp} to be added
     */
    private void addToOperations(DrawOp op) {
        historyIndex++;
        operations.add(op);
        op.draw(gc, gcTmp);
    }

    /**
     * Updates canvas after undo is triggered.
     */
    private void update() {
        DrawOp op = operations.get(historyIndex);
        if (op.getOpType() == DrawOp.OpType.DRAW) {
            writeSnapshot(op.getPixelsBelow());
        } else if (op.getOpType() == DrawOp.OpType.MOVE) {
            writeSnapshot(op.getPixelsBelow());
            operations.get(op.getMoveReference()).draw(gc, gcTmp);
            operations.get(op.getMoveReference()).setVisible(true);
            getShapesOver(op.getMoveReference()).forEach(t -> t.drawAfterMove(gc));
        }
    }

    /**
     * Checks if Shape with index "ref" in operations lays over moved/invisible
     * shapes and writes the updated pixels of the ShapePixelMap with the underlying pixels
     *
     * @param ref index of the Shape in the operations history
     * @return {@link ShapePixelMap} of the correct underlying pixels
     */
    private ShapePixelMap getPixelsOverMovedShapes(int ref) {
        DrawOp op = operations.get(ref);
        ShapePixelMap tmp = op.getPixelsBelow();
        try {
            Shapes shape = (Shapes) op;
            Shape base = shape.getShapeRepresentation();
            for (int i = ref - 1; i >= 0; i--) {
                if (operations.get(i) instanceof Clear) {
                    break;
                }
                Shapes under = (Shapes) operations.get(i);
                Shape below = under.getShapeRepresentation();

                if (!(Shape.intersect(base, below)).getBoundsInLocal().isEmpty()) {
                    updateSnapshotMoved(under, tmp);
                    DB_LOGGER.debug("updated tmp");
                }
            }
        } catch (ClassCastException e) {
            DB_LOGGER.error("clear draw operation passed to checkIfOverMovedShape(): " + e.getMessage());
        }
        return tmp;
    }

    /**
     * Checks for Shapes which lay above
     *
     * @param ref index of the Shape in the operations history
     * @return ArrayList of shapes which lay over the ref in ascending order
     */
    private List<Shapes> getShapesOver(int ref) {
        ArrayList<Shapes> over = new ArrayList<>();
        try {
            Shapes base = (Shapes) operations.get(ref);
            Shape base2 = base.getShapeRepresentation();
            for (int i = ref + 1; i < operations.size(); i++) {
                if (operations.get(i).isVisible()) {
                    Shapes step = (Shapes) operations.get(i);
                    Shape step2 = step.getShapeRepresentation();
                    if (!(Shape.intersect(base2, step2)).getBoundsInLocal().isEmpty()) {
                        over.add((Shapes) operations.get(i));
                    }
                }
            }
        } catch (ClassCastException e) {
            DB_LOGGER.error("clear passed to method: " + e.getMessage());
        }
        return over;
    }

    /**
     * Writes pixels provided by the {@link ShapePixelMap} into the canvas.
     *
     * @param values {@link ShapePixelMap} containing (location,value) of the pixels
     */
    private void writeSnapshot(ShapePixelMap values) {
        values.forEach((k, v) -> pw.setArgb((int) (k % gc.getCanvas().getWidth()), (int) (k / gc.getCanvas().getWidth()), v));
        DB_LOGGER.debug("write snapshot on canvas");
    }

    /**
     * Locks or Overrides Pixels in {@link ShapePixelMap} base, based on if
     * {@link Shapes} under is visible or not
     *
     * @param under {@link Shapes}
     * @param base  {@link ShapePixelMap}
     */
    private void updateSnapshotMoved(Shapes under, ShapePixelMap base) {
        if (under.isVisible()) {
            base.addLockPixels(base.getIntersect(under.getPixelsBelow()));
        }
        base.replace(under.getPixelsBelow());
    }
}
