package org.editor.tools.filterTool.filterControl;

import org.editor.tools.filterTool.filterControl.filter.GlitchFilter;
import org.editor.tools.filterTool.filterControl.filter.Original;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class FilterTest {
    private Filter filterA;
    private Filter filterB;

    @Before
    public void setUp() {
        filterA = new GlitchFilter();
        filterB = new Original();
    }

    @Test
    public void applyFilterA_Valid() {
        int argb = 333666;
        double factor = 2;

        Assertions.assertEquals(111222, filterA.applyFilter(argb, factor));
    }
    @Test
    public void applyFilterA_Invalid() {
        int argb = 983457945;
        double factor = 47856;

        Assertions.assertNotEquals(987, filterA.applyFilter(argb, factor));
    }

    @Test
    public void applyFilterB_Valid() {
        int argb = 547;
        double factor = 981230;

        Assertions.assertEquals(547, filterB.applyFilter(argb, factor));
    }
    @Test
    public void applyFilterB_Invalid() {
        int argb = 43657;
        double factor = 980;

        Assertions.assertNotEquals(3434, filterB.applyFilter(argb, factor));
    }

}