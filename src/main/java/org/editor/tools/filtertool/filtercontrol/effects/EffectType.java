package org.editor.tools.filtertool.filtercontrol.effects;

import org.editor.tools.filtertool.filtercontrol.Effect;
import org.editor.tools.filtertool.filtercontrol.adjustments.BrightnessAdjustment;
import org.editor.tools.filtertool.filtercontrol.adjustments.ContrastAdjustment;
import org.editor.tools.filtertool.filtercontrol.adjustments.SaturationAdjustment;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Match effect names to their functionality
 */
public enum EffectType {
    BLUR,
    DISPLACE,
    SEPIA,
    BRIGHTNESS,
    CONTRAST,
    SATURATION;

    /**
     * Original & private map that won't be accessed directly.
     * */
    private static final EnumMap<EffectType, Effect> effectTypeEnumMap = new EnumMap<>(Map.of(
            BLUR, new BlurEffect(),
            DISPLACE, new DisplacementEffect(),
            SEPIA, new SepiaEffect(),
            BRIGHTNESS, new BrightnessAdjustment(),
            CONTRAST, new ContrastAdjustment(),
            SATURATION, new SaturationAdjustment()
    ));

    /**
     * Static, public, and safely accessible, unmodifiable Map derived from effectTypeEnumMap.
     * Methods may access this map to instantiate effect object via the effect's name.
     */
    public static final Map<EffectType, Effect> TYPE_TO_EFFECT_ENUM_MAP = Collections.unmodifiableMap(effectTypeEnumMap);
}
