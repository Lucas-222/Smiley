module com.example.smiley {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.smiley to javafx.fxml;
    exports com.example.smiley;
}