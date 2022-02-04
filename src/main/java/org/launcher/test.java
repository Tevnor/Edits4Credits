package org.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



public class test extends Application {
    private TextField inputArea = new TextField();
    private TextArea outputArea = new TextArea();

    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {

        Label headerLbl = new Label("Please insert	Message in TextArea!");
        Label inputLbl = new Label("Input: ");
        Label outputLbl = new Label("Output: ");
        Button okBtn = new Button("OK");
        HBox output = new HBox();
        output.getChildren().addAll(outputLbl, outputArea);
        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> outputArea.appendText("You: " + inputArea.getText() + "\n"));
        BorderPane root = new BorderPane();
        root.setTop(headerLbl);
        root.setRight(okBtn);
        root.setBottom(output);
        root.setLeft(inputLbl);
        root.setCenter(inputArea);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("SE2 Nachdenkzettel GUI");
        stage.show();
    }

}
