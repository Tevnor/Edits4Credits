package org.editor.tools.filtertool.filtercontrol.filter;

import org.editor.tools.filtertool.filtercontrol.Filter;

public class GlitchFilter implements Filter {

    @Override
    public int applyFilter(int argb, double factor) {
        return (int) (argb / (1 + factor));
    }
}