package org.editor.tools.drawingtool;

import junit.framework.TestCase;
import org.editor.tools.drawingtool.handlers.HandlerFactory;
import org.junit.Test;

public class DrawOptionsControllerTest extends TestCase {
    private DrawOptionsController dc;

    @Test
    public void testGetTmpHandler() {
        dc = new DrawOptionsController();
        dc.setSelShape(HandlerFactory.DrawHandler.ELLIPSES);
    }

    public void testGetAttributes() {
    }
}