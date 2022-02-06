package org.editor.tools.filterTool.filterControl.filter;

import org.editor.tools.filterTool.filterControl.Filter;

public class Original implements Filter {
    @Override
    public int applyFilter(int argb, double factor) {
        return argb;
    }
}
