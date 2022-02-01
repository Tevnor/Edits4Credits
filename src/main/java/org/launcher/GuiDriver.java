package org.launcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.database.LogInController;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;

import java.util.Objects;


public class GuiDriver  extends Application {

  private Window window;
  private ScreensController sc;
  private double screenWidth;
  private double screenHeight;

  @Override
  public void start(Stage primaryStage) {
    sc = new ScreensController();
    sc.getMonitorInfo();
    this.window = sc.getWindow();
    screenWidth = window.getScreenWidth();
    screenHeight = window.getScreenHeight();

    openLogin();
  }

  private void openLogin() {
    try {
      FXMLLoader loginLoader = new FXMLLoader();
      loginLoader.setLocation(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
      Parent loginRoot = loginLoader.load();

      LogInController logInController = loginLoader.getController();
      logInController.setScreenParent(sc);
      logInController.setWindow(window);
      logInController.setCenter();
      logInController.hideElements();
      logInController.startOpeningAnimation();

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
