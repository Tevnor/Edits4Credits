package org.editor.tools.filtertool.filtercontrol.filter;

import org.editor.tools.filtertool.filtercontrol.Filter;

public class Original implements Filter {
    @Override
    public int applyFilter(int argb, double factor) {
        return argb;
    }
}
