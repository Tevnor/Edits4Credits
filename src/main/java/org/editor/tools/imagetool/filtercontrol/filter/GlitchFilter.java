package org.editor.tools.imagetool.filtercontrol.filter;

import org.editor.tools.imagetool.filtercontrol.Filter;

public class GlitchFilter implements Filter {

    @Override
    public int applyFilter(int argb, double factor) {
        return (int) ((argb / 1.0333) * factor);
    }
}