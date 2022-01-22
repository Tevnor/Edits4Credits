module gui {
    requires java.sql;
    requires mysql.connector.java;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    requires org.apache.logging.log4j;
    requires java.desktop;

    opens org.controller to javafx.fxml;
    exports org.controller;
    exports org.controller.tools.drawingtool.graphiccontrol.attributes;
    exports org.controller.tools.drawingtool.graphiccontrol.handlers;
    exports org.controller.tools.drawingtool;
}