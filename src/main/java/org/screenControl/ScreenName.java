package org.screenControl;

import org.editor.EditorController;
import org.editor.project.SettingsController;
import org.launcher.ModeSelectionController;
import org.marketplace.gallery.GalleryController;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Enums of all application's screens, referable by name
 */
public enum ScreenName {

    MODE_SELECTION,
    PROJECT_SETTINGS,
    EDITOR,
    GALLERY;

    /**
     * Original & private map that won't be accessed directly.
     * */
    private static final EnumMap<ScreenName, ControlScreen> screenNameToPathMap = new EnumMap<>(Map.of(
            MODE_SELECTION, new ModeSelectionController(),
            PROJECT_SETTINGS, new SettingsController(),
            EDITOR, new EditorController(),
            GALLERY, new GalleryController()
    ));
    /**
     * Static, public, and safely accessible, unmodifiable Map derived from screenNameToPathMap.
     * Methods may access this map to get the Controller to the corresponding screen names.
     */
    public static final Map<ScreenName, ControlScreen> SCREEN_NAME_TO_CONTROLLER_MAP = Collections.unmodifiableMap(screenNameToPathMap);

    /**
     * Original & private map that won't be accessed directly.
     * */
    private static final EnumMap<ScreenName, String> screenToPathMap = new EnumMap<>(Map.of(
            MODE_SELECTION, "/fxml/modeSelection.fxml",
            PROJECT_SETTINGS, "/fxml/settings.fxml",
            EDITOR, "/fxml/editor.fxml",
            GALLERY, "/fxml/gallery.fxml"
    ));

    /**
     * Static, public, and safely accessible, unmodifiable Map derived from screenToPathMap.
     * Methods may access this map to get the FXML file paths to the corresponding screen names.
     */
    public static final Map<ScreenName, String> SCREEN_NAME_TO_PATH_MAP = Collections.unmodifiableMap(screenToPathMap);
}
