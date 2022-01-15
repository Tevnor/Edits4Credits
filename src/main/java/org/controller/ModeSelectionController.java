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
        editorButton.setOpacity(0);
        marketplaceButton.setOpacity(0);
        usernameLabel.setOpacity(0);
        profilePane.setOpacity(0);
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
    public void startAnimations() {
        startTranslation(editorPane, -300);
        startTranslation(marketplacePane, 300);

//        startFade(backgroundImageView, 1000, 1);
        startFade(profilePane, 1750, 1);
        startFade(editorButton, 1750, 0.7);
        startFade(marketplaceButton, 1750, 0.7);
        startFade(usernameLabel, 2500, 1);
    }

    public void startTranslation(Node node, double direction) {
        KeyValue keyValue = new KeyValue(node.translateXProperty(), direction);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.4), keyValue);
        Timeline timeline = new Timeline(keyFrame);
        Duration duration = new Duration(300);

        timeline.setDelay(duration);

        timeline.play();
    }

    public void startFade(Node node, double time, double value) {
        Duration fadeDuration = new Duration(750);
        Duration fadeDelay = new Duration(time);

        FadeTransition fade = new FadeTransition();
        fade.setFromValue(0);
        fade.setToValue(value);
        fade.setDelay(fadeDelay);
        fade.setDuration(fadeDuration);
        fade.setNode(node);

        fade.play();
    }
}
