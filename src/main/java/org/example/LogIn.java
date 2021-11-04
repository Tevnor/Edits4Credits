package org.example;


import basic.login.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import java.io.IOException;

public class LogIn {
    public LogIn(){

    }

    @FXML
    private Button logInButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label loginError;

    public void userLogIn(ActionEvent event) throws IOException {
//        checkLogin();
        validateLogin();
    }
    private void checkLogin() throws IOException {
        GuiDriver m = new GuiDriver();
        if(username.getText().toString().equals("jr125") && password.getText().toString().equals("12345678")) {
            loginError.setText("Success!");

            m.changeScene("/fxml/afterLogin.fxml");
        }

        else if(username.getText().isEmpty() && password.getText().isEmpty()) {
            loginError.setText("Please enter your data.");
        }


        else {
            loginError.setText("Wrong username or password!");
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
                    Stage stage = (Stage) logInButton.getScene().getWindow();
                    GuiDriver guiDriver = new GuiDriver();
                    guiDriver.changeScene("/fxml/afterLogin.fxml");
//                    stage.close();

                } else {
                    loginError.setText("Invalid login. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}

