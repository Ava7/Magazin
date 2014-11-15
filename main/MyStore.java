package main;

import classes.BackupDatabase;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MyStore extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/screens/MainScreen.fxml"));
        Scene scene = new Scene(root);

        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setTitle("Магазин");
        stage.getIcons().add(new Image("/images/shop.png"));
        stage.setOnShown((WindowEvent event) -> {
            try {
                BackupDatabase.directoryExists();
            } catch (IOException ex) {
                Logger.getLogger(MyStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        stage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
