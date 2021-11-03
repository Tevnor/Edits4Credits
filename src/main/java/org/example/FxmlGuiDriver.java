package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Driver class for a simple JavaFX demonstration.
 *
 */
public class FxmlGuiDriver extends Application {


	private static final Logger log = LogManager.getLogger(FxmlGuiDriver.class);

    /**
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        log.info("Starting Hello JavaFX and Maven demonstration application");

        final String fxmlFile = "/fxml/login.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        final FXMLLoader loader = new FXMLLoader();
        final Parent rootNode = (Parent) loader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));

        log.debug("Showing JFX scene");
        final Scene scene = new Scene(rootNode, 1280, 720);
        //scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Hello JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }
}
