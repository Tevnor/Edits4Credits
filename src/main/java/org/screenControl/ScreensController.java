package org.screenControl;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static org.screenControl.ScreenName.*;

/**
 * <p>
 *     Switching between the application's GUI screens is facilitated here.
 *     Coordination between screen names and FXML files is done via HashMaps
 *     Smooth user experience is created by timed transitions and fades.
 * </p>
 * @see StackPane
 * @see HashMap
 * @see Window
 * @see Node
 */
public class ScreensController extends StackPane {

    private static final Logger SC_LOGGER = LogManager.getLogger(ScreensController.class);

    private final HashMap<ScreenName, Node> SCREEN_TO_NODE_MAP = new HashMap<>();
    private final HashMap<ScreenName, ControlScreen> SCREEN_TO_CONTROLLER_MAP = new HashMap<>();
    private Window window;


    /**
     * Instantiates a new Screens controller.
     */
    public ScreensController() {
        super();
    }

    /**
     * Add screen's node to the stack.
     *
     * @param name   the name
     * @param screen the screen
     */
    private void addScreen(ScreenName name, Node screen) {
        SCREEN_TO_NODE_MAP.put(name, screen);
    }

    /**
     * Get a screen's node via name
     *
     * @param name the name
     * @return the screen's node
     */
    private Node getScreen(ScreenName name) {
        return SCREEN_TO_NODE_MAP.get(name);
    }

    /**
     * Analyze the user's monitor specification to make a tailored screen fit possible.
     * @see Screen
     * @see GraphicsDevice
     */
    public void getMonitorInfo() {
        Screen screen = Screen.getPrimary();
        double scaleX = screen.getOutputScaleX();
        double scaleY = screen.getOutputScaleY();

        GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = monitor.getDisplayMode().getWidth() / scaleX;
        double height = monitor.getDisplayMode().getHeight() / scaleY;

        this.window = new Window(width, height);
        SC_LOGGER.debug("Monitor info analyzed. Width: " + width + ", height: " + height + ", scale: " + scaleX);
    }

    /**
     * Gets window object containing the user's relevant window/monitor specifications.
     *
     * @return the window
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Retrieves path of the FXML file from SCREEN_NAME_TO_PATH_MAP belonging via name as key.
     * Puts screen onto screenToController stack to make it available for setting.
     *
     * @param screenName the name
     * @return the boolean
     * @see ScreenName
     * @see FXMLLoader
     * @see Parent
     */
// Load FXML file, use addScreen(), get controller from parent tree structure, and inject screen into controller
    public boolean loadScreen(ScreenName screenName){
        try {
            FXMLLoader screenLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(SCREEN_NAME_TO_PATH_MAP.get(screenName))));
            Parent loadScreen = screenLoader.load();
            ControlScreen controlScreen = screenLoader.getController();
            controlScreen.setScreenParent(this);
            controlScreen.setWindow(getWindow());
            SCREEN_TO_CONTROLLER_MAP.put(screenName,controlScreen);

            // add scene excluding background
            AnchorPane ap = (AnchorPane) loadScreen;
            Pane loadPane = (Pane) ap.getChildren().get(1);

            addScreen(screenName, loadPane);

            SC_LOGGER.debug("Screen {} loaded.", screenName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            SC_LOGGER.error("Screen {} could not be loaded.", screenName);
            return false;
        }
    }



    /**
     * Get ControlScreen object via corresponding screen's name
     *
     * @param name the name active screen
     * @return the ControlScreen object
     */
    public ControlScreen getController(ScreenName name){
        return SCREEN_TO_CONTROLLER_MAP.get(name);
    }

    /**
     * Center the upcoming screen according to dimensions of the contained elements.
     *
     * @param name the name of the C screen
     */
    private void setCenter(ScreenName name) {
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

    /**
     * Gets the node of the wanted screen from the screenToNodeMap via the passed in Enum as key.
     * Removes previous screen from the top of the stack and replaces it with the new screen, setting it active and viewable.
     * Transition done with timed fade-outs/fade-ins
     *
     * @param name the name of the upcoming screen
     * @return the boolean depending on successful execution
     * @see Timeline
     * @see KeyFrame
     * @see KeyValue
     * @see PauseTransition
     */
    public boolean setScreen(final ScreenName name) {
        if (SCREEN_TO_NODE_MAP.get(name) != null) {
            final DoubleProperty opacity = opacityProperty();

            // In case of one more active screens
            if (!getChildren().isEmpty()) {
                Timeline screenFade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1000), actionEvent -> {
                            getChildren().remove(0);
                            getChildren().add(0, SCREEN_TO_NODE_MAP.get(name));
                            SC_LOGGER.debug("Screen set to: " + name);

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
                getChildren().add(SCREEN_TO_NODE_MAP.get(name));
                setCenter(name);

                Timeline screenFadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                PauseTransition delayCenter = new PauseTransition(Duration.seconds(0.75));
                delayCenter.setOnFinished( event -> screenFadeIn.play());
                delayCenter.play();
            }
            return true;
        } else {
            SC_LOGGER.error("Screen has not been loaded.");
            return false;
        }
    }

    /**
     * If a certain screen needs to be unloaded, this method can be called to do so.
     *
     * @param name the name of the departing screen
     * @return the boolean depending on successful execution
     */
    public boolean unloadScreen(ScreenName name) {
        if (SCREEN_TO_NODE_MAP.remove(name) != null) {
            SC_LOGGER.debug(name + " screen unloaded.");
            return true;
        } else {
            SC_LOGGER.error("Screen doesn't exist.");
            return false;
        }
    }
}
