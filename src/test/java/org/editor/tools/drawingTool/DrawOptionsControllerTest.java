package org.editor.tools.drawingTool;

import junit.framework.TestCase;
import org.editor.tools.drawingTool.handlers.HandlerFactory;
import org.junit.Test;

public class DrawOptionsControllerTest extends TestCase {
    private DrawOptionsController dc;

    @Test
    public void testGetTmpHandler() {
        dc = new DrawOptionsController();
        dc.setSelShape(HandlerFactory.DrawHandlerType.ELLIPSES);
    }

    public void testGetAttributes() {
    }
}