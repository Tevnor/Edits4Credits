package org.editor.tools.filtertool.filtercontrol.filter;

import org.editor.tools.filtertool.filtercontrol.Filter;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public enum FilterType {
    ORIGINAL,
    GLITCH,
    GRAYSCALE,
    INVERTED,
    NOISE;

    private static final EnumMap<FilterType, Filter> typeFilterEnumMap = new EnumMap<>(Map.of(
            ORIGINAL, new Original(),
            GLITCH, new GlitchFilter(),
            GRAYSCALE, new GrayscaleFilter(),
            INVERTED, new InvertedFilter(),
            NOISE, new NoiseFilter()
    ));

    public static final Map<FilterType, Filter> TYPE_TO_FILTER_ENUM_MAP = Collections.unmodifiableMap(typeFilterEnumMap);

//    FilterType(String filterName) {
//        this.f
//    }
}
