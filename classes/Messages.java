package classes;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Messages {

    public static void errorMessage(String text) {
        Pane root = new Pane();
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        Label label = new Label();
        label.setText(text);
        label.setFont(Font.font(14));
        label.setStyle("-fx-padding: 0 8px 0 0");
        label.setLayoutY(25);
        label.setLayoutX(60);
        root.getChildren().add(label);

        Button btn = new Button();
        btn.setPrefSize(48, 48);
        btn.setLayoutX(10);
        btn.setLayoutY(10);
        btn.setStyle("-fx-background-image: url(/images/no.png);"+
                     "-fx-background-color: none;");
        btn.setDefaultButton(true);
        root.getChildren().add(btn);
        btn.setOnAction((ActionEvent event) -> {
            stage.close();
        });

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setHeight(92);
        stage.setTitle("Грешка");
        stage.show();
    }

    public static void successMessage(String text) {
        Pane root = new Pane();
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        Label label = new Label();
        label.setText(text);
        label.setFont(Font.font(14));
        label.setStyle("-fx-padding: 0 8px 0 0");
        label.setLayoutY(25);
        label.setLayoutX(60);
        root.getChildren().add(label);
        
        Button btn = new Button();
        btn.setPrefSize(48, 48);
        btn.setLayoutX(10);
        btn.setLayoutY(10);
        btn.setStyle("-fx-background-image: url(/images/yes.png);"+
                     "-fx-background-color: none;");
        btn.setDefaultButton(true);
        root.getChildren().add(btn);
        btn.setOnAction((ActionEvent event) -> {
            stage.close();
        });

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setHeight(92);
        stage.setTitle("Успешно");
        stage.show();
    }
    
    public static void warningMessage(String text) {
        Pane root = new Pane();
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        Label label = new Label();
        label.setText(text);
        label.setFont(Font.font(14));
        label.setStyle("-fx-padding: 0 8px 0 0");
        label.setLayoutY(25);
        label.setLayoutX(60);
        root.getChildren().add(label);

        Button btn = new Button();
        btn.setPrefSize(48, 48);
        btn.setLayoutX(10);
        btn.setLayoutY(10);
        btn.setStyle("-fx-background-image: url(/images/warning.png);"+
                     "-fx-background-color: none;");
        btn.setDefaultButton(true);
        root.getChildren().add(btn);
        btn.setOnAction((ActionEvent event) -> {
            stage.close();
        });

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setHeight(92);
        stage.setTitle("Внимание");
        stage.show();
    }
}
