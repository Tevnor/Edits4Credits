package org.controller;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public enum ScreenName {
    MODE_SELECTION,
    PROJECT_SETTINGS,
    EDITOR;

    private static final EnumMap<ScreenName, String> screenToPathMap = new EnumMap<>(Map.of(
            MODE_SELECTION, "/fxml/modeSelection.fxml",
            PROJECT_SETTINGS, "/fxml/settings.fxml",
            EDITOR, "/fxml/editor.fxml"
    ));

    public static final Map<ScreenName, String> SCREEN_NAME_TO_PATH_MAP = Collections.unmodifiableMap(screenToPathMap);
}
