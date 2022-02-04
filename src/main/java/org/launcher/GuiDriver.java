package org.launcher;

import javafx.application.Platform;
import javafx.concurrent.Task;
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


public class GuiDriver  extends Application {
  private Window window;
  private ScreensController sc;
  private double screenWidth;
  private double screenHeight;


  @Override
  public void start(Stage primaryStage) {

    Thread thread = new Thread(() -> {
      sc = new ScreensController();
      sc.getMonitorInfo();
      this.window = sc.getWindow();
      screenWidth = window.getScreenWidth();
      screenHeight = window.getScreenHeight();

    });
    thread.setDaemon(true);
    thread.start();

    openLogin();
  }

  private void openLogin() {

    Task<Void> openLoginTask = new Task<>() {

      @Override
      protected Void call() {
        Platform.runLater(() -> {
          try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginRoot = loginLoader.load();

            LogInController logInController = loginLoader.getController();
            logInController.setScreenParent(sc);
            logInController.setWindow(window);
            logInController.initLogin();

            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.DECORATED);
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.initStyle(StageStyle.TRANSPARENT);

            Scene loginScene = new Scene(loginRoot, screenWidth, screenHeight, Color.TRANSPARENT);
            loginStage.setMaximized(true);
            loginStage.centerOnScreen();
            loginStage.setScene(loginScene);
            loginStage.show();


            logInController.startLoginAnimation();
          } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
          }
        });
        return null;
      }
    };
    new Thread(openLoginTask).start();
  }
}
