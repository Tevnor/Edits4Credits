package org.editor.tools.drawingtool.graphiccontrol;

import javafx.geometry.Point2D;
import junit.framework.TestCase;
import org.editor.tools.drawingtool.DrawingControl;
import org.junit.Assert;

import java.util.Arrays;

public class DrawingControlTest extends TestCase {
    private final DrawingControl dc = new DrawingControl();

    final double[] dims = new double[]{200,200,300,100};
    public void testCalculateMinXMinYWidthHeight() {
        Assert.assertArrayEquals(dims, dc.calculateMinXMinYWidthHeight(new Point2D(500,200), new Point2D(200,300)),0);
        assertFalse(Arrays.equals(dims,dc.calculateMinXMinYWidthHeight(new Point2D(100,345), new Point2D(700,125))));
    }

    final double[] dims2 = new double[]{450,300,150};
    public void testCalculateMinXMinYRadius() {
        Assert.assertArrayEquals(dims2, dc.calculateMinXMinYRadius(new Point2D(450,300), new Point2D(750,900)),0);
        assertFalse(Arrays.equals(dims2,dc.calculateMinXMinYRadius(new Point2D(500,330), new Point2D(912,900))));
    }
}