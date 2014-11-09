package classes;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Edit {
    
    public void editName() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/screens/EditNameScreen.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setTitle("Редактиране име на артикул");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.show();
    }
    
    public void editValue() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/screens/EditValueScreen.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setTitle("Редактиране количество на артикул");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.show();
    }
    
    public void editPrice() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/screens/EditPriceScreen.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setTitle("Редактиране цена на артикул");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.show();
    }
    
    public void deleteArticle() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/screens/DeleteArticleScreen.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setTitle("Изтриване на артикул");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.show();
    }
    
}
