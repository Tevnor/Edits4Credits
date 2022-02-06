package org.screencontrol;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

public class ScreensControllerTest extends ApplicationTest {
    private final JFXPanel panel = new JFXPanel();
    private ScreensController screensController;
    private ScreenName screenName;

    @Test
    public void loadScreen_Valid() {
        interact(() -> {
            screensController = new ScreensController();
            screenName = ScreenName.PROJECT_SETTINGS;
        });
        Platform.runLater(() -> Assertions.assertTrue(screensController.loadScreen(screenName)));
    }

    @Test
    public void getController_Valid() {
        interact(() -> {
            screensController = new ScreensController();
            screenName = ScreenName.EDITOR;
        });
        Platform.runLater(() -> Assertions.assertTrue(screensController.loadScreen(screenName)));
    }

    @Test
    public void setScreen_Valid() {
            interact(() -> {
                screensController = new ScreensController();
                screenName = ScreenName.EDITOR;
                screensController.loadScreen(screenName);
            });
            Platform.runLater(() -> Assertions.assertTrue(screensController.setScreen(screenName)));
    }
    @Test
    public void setScreen_InvalidNotLoaded() {
        interact(() -> {
            screensController = new ScreensController();
            screenName = ScreenName.GALLERY;
        });
        Platform.runLater(() -> Assertions.assertFalse(screensController.setScreen(screenName)));
    }

    @Test
    public void setScreen_InvalidNullArgument() {
        interact(() -> {
            screensController = new ScreensController();
            screenName = null;
        });
        Platform.runLater(() -> Assertions.assertFalse(screensController.setScreen(screenName)));
    }

    @Test
    public void unloadScreen_Valid() {
        interact(() -> {
            screensController = new ScreensController();
            screenName = ScreenName.GALLERY;
            screensController.loadScreen(screenName);
        });
        Platform.runLater(() -> Assertions.assertTrue(screensController.unloadScreen(screenName)));
    }

    @Test
    public void unloadScreen_ValidNotOnlyScreen() {
        interact(() -> {
            screensController = new ScreensController();
            screenName = ScreenName.GALLERY;
            ScreenName screenName2 = ScreenName.PROJECT_SETTINGS;
            screensController.loadScreen(screenName);
            screensController.loadScreen(screenName2);
        });
        Platform.runLater(() -> Assertions.assertTrue(screensController.unloadScreen(screenName)));
    }

    @Test
    public void unloadScreen_InvalidNotLoadedYet() {
        interact(() -> {
            screensController = new ScreensController();
            screenName = ScreenName.GALLERY;
        });
        Platform.runLater(() -> Assertions.assertFalse(screensController.unloadScreen(screenName)));
    }

    @Override
    public void start(Stage stage) {

    }
}