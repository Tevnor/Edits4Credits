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
    private final General general = new General(0, BlendMode.SRC_OVER,1, Color.BLACK,1,true);
    private final double[] dims = new double[]{0,0,100,100};
    private final double[] x = new double[]{0,50,100};
    private final double[] y = new double[]{100,0,100};
    private final Arc arc = new Arc(0,0,100,100,new ArcAttributes(general, ArcType.ROUND,0,180));
    private final Circle circle = new Circle(0,0,50,general);
    private final Ellipses ellipses = new Ellipses(0,0,100,50,general);
    private final Line line = new Line(0,0,100,100,general);
    private final Polygon poly = new Polygon(dims,x,y,3,new PolygonAttributes(general,true));
    private final RoundedRectangle roundedRectangle = new RoundedRectangle(0,0,100,100,new RoundRectAttributes(general,10,20));
    private final Text txt = new Text(50,50,new TextAttributes(general,"HELLLOOO",new Font("System",50), TextAlignment.CENTER));
    private final Point2D mid = new Point2D(50,50);
    private final Point2D out = new Point2D(200,100);
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