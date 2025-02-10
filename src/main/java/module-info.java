module appli.schumanconnect {
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires java.desktop;
    requires jbcrypt;
    requires org.json;
    requires itextpdf;

    exports appli.schumanconnect.controller;  // Export du package controller
    exports appli.schumanconnect.controller.SecretaryController;
    exports appli.schumanconnect.controller.TeacherController;
    opens appli.schumanconnect.controller.SecretaryController to javafx.fxml;
    opens appli.schumanconnect.controller to javafx.fxml;
    opens appli.schumanconnect.controller.TeacherController to javafx.fxml;

    // Ouvrir le package appli.schumanconnect.model à javafx.base
    opens appli.schumanconnect.model to javafx.base;

    opens appli.schumanconnect to javafx.fxml;
    exports appli.schumanconnect;
    exports appli.schumanconnect.utils;
    opens appli.schumanconnect.utils to javafx.fxml;
    exports appli.schumanconnect.controller.AdminController;
    opens appli.schumanconnect.controller.AdminController to javafx.fxml;
}