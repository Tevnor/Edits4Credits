package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Menu {

    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button editorButton;
    @FXML
    private Button marketplaceButton;

    // Open editor in same window by replacing anchor pane contents
    public void openEditor(ActionEvent ae) {
        try {
            AnchorPane editorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/editor.fxml")));
            rootAnchorPane.getChildren().setAll(editorPane);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    public void openMarketplace(ActionEvent ae) {

    }

    public void initUserData(User user) {
        usernameLabel.setText(user.getUsername());
    }
}
