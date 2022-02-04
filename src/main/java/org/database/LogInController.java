package org.database;


import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.launcher.ModeSelectionController;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import java.net.URL;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogInController implements Initializable, ControlScreen {

    private ScreensController screensController;
    private Stage loginStage;
    private Stage appStage;
    private User user;
    private Window window;
    private FXMLLoader loader;
    private ModeSelectionController modeSelector;

    public LogInController(){
    }

    @FXML
    private Rectangle loginBackground;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label loginError;
    @FXML
    private Button logInButton;
    @FXML
    private Button cancelButton;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView loginImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initLogin() {
        setCenter();
        initLoginElements();
        hideElements();
        preloadScreens();
        initAppStages();
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        this.screensController = screenPage;
    }
    public void setWindow(Window window) {
        this.window = window;
    }
    public void setCenter() {
        double xCenter = window.getScreenWidth() / 2;
        double yCenter = window.getScreenHeight() / 2;

        rootPane.setLayoutX(xCenter - 200);
        rootPane.setLayoutY(yCenter - 300);
    }

    @FXML
    private void enterMainApp(ActionEvent event) {
        loginStage = (Stage) logInButton.getScene().getWindow();
        user = new User(username.getText());
        if (username!=null && password != null) {
            createApp();
            startClosingAnimation();
        } else {
            loginError.setText("Invalid login. Please try again.");
        }
    }

//    public void userLogIn(ActionEvent event) throws IOException {
//        //validateLogin();
//        replacementLogin();
//    }

//    public void replacementLogin(){
//        user = new User(username.getText());
//        if (username!=null && password != null){
//            enterApp();
//        }
//        else {
//            loginError.setText("Invalid login. Please try again.");
//        }
//    }

//    public void validateLogin(){
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.getConnection();
//
//        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = '" + username.getText() + "'AND password ='" + password.getText() + "'";
//
//
//        try {
//            Statement statement = connectDB.createStatement();
//            ResultSet queryResult = statement.executeQuery(verifyLogin);
//
//            while(queryResult.next()){
//                if (queryResult.getInt(1) == 1) {
//                    // Create user instance with logged in user
//
//                    String username = "TestUser";
//                    user = new User(username);
////                    user = new User(username.getText());
//                    enterApp();
//                } else {
//                    loginError.setText("Invalid login. Please try again.");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            e.getCause();
//        }
//    }

    private void preloadScreens() {
        screensController.loadScreen(ScreenName.MODE_SELECTION);
        loader = screensController.getLoader();
        screensController.loadScreen(ScreenName.PROJECT_SETTINGS);
        screensController.loadScreen(ScreenName.GALLERY);
        screensController.loadScreen(ScreenName.EDITOR);
        screensController.setScreen(ScreenName.MODE_SELECTION);
    }
    private void initAppStages(){
        ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/e4c-bg.png")), window.getScreenWidth(), window.getScreenHeight(), false, true));
        Group root = new Group();
        root.getChildren().addAll(iv, screensController);

        Scene scene = new Scene(root, Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(LogInController.class.getResource("/styles/styles.css")).toExternalForm());
        appStage = new Stage();
        appStage.initStyle(StageStyle.TRANSPARENT);
        appStage.setMaximized(true);
        appStage.setScene(scene);
    }
    private void createApp() {

        Platform.runLater(() -> {
            try {
                // Pass logged in user to menu class
                modeSelector = loader.getController();
                modeSelector.initUserData(user);
                modeSelector.setWindow(window);
                modeSelector.setRootAnchorPane();
                modeSelector.initModeSelectionElements();
                modeSelector.hideElements();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        });
    }

    @FXML
    private void cancelLogin(ActionEvent event) {
        loginStage = (Stage) cancelButton.getScene().getWindow();
        closeLoginStage(1000);
    }

    private void closeLoginStage(double duration){
        FadeTransition fadeToClose = getFade(rootPane, 0, duration, 0);
        fadeToClose.play();
        fadeToClose.setOnFinished(e -> loginStage.close());
    }

    /**
     * Initiating Login Node Elements
     */
    private void initLoginElements() {
        loginImageView.setCache(true);
        loginImageView.setCacheHint(CacheHint.SCALE);
        loginImageView.setCacheHint(CacheHint.SPEED);
        loginImageView.setScaleX(0.5);
        loginImageView.setScaleY(0.5);

        logInButton.setCache(true);
        logInButton.setCacheHint(CacheHint.SPEED);
        cancelButton.setCache(true);
        cancelButton.setCacheHint(CacheHint.SPEED);
    }
    public void hideElements() {
        loginImageView.toBack();
        loginImageView.setLayoutY(150);
        username.setOpacity(0);
        password.setOpacity(0);
        logInButton.setOpacity(0);
        cancelButton.setOpacity(0);
    }

    /**
     * NEW OPENING SEQUENCE
     * */
    public void startLoginAnimation() {
        // Scale back to regular size
        ScaleTransition iconScale = getScale(loginImageView, 1000, 0, 1.0, 1.0);

        // Move icon up, bring the node back to front, then move down to overlap on the menu
        TranslateTransition iconTranslateUp = getTranslate(loginImageView, 600, 400, 0, 0, loginImageView.getLayoutY(), -225);
        TranslateTransition iconTranslateToFront = getTranslate(loginImageView, 0, 100);
        TranslateTransition iconTranslateDown = getTranslate(loginImageView, 400, 0, 0, 0, -225, -150);

        // Scale and move up simultaneously
        ParallelTransition iconScaleAndTranslateUp = new ParallelTransition();
        iconScaleAndTranslateUp.getChildren().addAll(
                iconScale,
                iconTranslateUp
        );

        // Move down and show input fields simultaneously
        ParallelTransition iconTranslateDownAndShowFields = new ParallelTransition();
        iconTranslateDownAndShowFields.getChildren().addAll(
                iconTranslateDown,
                showInputFields()
        );

        // Move down sequentially
        SequentialTransition iconSequence = new SequentialTransition(
                iconScaleAndTranslateUp,
                iconTranslateToFront,
                iconTranslateDownAndShowFields
        );
        iconSequence.setInterpolator(Interpolator.EASE_OUT);

        // Start animation sequence
        iconSequence.play();
    }

    private ParallelTransition showInputFields() {
        ParallelTransition inputFieldSequence = new ParallelTransition();

        List<Node> inputFieldList = new ArrayList<>();
        inputFieldList.add(username);
        inputFieldList.add(password);
        inputFieldList.add(logInButton);
        inputFieldList.add(cancelButton);
        inputFieldList.forEach(node -> inputFieldSequence.getChildren().add(getFade(node, 250, 600, 1.0)));

        return inputFieldSequence;
    }
    private ScaleTransition getScale(Node node, double duration, double delay, double scaleToX, double scaleToY) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration), node);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.setFromX(node.getScaleX());
        scaleTransition.setToX(scaleToX);
        scaleTransition.setFromY(node.getScaleY());
        scaleTransition.setToY(scaleToY);
        scaleTransition.setDelay(Duration.millis(delay));

        return scaleTransition;
    }
    private TranslateTransition getTranslate(Node node, double duration, double delay, double translateFromX, double translateToX, double translateFromY, double translateToY) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setDelay(Duration.millis(delay));
        translateTransition.setInterpolator(Interpolator.EASE_OUT);
        translateTransition.setFromX(translateFromX);
        translateTransition.setToX(translateToX);
        translateTransition.setFromY(translateFromY);
        translateTransition.setToY(translateToY);

        return  translateTransition;
    }
    private TranslateTransition getTranslate(Node node, double duration, double delay) {
        TranslateTransition translateToFront = new TranslateTransition(Duration.millis(duration), node);
        translateToFront.setDelay(Duration.millis(delay));
        translateToFront.setOnFinished(e -> node.toFront());
        return translateToFront;
    }

    private void startClosingAnimation() {
        // Username and Password
        FadeTransition usernameFade = getFade(username, 200, 500, 0);
        FadeTransition passwordFade = getFade(password, 200, 500, 0);

        ParallelTransition fieldFade = new ParallelTransition();
        fieldFade.getChildren().addAll(
                usernameFade,
                passwordFade
        );

        // Move Buttons
        TranslateTransition loginMove = getTranslate(logInButton, 400, 0, 0, 85, 0, 0);
        TranslateTransition cancelMove = getTranslate(cancelButton, 400, 0, 0, -85, 0, 0);

        ParallelTransition moveToCenter = new ParallelTransition();
        moveToCenter.setInterpolator(Interpolator.EASE_BOTH);
        moveToCenter.getChildren().addAll(
                loginMove,
                cancelMove
        );

        // Move and Scale Icon
        TranslateTransition iconMoveDown = getTranslate(loginImageView, 400, 0, 0, 0, -150, 50);
        ScaleTransition iconScaleUp = getScale(loginImageView, 400, 0, 1.5, 1.5);

        // Run everything sequentially
        SequentialTransition closingSequence = new SequentialTransition();
        closingSequence.getChildren().addAll(
                fieldFade,
                moveToCenter,
                iconMoveDown,
                iconScaleUp,
                new PauseTransition(Duration.millis(800))
        );
        closingSequence.play();

        logInButton.setText("Loading...");
        closingSequence.setOnFinished(e -> startTransition());
    }

    private void startTransition() {

        FadeTransition loginButtonFade = getFade(logInButton, 0, 300, 0);
        FadeTransition cancelButtonFade = getFade(cancelButton, 0, 300, 0);
        FadeTransition loginFade = getFade(rootPane, 0, 300, 0);

        ParallelTransition parallelClosing = new ParallelTransition();
        parallelClosing.getChildren().addAll(
                loginButtonFade,
                cancelButtonFade
        );
        parallelClosing.play();

        loginStage.toBack();
        appStage.show();
        modeSelector.startOpeningSequence();

        loginFade.play();
        loginFade.setOnFinished(e -> closeLoginStage(500));
    }

    private FadeTransition getFade(Node node, double delayInMillis, double duration, double value) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setToValue(value);
        fadeTransition.setDelay(Duration.millis(delayInMillis));

        return fadeTransition;
    }
}

