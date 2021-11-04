package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.example.GuiHelper.Dialog;
import org.example.GuiHelper.NumberField;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class GuiDriver extends Application {

  private static Stage stg;


  @Override
  public void start(final Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
    stg = primaryStage;

    primaryStage.setResizable(false);
    primaryStage.setTitle("edits4credits");
    primaryStage.setScene(new Scene(root,1280,720));
    primaryStage.show();
  }
  public void changeScene(String fxml) throws IOException {
    Parent pane = FXMLLoader.load(getClass().getResource(fxml));
    stg.getScene().setRoot(pane);
  }
}