package org.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModeSelectionController implements Initializable, ControlScreen {

    ScreensController screensController;
    Window window;
    private FXMLLoader loader;
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

    public void initUserData(User user) {
        usernameLabel.setText(user.getUsername());
    }
    public void setScreenParent(ScreensController screenParent) {
        screensController = screenParent;
    }
    public void setWindow(Window window) {
        this.window = window;
        this.screenWidth = window.getScreenWidth();
        this.screenHeight = window.getScreenHeight();
    }
    public void setRootAnchorPane() {
        this.rootAnchorPane = new AnchorPane();
        rootAnchorPane.setPrefWidth(screenWidth);
        rootAnchorPane.setPrefHeight(screenHeight);
    }

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
        screensController.setScreen(GuiDriver.settingsScreenID);
    }

    public void openMarketplace(ActionEvent ae) {

    }

    /*
     * Animations
     */

    // Animation trigger sequence
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

    // Slide node in X/Y direction
    public void startTranslation(Node node, double direction) {
        KeyValue keyValue = new KeyValue(node.translateXProperty(), direction);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.3), keyValue);
        Timeline timeline = new Timeline(keyFrame);
        Duration duration = new Duration(500);

        timeline.setDelay(duration);

        timeline.play();
    }

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
