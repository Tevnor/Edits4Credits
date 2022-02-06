package org.editor.tools.drawingtool.objects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import org.editor.tools.drawingtool.attributes.RoundRectAttributes;

import static org.editor.tools.drawingtool.objects.Shapes.ShapeType.ROUNDED_RECT;

public class RoundedRectangle extends Rectangle{

    private final RoundRectAttributes attributes;

    public RoundedRectangle(double minX, double minY, double width, double height, RoundRectAttributes attributes) {
        super(minX, minY, width, height);
        this.attributes = attributes;
        setRotation(attributes.getRotation());
        this.shapeType = ROUNDED_RECT;
    }

    @Override
    void drawStroke(GraphicsContext gc) {
        gc.setStroke(attributes.getColor());
        setAttributesOfGc(gc);
        gc.strokeRoundRect(getMinX(),getMinY(),getWidth(),getHeight(),attributes.getArcWidth(),attributes.getArcHeight());
    }
    @Override
    void drawFill(GraphicsContext gc){
        gc.setFill(attributes.getColor());
        setAttributesOfGc(gc);
        gc.fillRoundRect(getMinX(),getMinY(),getWidth(),getHeight(),attributes.getArcWidth(),attributes.getArcHeight());
    }
    @Override
    public boolean pointInside(Point2D point){
        double minX = getMinX(), minY = getMinY(), width = getWidth(), height = getHeight();
        Point2D postR = r.inverseTransform(point.getX(),point.getY());
        Rectangle2D rect2D, corOne, corTwo, corThree, corFour;
        rect2D = new Rectangle2D(minX,minY,width,height);

        corOne = new Rectangle2D(minX,minY,minX+attributes.getArcWidth(),minY+attributes.getArcHeight());
        corTwo = new Rectangle2D(minX,minY+height-attributes.getArcHeight(), minX+attributes.getArcWidth(),minY+height);
        corThree = new Rectangle2D(minX+width-attributes.getArcWidth(), minY, minX+width,minY+attributes.getArcHeight());
        corFour = new Rectangle2D(minX+width-attributes.getArcWidth(), minY+height-attributes.getArcHeight(),minX+width, minY+height);

        double a = postR.getX();
        double b = postR.getY();
        double radX = attributes.getArcWidth();
        double radY = attributes.getArcHeight();

        if(rect2D.contains(postR)){

            boolean inside = true;

            if(corOne.contains(postR)){
                a -= minX+radX;
                b -= minY+radY;
                if(!(1 >= (a*a)/(radX*radX) + (b*b)/(radY*radY))){
                    inside = false;
                }
            }

            if(corTwo.contains(postR)){
                a -= minX+radX;
                b -= minY+height-radY;
                if(!(1 >= (a*a)/(radX*radX) + (b*b)/(radY*radY))){
                    inside = false;
                }
            }

            if(corThree.contains(postR)){
                a -= minX+height-radX;
                b -= minY+radY;
                if(!(1 >= (a*a)/(radX*radX) + (b*b)/(radY*radY))){
                    inside = false;
                }
            }
            if(corFour.contains(postR)){
                a -= minX+height-radX;
                b -= minY+height-radY;
                if(!(1 >= (a*a)/(radX*radX) + (b*b)/(radY*radY))){
                    inside = false;
                }
            }


            return inside;


        }

        return false;

    }
    @Override
    public Shape getShapeRepresentation() {
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(getMinX(),getMinY(),getWidth(),getHeight());
        rect.setArcHeight(attributes.getArcHeight());
        rect.setArcWidth(attributes.getArcWidth());
        rect.setRotate(this.r.getAngle());
        return rect;
    }
    @Override
    public Shapes reposition(Point2D point) {
        RoundedRectangle r = new RoundedRectangle(point.getX(),point.getY(),getWidth(),getWidth(),
                new RoundRectAttributes(attributes,attributes.getArcWidth(),attributes.getArcHeight()));
        r.setOpType(OpType.MOVE);
        return r;
    }
    @Override
    public RoundRectAttributes getAttributes(){
        return new RoundRectAttributes(attributes,attributes.getArcWidth(),attributes.getArcHeight());
    }

}
