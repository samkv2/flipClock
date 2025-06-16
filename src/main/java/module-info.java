module com.example.flipclock.flipclock {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    //requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.flipclock.flipclock to javafx.fxml;
    exports com.example.flipclock.flipclock;
}