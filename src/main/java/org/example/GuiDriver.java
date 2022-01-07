package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
      Scene loginScene = new Scene(root, 1280, 720, Color.TRANSPARENT);
      primaryStage.centerOnScreen();
      primaryStage.setScene(loginScene);
      primaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
      e.getCause();
    }
  }
}