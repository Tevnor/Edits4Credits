package org.editor.tools.drawingtool.graphiccontrol;

import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FontMetrics {
    final private Text internal;
    private final double ascent, descent, lineHeight;

    public FontMetrics(Font fnt) {
        internal = new Text();
        internal.setFont(fnt);

        Bounds b= internal.getLayoutBounds();
        lineHeight= b.getHeight();
        ascent= -b.getMinY();
        descent= b.getMaxY();
    }

    public double computeStringWidth(String txt) {
        internal.setText(txt);
        return internal.getLayoutBounds().getWidth();
    }

    public double getLineHeight(){
        return lineHeight;
    }

    public double getAscent() {
        return ascent;
    }

    public double getDescent() {
        return descent;
    }
}
