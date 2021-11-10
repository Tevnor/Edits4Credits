package basic.editor.tools.drawtool.graphiccontrol;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DrawBoard {
    private final List<DrawOperation> operations = new ArrayList<>();
    private final GraphicsContext gc;
    private int historyIndex = -1;

    public DrawBoard(GraphicsContext gc) {
        this.gc = gc;
    }

    public void redraw() {
        Canvas c = gc.getCanvas();
        //gc.clearRect(1, 1, c.getWidth(), c.getHeight());
        clearBoard();
        for (int i = 0; i <= historyIndex; i++) {
            operations.get(i).draw(gc);
        }
    }

    public void addDrawOperation(DrawOperation op) {
        // clear history after current position
        operations.subList(historyIndex+1, operations.size()).clear();

        // add new operation
        operations.add(op);
        historyIndex++;
        op.draw(gc);
    }

    public void undo() {
        if (historyIndex >= 0) {
            historyIndex--;
            redraw();
        }
    }

    public void redo() {
        if (historyIndex < operations.size()-1) {
            historyIndex++;
            operations.get(historyIndex).draw(gc);
        }
    }

    public void clearBoard(){
        PixelWriter pw = gc.getPixelWriter();
        Canvas c = gc.getCanvas();

        for(int x = 0; x <= c.getWidth(); x++){
            for(int y = 0; y <= c.getHeight(); y++){

                pw.setColor(x, y, Color.TRANSPARENT);
            }
        }


    }
}
