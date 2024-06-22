module io.github.isaatonimov.midiKeys {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires com.github.kwhat.jnativehook;
    requires animatefx;


    opens io.github.isaatonimov.midiKeys to javafx.fxml;
    exports io.github.isaatonimov.midiKeys;
}
