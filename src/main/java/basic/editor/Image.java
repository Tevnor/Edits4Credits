package basic.editor;

import basic.editor.tools.EditingTools;
import basic.editor.tools.drawtool.DrawingTool;
import javafx.scene.canvas.Canvas;
import java.util.ArrayList;

public class Image {

    private long xDim;
    private long yDim;
    private javafx.scene.image.Image background;
    private ArrayList<Canvas> layers;

    public Image (long xDim,long yDim){

        this.xDim = xDim;
        this.yDim = yDim;
        //TODO constructor either completly new Image (blank) or load Image from local hard drive
    }

    public void setBackground(javafx.scene.image.Image background){
        this.background = background;
    }


    public void addLayer(){
        layers.add(new Canvas(xDim,yDim));
    }


    private void editDraw(int layer){
        DrawingTool dt = new DrawingTool(this.layers.get(layer));

    }
    private void editVisual(){

    }
    private void editTransform(){

    }


    public void delete(){
        //TODO delete Image
    }

    public void copy(){
        //TODO copy Image in new Artwork maybe?
    }

}
