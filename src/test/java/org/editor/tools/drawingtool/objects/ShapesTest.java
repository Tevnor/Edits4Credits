package org.editor.tools.drawingtool.objects;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import junit.framework.TestCase;
import org.editor.tools.drawingtool.attributes.*;

public class ShapesTest extends TestCase {
    private General general = new General(0, BlendMode.SRC_OVER,1, Color.BLACK,1,true);
    private double[] dims = new double[]{0,0,100,100}, x = new double[]{0,50,100}, y = new double[]{100,0,100};
    private Arc arc = new Arc(0,0,100,100,new ArcAttributes(general, ArcType.ROUND,0,180));
    private Circle circle = new Circle(0,0,50,general);
    private Ellipses ellipses = new Ellipses(0,0,100,50,general);
    private Line line = new Line(0,0,100,100,general);
    private Polygon poly = new Polygon(dims,x,y,3,new PolygonAttributes(general,true));
    private RoundedRectangle roundedRectangle = new RoundedRectangle(0,0,100,100,new RoundRectAttributes(general,10,20));
    private Text txt = new Text(50,50,new TextAttributes(general,"HELLLOOO",new Font("System",50), TextAlignment.CENTER));
    private Point2D mid = new Point2D(50,50);
    private Point2D out = new Point2D(200,100);
    public void testPointInsideArc() {
        assertTrue(arc.pointInside(mid));
        assertFalse(arc.pointInside(out));
    }
    public void testPointInsideCircle() {
        assertTrue(circle.pointInside(mid));
        assertFalse(circle.pointInside(out));
    }
    public void testPointInsideEllipses() {
        assertTrue(ellipses.pointInside(mid));
        assertFalse(ellipses.pointInside(out));
    }
    public void testPointInsideLine() {
        assertTrue(line.pointInside(mid));
        assertFalse(line.pointInside(out));
    }
    public void testPointInsidePolygon() {
        assertTrue(poly.pointInside(mid));
        assertFalse(poly.pointInside(out));
    }
    public void testPointInsideRoundRect() {
        assertTrue(roundedRectangle.pointInside(mid));
        assertFalse(roundedRectangle.pointInside(out));
    }
    public void testPointInsideText() {
        assertTrue(txt.pointInside(mid));
        assertFalse(txt.pointInside(out));
    }
}