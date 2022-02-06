package org.editor.tools.filterTool.filterControl.filter;

import org.editor.tools.filterTool.filterControl.Filter;

public class GlitchFilter implements Filter {

    @Override
    public int applyFilter(int argb, double factor) {
        return (int) (argb / (1 + factor));
    }
}