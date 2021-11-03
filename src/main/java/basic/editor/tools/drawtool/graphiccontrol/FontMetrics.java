package basic.editor.tools.drawtool.graphiccontrol;

import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FontMetrics {
    final private Text internal;
    private float ascent, descent, lineHeight;

    public FontMetrics(Font fnt) {
        internal =new Text();
        internal.setFont(fnt);
        Bounds b= internal.getLayoutBounds();
        lineHeight= (float) b.getHeight();
        ascent= (float) -b.getMinY();
        descent=(float) b.getMaxY();
    }

    public float computeStringWidth(String txt) {
        internal.setText(txt);
        return (float) internal.getLayoutBounds().getWidth();
    }

    public float getLineHeight(){
        return lineHeight;
    }
}
