package org.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
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

    private HashMap<String, Node> screenMap = new HashMap<>();

    private Window window;
    private Scene loginScene;
    private Parent loadScreen;
    private Pane loadPane;
    private ImageView imageView;
    private FXMLLoader loader;

    public ScreensController() {
        super();
    }

    // Add new scene to the HashMap
    public void addScreen(String name, Node screen) {
        screenMap.put(name, screen);
    }

    // Return the screen node with the corresponding name
    public Node getScreen(String name) {
        return screenMap.get(name);
    }

    public void getMonitorInfo() {
        GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = monitor.getDisplayMode().getWidth();
        double height = monitor.getDisplayMode().getHeight();

        Screen screen = Screen.getPrimary();
        double scaleX = screen.getOutputScaleX();
        double scaleY = screen.getOutputScaleY();

        this.window = new Window(width, height, scaleX, scaleY);
    }

    public Window getWindow() {
        return window;
    }

    // Load FXML file, use addScreen(), get controller from parent tree structure, and inject screen into controller
    public boolean loadScreen(String name, String resource){
        try {
            loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(resource)));
            this.loadScreen = loader.load();
            ControlScreen controlScreen = loader.getController();
            controlScreen.setScreenParent(this);
            controlScreen.setWindow(getWindow());

            // add scene excluding background
            AnchorPane ap = (AnchorPane) loadScreen;
            this.loadPane = (Pane) ap.getChildren().get(1);

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
    public Parent getLoadScreen() {
        return loadScreen;
    }
    public Scene getNewScene() {
        return loginScene;
    }

    public void setCenter(String name) {
        switch (name)  {
            case "modeSelection":   setLayoutX(window.getScreenWidth() / 4d - 500);
            setLayoutY(window.getScreenHeight() / 4d - 300);
                                    break;
            case "settings":        setLayoutX(window.getScreenWidth() / 4d - 300);
                                    setLayoutY(window.getScreenHeight() / 4d - 300);
                                    break;
            default:                setLayoutX(0);
                                    setLayoutY(0);
        }
    }

    public boolean setScreen(final String name) {
        if (screenMap.get(name) != null) {

            final DoubleProperty opacity = opacityProperty();
            // In case of one more active screens
            if (!getChildren().isEmpty()) {

                Timeline screenFade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                            getChildren().remove(0);
                            getChildren().add(0, screenMap.get(name));
//                        }
                        Timeline screenFadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                        screenFadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                screenFade.play();

                PauseTransition delay = new PauseTransition(Duration.seconds(.5));
                delay.setOnFinished( event -> setCenter(name));
                delay.play();
            // In case of no active screens
            } else {
                setOpacity(0.0);
                getChildren().add(screenMap.get(name));

                Timeline screenFadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                screenFadeIn.play();

                PauseTransition delay = new PauseTransition(Duration.seconds(.5));
                delay.setOnFinished( event -> setCenter(name));
                delay.play();
            }
            return true;
        // If no such screen exists
        } else {
            System.err.println("Screen has not been loaded.");
            return false;
        }
    }

    public boolean unloadScreen(String name) {
        if (screenMap.remove(name) != null) {
            return true;
        } else {
            System.err.println("Screen doesn't exist.");
            return false;
        }
    }
}
