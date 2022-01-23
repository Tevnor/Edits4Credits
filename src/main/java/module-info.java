module gui {
    requires java.sql;
    requires mysql.connector.java;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    requires org.apache.logging.log4j;
    requires java.desktop;

    opens org to javafx.fxml;
    exports org.editor.tools.drawingtool.graphiccontrol.attributes;
    exports org.editor.tools.drawingtool.graphiccontrol.handlers;
    exports org.editor.tools.drawingtool;
    exports org.screencontrol;
    opens org.screencontrol to javafx.fxml;
    exports org.launcher;
    opens org.launcher to javafx.fxml;
    exports org.database;
    opens org.database to javafx.fxml;
    exports org.editor;
    opens org.editor to javafx.fxml;
    opens org.editor.tools.drawingtool to javafx.fxml;
    exports org.editor.tools.filtertool;
    opens org.editor.tools.filtertool to javafx.fxml;
    exports org.editor.tools.imagetool;
    opens org.editor.tools.imagetool to javafx.fxml;
    exports org.editor.project;
    opens org.editor.project to javafx.fxml;
}