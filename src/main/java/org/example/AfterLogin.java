package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AfterLogin {

    @FXML
    private Button logout;


    public void userLogOut(ActionEvent event) throws IOException {
        GuiDriver m = new GuiDriver();
        m.changeScene("/fxml/login.fxml");

    }
}
