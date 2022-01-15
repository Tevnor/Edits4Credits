package org.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GuiDriver  extends Application {

  private Window window;
  private ScreensController sc;
  private int screenWidth;
  private int screenHeight;

  private static Stage primaryStage;
  private static Scene loginScene;

  public static String modeScreenID = "modeSelection";
  public static String modeScreenFile = "/fxml/modeSelection.fxml";

  public static String settingsScreenID = "settings";
  public static String settingsScreenFile = "/fxml/settings.fxml";

  public static String editorScreenID = "editor";
  public static String editorScreenFile = "/fxml/editor.fxml";

  @Override
  public void start(Stage primaryStage) {
    sc = new ScreensController();

    sc.getMonitorInfo();
    screenWidth = sc.getWindow().getScreenWidth();
    screenHeight = sc.getWindow().getScreenHeight();

    window = new Window(screenWidth, screenHeight);

    openLogin();
  }

  public void openLogin() {
    try {
      FXMLLoader loginLoader = new FXMLLoader();
      loginLoader.setLocation(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
      Parent loginRoot = loginLoader.load();

      LogInController logInController = loginLoader.getController();
      logInController.setScreenParent(sc);
      logInController.setWindow(window);
      logInController.setCenter();

      Stage loginStage = new Stage();

      loginStage.initStyle(StageStyle.DECORATED);
      loginStage.initStyle(StageStyle.UNDECORATED);
      loginStage.initStyle(StageStyle.TRANSPARENT);


      Scene loginScene = new Scene(loginRoot, screenWidth, screenHeight, Color.TRANSPARENT);
      loginStage.setMaximized(true);
      loginStage.centerOnScreen();
      loginStage.setScene(loginScene);
      loginStage.show();

    } catch (Exception e) {
      e.printStackTrace();
      e.getCause();
    }
  }
}