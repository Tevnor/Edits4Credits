package org.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.IOException;

public class LogInController {
    public User user;

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



    public void userLogIn(ActionEvent event) throws IOException {
        //validateLogin();
        replacementLogin();
    }

    public void replacementLogin(){
        user = new User(username.getText());
        if (username!=null && password != null){
            enterApp();
        }
        else {
            loginError.setText("Invalid login. Please try again.");
        }
    }

    public void validateLogin(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = '" + username.getText() + "'AND password ='" + password.getText() + "'";


        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if (queryResult.getInt(1) == 1) {
                    // Create user instance with logged in user

                    user = new User(username.getText());
                    enterApp();
                } else {
                    loginError.setText("Invalid login. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void cancelLogin(ActionEvent event) {
        Stage stage = (Stage) logInButton.getScene().getWindow();
        stage.close();
    }

    public void enterApp() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/modeSelection.fxml"));
            rootPane.getChildren().setAll(pane);


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}

