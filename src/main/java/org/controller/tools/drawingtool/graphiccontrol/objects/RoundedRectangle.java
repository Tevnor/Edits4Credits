package org.controller.tools.drawingtool.graphiccontrol.objects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public class RoundedRectangle extends Rectangle{

    private double arcWidth;
    private double arcHeigth;

    public RoundedRectangle(double minX, double minY, double width, double height, double arcWidth, double arcHeight, Paint color) {
        super(minX, minY, width, height, color);
        this.arcWidth = arcWidth;
        this.arcHeigth = arcHeight;
        this.type = ROUNDED_RECT;
    }

    @Override
    void drawStroke(GraphicsContext gc) {
        gc.setStroke(getColor());
        setAttributes(gc);
        gc.strokeRoundRect(getMinX(),getMinY(),getWidth(),getHeight(),this.arcWidth,this.arcHeigth);
    }

    @Override
    void drawFill(GraphicsContext gc){
        gc.setFill(getColor());
        setAttributes(gc);
        gc.fillRoundRect(getMinX(),getMinY(),getWidth(),getHeight(),this.arcWidth,this.arcHeigth);
    }

    public boolean pointInside(Point2D point){
        Point2D postR = r.inverseTransform(point.getX(),point.getY());
        Rectangle2D rect2D, corOne, corTwo, corThree, corFour;
        rect2D = new Rectangle2D(minX,minY,width,height);

        corOne = new Rectangle2D(minX,minY,minX+arcWidth,minY+arcHeigth);
        corTwo = new Rectangle2D(minX,minY+height-arcHeigth, minX+arcWidth,minY+height);
        corThree = new Rectangle2D(minX+width-arcWidth, minY, minX+width,minY+arcHeigth);
        corFour = new Rectangle2D(minX+width-arcWidth, minY+height-arcHeigth,minX+width, minY+height);

        double a = postR.getX();
        double b = postR.getY();
        double radX = arcWidth;
        double radY = arcHeigth;

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
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(minX,minY,width,height);
        rect.setArcHeight(this.arcHeigth);
        rect.setArcWidth(this.arcWidth);
        rect.setRotate(this.r.getAngle());
        return rect;
    }

    @Override
    public Shapes reposition(Point2D point) {
        RoundedRectangle r = new RoundedRectangle(minX,minY,width,height,arcWidth,arcHeigth,color);
        r.minX = point.getX();
        r.minY = point.getY();
        r.setOpType(OpType.MOVE);
        return r;
    }


}
