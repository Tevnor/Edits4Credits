package org.editor.tools.drawingtool.graphiccontrol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ShapePixelMap extends HashMap<Integer,Integer> {
    private static final Logger SPM_LOGGER = LogManager.getLogger(ShapePixelMap.class.getName());

    private Set<Integer> locked = new HashSet<>();
    public ShapePixelMap(){}
    public ShapePixelMap(ShapePixelMap input){
        super(input);
    }

    public void addLockPixels(Set<Integer> locks){
        locked.addAll(locks);
    }
    public void replace(Map<Integer,Integer> input){
        input.keySet().forEach(k -> {
            if (!locked.contains(k)){
                if (input.containsKey(k)) {
                    replace(k, input.get(k));
                }
            }
        });
        SPM_LOGGER.debug("replaced values with input map");
    }
    public Set<Integer> getIntersect(ShapePixelMap input){
        SPM_LOGGER.debug("getIntersect() entered");
        HashSet<Integer> intersect = new HashSet<>(keySet());
        intersect.retainAll(input.keySet());
        return intersect;
    }

}
