package org.editor.tools.drawingTool;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class ShapePixelMapTest extends TestCase {

    final ShapePixelMap spm = new ShapePixelMap();
    final ShapePixelMap test = new ShapePixelMap();
    final ShapePixelMap testControl = new ShapePixelMap();
    final Set<Integer> locks = new HashSet<>();

    public void testAddLockPixels() {
        set();
        spm.addLockPixels(locks);
        spm.replace(test);
        assertFalse(testControl.equals(spm));
    }

    public void testReplace() {
        set();
        spm.replace(test);
        assertTrue(testControl.equals(spm));
    }

    public void testGetIntersect() {
        set();
        assertTrue(locks.equals(spm.getIntersect(test)));
    }

    private void set(){
        spm.put(1,1);
        spm.put(2,2);
        spm.put(3,3);
        spm.put(4,4);
        spm.put(5,5);

        locks.add(2);
        locks.add(3);

        test.put(2,4);
        test.put(3,5);

        testControl.put(1,1);
        testControl.put(2,4);
        testControl.put(3,5);
        testControl.put(4,4);
        testControl.put(5,5);
    }
}