package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AfterLogin {

    @FXML
    private Button logout;


    public void userLogOut(ActionEvent event)  {
        GuiDriver guiDriver = new GuiDriver();
        guiDriver.openLogin();
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
    }
}
