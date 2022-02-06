package org.launcher;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.database.User;
import org.marketplace.gallery.GalleryController;
import org.screenControl.ControlScreen;
import org.screenControl.ScreenName;
import org.screenControl.ScreensController;
import org.screenControl.Window;

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
     * Init nodes
     * */
    public void initModeSelectionElements() {
        editorPane.setCache(true);
        editorPane.setCacheHint(CacheHint.SPEED);

        marketplacePane.setCache(true);
        marketplacePane.setCacheHint(CacheHint.SPEED);
    }

    /**
     * Initially hides certain elements from view to be revealed during entering.
     */
    public void hideElements() {
        // Hide background image
        screensController.getParent().getChildrenUnmodifiable().get(0).setOpacity(0);

        marketplaceButton.setText("Starting App");
        usernameLabel.setOpacity(0);
        profilePane.setOpacity(0);
        editorPane.setOpacity(0);
        editorPane.toFront();
        marketplacePane.setOpacity(0);
    }

    @FXML
    private void enterEditor(ActionEvent event) {
        screensController.setScreen(ScreenName.PROJECT_SETTINGS);
    }

    /**
     * Open the gallery.
     */
    public void openMarketplace() {
        if(((GalleryController)screensController.getController(ScreenName.GALLERY)).setOpen(-1)){
            screensController.setScreen(ScreenName.GALLERY);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING,"Images did not finish loading please wait.");
            alert.show();
        }
    }

    /**
     * Start the timed animation opening sequence
     */
    public void startOpeningSequence() {
        marketplaceButton.setText("Marketplace");

        FadeTransition marketplaceFadeIn = getFade(editorPane, 500, 500, 1);
        marketplaceFadeIn.play();

        TranslateTransition editorTranslateLeft = getTranslate(editorPane, 1200.0, 500.0, 0.0, -300.0, 0.0, 0.0);
        TranslateTransition marketplaceTranslateRight = getTranslate(marketplacePane, 1200.0, 500.0, 0.0, 300.0, 0.0, 0.0);
        FadeTransition editorFadeIn = getFade(marketplacePane, 500.0, 100.0, 1.0);

        ParallelTransition paneTransition = new ParallelTransition();
        paneTransition.getChildren().addAll(
                editorTranslateLeft,
                marketplaceTranslateRight,
                editorFadeIn
        );

        FadeTransition backgroundFadeIn = getFade(screensController.getParent().getChildrenUnmodifiable().get(0), 0.0, 500.0, 1.0);
        FadeTransition iconFadeIn = getFade(profilePane, 250, 500, 1.0);
        FadeTransition usernameFadeIn = getFade(usernameLabel, 400.0, 500.0, 1.0);

        SequentialTransition modeSequence = new SequentialTransition();
        modeSequence.getChildren().addAll(
                new PauseTransition(Duration.millis(400)),
                paneTransition,
                backgroundFadeIn,
                iconFadeIn,
                usernameFadeIn
        );
        modeSequence.play();
    }

    private TranslateTransition getTranslate(Node node, double duration, double delay, double translateFromX, double translateToX, double translateFromY, double translateToY) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setDelay(Duration.millis(delay));
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        translateTransition.setFromX(translateFromX);
        translateTransition.setToX(translateToX);
        translateTransition.setFromY(translateFromY);
        translateTransition.setToY(translateToY);

        return  translateTransition;
    }

    private FadeTransition getFade(Node node, double delayInMillis, double duration, double value) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setToValue(value);
        fadeTransition.setDelay(Duration.millis(delayInMillis));

        return fadeTransition;
    }
}
