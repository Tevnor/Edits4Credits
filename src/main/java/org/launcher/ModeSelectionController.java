package org.launcher;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.database.User;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The first full screen view.
 * Select between the two main modes
 * a) Editor
 * b) Gallery
 */
public class ModeSelectionController implements Initializable, ControlScreen {

    private ScreensController screensController;
    private double screenWidth;
    private double screenHeight;

    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button editorButton;
    @FXML
    private Button marketplaceButton;
    @FXML
    private Pane modeSelectorElementsPane;
    @FXML
    private Pane editorPane;
    @FXML
    private Pane marketplacePane;
    @FXML
    private Pane profilePane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Set the welcome text to greet the logged in user.
     *
     * @param user the logged in user
     */
    public void initUserData(User user) {
        usernameLabel.setText(user.getUsername());
    }

    public void setScreenParent(ScreensController screenParent) {
        screensController = screenParent;
    }
    public void setWindow(Window window) {
        this.screenWidth = window.getScreenWidth();
        this.screenHeight = window.getScreenHeight();
    }

    /**
     * Sets root anchor pane.
     */
    public void setRootAnchorPane() {
        this.rootAnchorPane = new AnchorPane();
        rootAnchorPane.setPrefWidth(screenWidth);
        rootAnchorPane.setPrefHeight(screenHeight);
    }

    /**
     * Initially hides certain elements from view to be revealed during entering.
     */
    public void hideElements() {
        screensController.getParent().getChildrenUnmodifiable().get(0).setOpacity(0);
        editorButton.setOpacity(0);
        marketplaceButton.setText("Starting App.");
        startFade(marketplaceButton, 500, 1800, 0);
        usernameLabel.setOpacity(0);
        profilePane.setOpacity(0);

        //
        editorPane.setOpacity(0.9);
        marketplacePane.setOpacity(0.9);
    }

    @FXML
    private void enterEditor(ActionEvent event) {
        screensController.setScreen(ScreenName.PROJECT_SETTINGS);
    }

    /**
     * Open the gallery.
     *
     * @param ae the ae
     */
    public void openMarketplace(ActionEvent ae) {

    }

    /*
     * Animations
     */

    /**
     * Start the timed animation opening sequence
     */
    public void startAnimations() {
        // Slide two main mode panes outwards
        startTranslation(editorPane, -300);
        startTranslation(marketplacePane, 300);
        // Fade in background image
        startFade(screensController.getParent().getChildrenUnmodifiable().get(0), 100,1250, 1);
        // Fade in profile icon
        startFade(profilePane, 900, 1750, 1);
        // Fade in enter buttons
        startFade(editorButton, 900, 1750, 0.7);
        startFade(marketplaceButton, 900, 1750, 0.7);
        // Reset marketplace button text
        PauseTransition delay = new PauseTransition(Duration.seconds(1.6));
        delay.setOnFinished(event -> marketplaceButton.setText("Marketplace"));
        delay.play();
        // Fade in user greeting
        startFade(usernameLabel, 1200, 2500, 1);
        // Reset main mode panes' opacity
        startFade(editorPane, 200, 0, 0.9);
        startFade(marketplacePane, 200, 0, 0.9);
    }

    /**
     * Translate node in X direction
     *
     * @param node      the selected node
     * @param direction the translation distance in pixels (negative values -> left, positive values -> right)
     */
    public void startTranslation(Node node, double direction) {
        KeyValue keyValue = new KeyValue(node.translateXProperty(), direction);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.3), keyValue);
        Timeline timeline = new Timeline(keyFrame);
        Duration duration = new Duration(500);

        timeline.setDelay(duration);

        timeline.play();
    }

    /**
     * Fade elements into the screen
     *
     * @param node  the selected node
     * @param dur   the duration the fade is supposed to take in milliseconds
     * @param del   the delay the fade is supposed the wait for in milliseconds
     * @param value the value to which the fade is supposed to set the final opacity (0.0-1.0)
     */
// Fade node in/out
    public void startFade(Node node, double dur, double del, double value) {
        Duration fadeDuration = new Duration(dur);
        Duration fadeDelay = new Duration(del);

        FadeTransition fade = new FadeTransition();
        fade.setFromValue(0);
        fade.setToValue(value);
        fade.setDelay(fadeDelay);
        fade.setDuration(fadeDuration);
        fade.setNode(node);

        fade.play();
    }
}
