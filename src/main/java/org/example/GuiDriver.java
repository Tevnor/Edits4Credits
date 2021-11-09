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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class GuiDriver extends Application {

  @FXML
  private AnchorPane rootPane;
  @FXML
  private AnchorPane loginAnchor;

  private static Stage stg;


  @Override
  public void start(final Stage primaryStage) {

    stg = primaryStage;
    openLogin();

  }

  public void openLogin() {
    try {
      Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/loginMini.fxml")));
      Stage loginStage = new Stage();

      // For mini login window
      loginStage.initStyle(StageStyle.DECORATED);

      loginStage.initStyle(StageStyle.UNDECORATED);
      loginStage.initStyle(StageStyle.TRANSPARENT);
      Scene loginScene = new Scene(loginRoot, 400, 525, Color.TRANSPARENT);
      loginStage.centerOnScreen();
      loginStage.setScene(loginScene);
      loginStage.show();

    } catch (Exception e) {
      e.printStackTrace();
      e.getCause();
    }
  }


//  public void changeScene(String fxml) throws IOException {
//    Parent pane = FXMLLoader.load(getClass().getResource(fxml));
//    stg.getScene().setRoot(pane);
//  }
}