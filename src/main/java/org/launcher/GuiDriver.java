package org.launcher;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.database.LogInController;
import org.screencontrol.ScreensController;
import org.screencontrol.Window;


public class GuiDriver  extends Application {
  private static final Logger GD_LOGGER = LogManager.getLogger(GuiDriver.class);
  private static Image icon = null;
  private Window window;
  private ScreensController screensController;
  private double screenWidth;
  private double screenHeight;


  @Override
  public void start(Stage primaryStage) {

    Thread thread = new Thread(() -> {
      screensController = new ScreensController();
      screensController.getMonitorInfo();
      this.window = screensController.getWindow();
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
            logInController.setScreenParent(screensController);
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
            try {
              icon = new Image(getClass().getResource("/images/icon/icon_128x128.png").toString());
              loginStage.getIcons().add(icon);
            } catch (NullPointerException e){
              GD_LOGGER.error("icon could not be loaded");
            }
            loginStage.show();


            logInController.startLoginAnimation();
          } catch (Exception e) {
            GD_LOGGER.fatal("Unknown error application could not be started");
          }
        });
        return null;
      }
    };
    GD_LOGGER.info("Main app started");
    new Thread(openLoginTask).start();
  }
  public static Image getIcon(){
    if(icon != null){
      return new Image(icon.getUrl());
    }
    return null;
  }
}
