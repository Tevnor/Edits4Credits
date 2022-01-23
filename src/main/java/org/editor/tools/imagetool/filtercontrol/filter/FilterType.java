package org.editor.tools.imagetool.filtercontrol.filter;

import org.editor.tools.imagetool.filtercontrol.Filter;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public enum FilterType {
    ORIGINAL,
    GLITCH,
    GRAYSCALE,
    INVERTED;

    private static final EnumMap<FilterType, Filter> typeFilterEnumMap = new EnumMap<>(Map.of(
            ORIGINAL, new Original(),
            GLITCH, new GlitchFilter(),
            GRAYSCALE, new GrayscaleFilter(),
            INVERTED, new InvertedFilter()
    ));

    public static final Map<FilterType, Filter> TYPE_TO_FILTER_ENUM_MAP = Collections.unmodifiableMap(typeFilterEnumMap);
}
