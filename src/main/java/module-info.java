module com.example.spacerunner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires javafx.media;

    opens com.example.spacerunner to javafx.fxml;
    exports com.example.spacerunner;
}