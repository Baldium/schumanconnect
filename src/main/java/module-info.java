module appli.schumanconnect {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens appli.schumanconnect to javafx.fxml;
    exports appli.schumanconnect;
    exports appli.schumanconnect.utils;
    opens appli.schumanconnect.utils to javafx.fxml;
}