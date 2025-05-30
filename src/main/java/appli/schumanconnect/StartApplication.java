package appli.schumanconnect;

import appli.schumanconnect.utils.ScenePage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 899, 518);
        stage.setTitle("SchumanConnect");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


