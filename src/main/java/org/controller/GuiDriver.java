package org.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class GuiDriver extends Application {

  @Override
  public void start(Stage primaryStage) {
    openLogin();
  }

  public void openLogin() {
    try {
      Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
      Stage primaryStage = new Stage();

      // For mini login window
      primaryStage.initStyle(StageStyle.DECORATED);
      primaryStage.initStyle(StageStyle.UNDECORATED);
      primaryStage.initStyle(StageStyle.TRANSPARENT);
      primaryStage.setMaximized(true);

      GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      int width = monitor.getDisplayMode().getWidth();
      int height = monitor.getDisplayMode().getHeight();
      Scene loginScene = new Scene(root, width, height, Color.TRANSPARENT);
      primaryStage.centerOnScreen();
      primaryStage.setScene(loginScene);
      primaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
      e.getCause();
    }
  }
}