package basic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GraphicImageEditor extends Application{



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Test");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,500,500);
        stage.setScene(scene);
        stage.show();
    }





}
