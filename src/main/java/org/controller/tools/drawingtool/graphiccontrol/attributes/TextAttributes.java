package org.controller.tools.drawingtool.graphiccontrol.attributes;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.controller.tools.drawingtool.graphiccontrol.FontMetrics;

public class TextAttributes extends AbstractGeneral {
    private final String content;
    private final Font font;
    private final TextAlignment txtAlignment;
    private final FontMetrics fm;
    public TextAttributes(AbstractGeneral base, String content, Font font, TextAlignment txtAlignment) {
        super(base);
        this.content = content;
        this.font = font;
        this.txtAlignment = txtAlignment;
        this.fm = new FontMetrics(font);
    }

    public String getContent() {
        return content;
    }
    public Font getFont() {
        return font;
    }
    public TextAlignment getTxtAlignment() {
        return txtAlignment;
    }
    public FontMetrics getFm() {
        return fm;
    }
}
