module uifx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    exports uifx;
    opens uifx to javafx.fxml;

    //exports uifx to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
}