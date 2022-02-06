package org.editor.tools.filterTool.filterControl.filter;

import org.editor.tools.filterTool.filterControl.Filter;

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
     * @see EnumMap
     * @see Map
     * @see Filter
     * */
    private static final EnumMap<FilterType, Filter> typeFilterEnumMap = new EnumMap<>(Map.of(
            ORIGINAL, new Original(),
            GLITCH, new GlitchFilter(),
            GRAYSCALE, new GrayscaleFilter(),
            INVERTED, new InvertedFilter(),
            NOISE, new NoiseFilter()
    ));

    /**
     * <p>
     *     Static, public, and safely accessible, unmodifiable Map derived from typeFilterEnumMap.
     *     Methods may access this map to instantiate filter object via the filter's name.
     * </p>
     * @see Map
     * @see FilterType
     * @see Filter
     * @see Collections
     * @see Collections#unmodifiableMap(Map)
     */
    public static final Map<FilterType, Filter> TYPE_TO_FILTER_ENUM_MAP = Collections.unmodifiableMap(typeFilterEnumMap);

}
