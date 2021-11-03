package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        checkLogin();
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
}

