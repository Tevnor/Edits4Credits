package org.controller.tools.imagetool.filtercontrol.filter;

import org.controller.tools.imagetool.filtercontrol.Filter;

public class Original implements Filter {
    @Override
    public int applyFilter(int argb, double factor) {
        return argb;
    }
}
