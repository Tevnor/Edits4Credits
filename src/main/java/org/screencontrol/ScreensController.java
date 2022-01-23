package org.screencontrol;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ScreensController extends StackPane {

    private final HashMap<ScreenName, Node> screenToNodeMap = new HashMap<>();
    private Window window;
    private FXMLLoader loader;

    public ScreensController() {
        super();
    }

    // Add new scene to the HashMap
    public void addScreen(ScreenName name, Node screen) {
        screenToNodeMap.put(name, screen);
    }

    // Return the screen node with the corresponding name
    public Node getScreen(ScreenName name) {
        return screenToNodeMap.get(name);
    }

    public void getMonitorInfo() {
        Screen screen = Screen.getPrimary();
        double scaleX = screen.getOutputScaleX();
        double scaleY = screen.getOutputScaleY();

        GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = monitor.getDisplayMode().getWidth() / scaleX;
        double height = monitor.getDisplayMode().getHeight() / scaleY;

        this.window = new Window(width, height);
    }

    public Window getWindow() {
        return window;
    }

    // Load FXML file, use addScreen(), get controller from parent tree structure, and inject screen into controller
    public boolean loadScreen(ScreenName name){
        try {
            loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(ScreenName.SCREEN_NAME_TO_PATH_MAP.get(name))));
            Parent loadScreen = loader.load();
            ControlScreen controlScreen = loader.getController();
            controlScreen.setScreenParent(this);
            controlScreen.setWindow(getWindow());

            // add scene excluding background
            AnchorPane ap = (AnchorPane) loadScreen;
            Pane loadPane = (Pane) ap.getChildren().get(1);

            addScreen(name, loadPane);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public FXMLLoader getLoader() {
        return loader;
    }

    public void setCenter(ScreenName name) {
        switch (name)  {
            case MODE_SELECTION:
                setLayoutX(window.getScreenWidth() / 2 - 500);
                setLayoutY(window.getScreenHeight() / 2 - 300);
                break;
            case PROJECT_SETTINGS:
                setLayoutX(window.getScreenWidth() / 2 - 300);
                setLayoutY(window.getScreenHeight() / 2 - 300);
                break;
            default:
                setLayoutX(0);
                setLayoutY(0);
        }
    }

    public boolean setScreen(final ScreenName name) {
        if (screenToNodeMap.get(name) != null) {
            final DoubleProperty opacity = opacityProperty();

            // In case of one more active screens
            if (!getChildren().isEmpty()) {
                Timeline screenFade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1000), actionEvent -> {
                            getChildren().remove(0);
                            getChildren().add(0, screenToNodeMap.get(name));

                            Timeline screenFadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                            screenFadeIn.play();
                        }, new KeyValue(opacity, 0.0)));
                screenFade.play();

                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished( event -> setCenter(name));
                delay.play();
            // In case of no active screens
            } else {
                setOpacity(0.0);
                getChildren().add(screenToNodeMap.get(name));
                setCenter(name);

                Timeline screenFadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                PauseTransition delayCenter = new PauseTransition(Duration.seconds(0.75));
                delayCenter.setOnFinished( event -> screenFadeIn.play());
                delayCenter.play();
            }
            return true;
        // If no such screen exists
        } else {
            System.err.println("Screen has not been loaded.");
            return false;
        }
    }

    public boolean unloadScreen(ScreenName name) {
        if (screenToNodeMap.remove(name) != null) {
            return true;
        } else {
            System.err.println("Screen doesn't exist.");
            return false;
        }
    }
}