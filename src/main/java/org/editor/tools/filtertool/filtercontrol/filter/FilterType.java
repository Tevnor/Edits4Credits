package org.editor.tools.filtertool.filtercontrol.filter;

import org.editor.tools.filtertool.filtercontrol.Filter;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Match filter names to their functionality
 */
public enum FilterType {
    ORIGINAL,
    GLITCH,
    GRAYSCALE,
    INVERTED,
    NOISE;

    /**
     * Original & private map that won't be accessed directly.
     * */
    private static final EnumMap<FilterType, Filter> typeFilterEnumMap = new EnumMap<>(Map.of(
            ORIGINAL, new Original(),
            GLITCH, new GlitchFilter(),
            GRAYSCALE, new GrayscaleFilter(),
            INVERTED, new InvertedFilter(),
            NOISE, new NoiseFilter()
    ));

    /**
     * Static, public, and safely accessible, unmodifiable Map derived from typeFilterEnumMap.
     * Methods may access this map to instantiate filter object via the filter's name.
     */
    public static final Map<FilterType, Filter> TYPE_TO_FILTER_ENUM_MAP = Collections.unmodifiableMap(typeFilterEnumMap);

}
