package org.editor.tools.drawingTool.attributes;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.drawingTool.FontMetrics;

public class TextAttributes extends AbstractGeneral {
    private static final Logger TA_LOGGER = LogManager.getLogger(RoundRectAttributes.class.getName());
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
        TA_LOGGER.debug("TextAttributes object created");
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
