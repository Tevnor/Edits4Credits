module gui {
    requires java.sql;
    requires mysql.connector.java;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires java.desktop;

    opens org to javafx.fxml;
    exports org.editor.tools.drawingTool.attributes;
    exports org.editor.tools.drawingTool.handlers;
    exports org.editor.tools.drawingTool;
    exports org.screenControl;
    exports org.marketplace.gallery;
    opens org.marketplace.gallery to javafx.fxml;
    opens org.screenControl to javafx.fxml;
    exports org.launcher;
    opens org.launcher to javafx.fxml;
    exports org.database;
    opens org.database to javafx.fxml;
    exports org.editor;
    opens org.editor to javafx.fxml;
    opens org.editor.tools.drawingTool to javafx.fxml;
    exports org.editor.tools.filterTool;
    opens org.editor.tools.filterTool to javafx.fxml;
    exports org.editor.tools.imageTool;
    opens org.editor.tools.imageTool to javafx.fxml;
    exports org.editor.project;
    opens org.editor.project to javafx.fxml;
    exports org.editor.tools.filterTool.filterControl;
    opens org.editor.tools.filterTool.filterControl to javafx.fxml;
    exports org.editor.tools.filterTool.filterControl.filter;
    opens org.editor.tools.filterTool.filterControl.filter to javafx.fxml;
    exports org.editor.tools.filterTool.filterControl.effects;
    opens org.editor.tools.filterTool.filterControl.effects to javafx.fxml;
}