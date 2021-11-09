package org.example;


import basic.login.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import java.io.IOException;
import java.util.ResourceBundle;

public class LogIn {
    public User user;

    public LogIn(){
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



    public void userLogIn(ActionEvent event) throws IOException {
        validateLogin();
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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/fxml/menu.fxml")));
            Parent root = loader.load();

            // Pass logged in user to menu class
            Menu menu = loader.getController();
            menu.initUserData(user);




            Stage appStage = new Stage();
            appStage.setResizable(false);
            appStage.setTitle("E4C");

            appStage.setScene(new Scene(root,1280,720));
            appStage.centerOnScreen();
            appStage.show();
            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}

