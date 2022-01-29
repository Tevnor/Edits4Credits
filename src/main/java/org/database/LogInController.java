package org.database;


import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.launcher.ModeSelectionController;
import org.screencontrol.ControlScreen;
import org.screencontrol.ScreenName;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import java.net.URL;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogInController implements Initializable, ControlScreen {

    private ScreensController screensController;
    private Stage appStage;
    private User user;
    private Window window;
    private FXMLLoader loader;

    public LogInController(){
    }

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
        hideElements();
        startOpeningAnimation();
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        this.screensController = screenPage;
    }
    public void setWindow(Window window) {
        this.window = window;
    }
    public void setCenter() {
//        double xCenter = window.getScreenWidth() / (2 * window.getScaleX());
//        double yCenter = window.getScreenHeight() / (2 * window.getScaleY());
        double xCenter = window.getScreenWidth() / 2;
        double yCenter = window.getScreenHeight() / 2;

        rootPane.setLayoutX(xCenter - 200);
        rootPane.setLayoutY(yCenter - 300);
    }

    @FXML
    private void enterMainApp(ActionEvent event) {
        user = new User(username.getText());
        if (username!=null && password != null) {
            startClosingSequence();
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

    public void startClosingSequence() {
        ExecutorService closeLoginExecutor = Executors.newFixedThreadPool(1);
        Runnable r1 = this::startClosingAnimation;

        closeLoginExecutor.execute(r1);
        closeLoginExecutor.shutdown();
        while(!closeLoginExecutor.isTerminated()) {}

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished( event -> enterApp.start());
        delay.play();
    }

    Thread enterApp = new Thread(new Runnable() {
        @Override
        public void run() {
            Platform.runLater(() -> initAppStages());
            Platform.runLater(() -> createApp());
        }
    });

    public void initAppStages(){
        screensController.loadScreen(ScreenName.MODE_SELECTION);
        loader = screensController.getLoader();
        screensController.loadScreen(ScreenName.PROJECT_SETTINGS);
        screensController.loadScreen(ScreenName.EDITOR);
        screensController.loadScreen(ScreenName.GALLERY);
        screensController.setScreen(ScreenName.MODE_SELECTION);

        ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/e4c-bg.png")), window.getScreenWidth(), window.getScreenHeight(), false, true));
        Group root = new Group();
        root.getChildren().addAll(iv, screensController);
        Scene scene = new Scene(root, Color.TRANSPARENT);


        appStage = new Stage();
        appStage.initStyle(StageStyle.TRANSPARENT);
        appStage.setMaximized(true);
        appStage.setScene(scene);
    }

    public void createApp() {
        try {
            // Pass logged in user to menu class
            ModeSelectionController modeSelector = loader.getController();
            modeSelector.initUserData(user);
            modeSelector.setWindow(window);
            modeSelector.setRootAnchorPane();
            modeSelector.hideElements();

            PauseTransition delay = new PauseTransition(Duration.seconds(1.3));
            delay.setOnFinished( event -> startTransition(modeSelector));
            delay.play();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void cancelLogin(ActionEvent event) {
        startFade(rootPane, 0, 0);
        closeLoginStage(0.75);
    }

    public void closeLoginStage(double seconds){
        Stage stage = (Stage) logInButton.getScene().getWindow();
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished( event -> stage.close() );
        delay.play();
    }

    /*
     * Animations
     * */
    public void hideElements() {
        loginImageView.toBack();
        loginImageView.setLayoutY(80);
        username.setOpacity(0);
        password.setOpacity(0);
        logInButton.setOpacity(0);
        cancelButton.setOpacity(0);
    }
    public void showElements() {
        startFade(username, 0.3, 0.7);
        startFade(password, 0.3, 0.7);
        startFade(logInButton, 0.3, 0.7);
        startFade(cancelButton, 0.3, 0.7);
    }

    public void startOpeningAnimation() {
        PauseTransition delay1 = new PauseTransition(Duration.seconds(1));
        delay1.setOnFinished(event -> moveNodeY(loginImageView, -130, new Duration(170)));
        delay1.play();

        PauseTransition delay2 = new PauseTransition(Duration.seconds(1.35));
        delay2.setOnFinished(event -> moveNodeY(loginImageView, -75, new Duration(150)));
        delay2.play();

        PauseTransition delay3 = new PauseTransition(Duration.seconds(1.47));
        delay3.setOnFinished(event -> showElements());
        delay3.play();

        PauseTransition delay4 = new PauseTransition(Duration.seconds(1.5));
        delay4.setOnFinished(event -> loginImageView.toFront());
        delay4.play();

        PauseTransition delay5 = new PauseTransition(Duration.seconds(0.2));
        delay5.setOnFinished(event -> scaleNode(loginImageView, 0.15));
        delay5.play();

    }
    public void startClosingAnimation() {
        startFade(username, 1, 0);
        startFade(password, 1, 0);
        moveNodeX(logInButton, 85, new Duration(200));
        moveNodeX(cancelButton, -85, new Duration(200));
        changeButtonText(cancelButton, "Success");
        changeButtonText(logInButton, "Success");
        moveNodeY(loginImageView, 125, new Duration(600));
        scaleNode(loginImageView, .33);
        startFade(logInButton, 0, 0);

        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> changeButtonText(cancelButton, "Loading..."));
        delay.play();
    }

    public void startTransition(ModeSelectionController modeSelector) {
        appStage.show();
        modeSelector.startAnimations();
        closeLoginStage(0.1);
    }

    public void startFade(Node node, double time, double value) {
        Duration fadeDuration = new Duration(750);
        Duration fadeDelay = new Duration(time);

        FadeTransition fade = new FadeTransition();
        fade.setToValue(value);
        fade.setDelay(fadeDelay);
        fade.setDuration(fadeDuration);
        fade.setNode(node);
        fade.play();
    }

    public void moveNodeX(Node node, double direction, Duration delay) {
        KeyValue keyValue = new KeyValue(node.translateXProperty(), direction);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValue);

        moveNode(keyFrame, delay);
    }
    public void moveNodeY(Node node, double direction, Duration delay) {
        KeyValue keyValue = new KeyValue(node.translateYProperty(), direction);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValue);

        moveNode(keyFrame, delay);
    }

    public void moveNode(KeyFrame keyFrame, Duration delay) {
        Timeline timeline = new Timeline(keyFrame);
        timeline.setDelay(delay);
        timeline.play();
    }

    public void changeButtonText(Button btn, String text) {
        PauseTransition delay = new PauseTransition(Duration.seconds(.4));
        delay.setOnFinished( event -> btn.setText(text));
        delay.play();
    }

    public void scaleNode(Node node, double factor) {
        Duration durationScale = new Duration(900);
        ScaleTransition scale = new ScaleTransition(Duration.seconds(.5), node);

        scale.setByX(factor);
        scale.setByY(factor);
        scale.setDelay(durationScale);
        scale.play();
    }
}

