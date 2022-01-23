package org.editor.tools.imagetool.filtercontrol.filter;

import org.editor.tools.imagetool.filtercontrol.Filter;

public class Original implements Filter {
    @Override
    public int applyFilter(int argb, double factor) {
        return argb;
    }
}
