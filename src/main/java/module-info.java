module gui {
    requires java.sql;
    requires mysql.connector.java;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    requires org.apache.logging.log4j;
    requires java.desktop;

    opens org.example to javafx.fxml;
    exports org.example;
    exports basic;
    opens basic to javafx.fxml;
    exports basic.editor;
    opens basic.editor to javafx.fxml;
}