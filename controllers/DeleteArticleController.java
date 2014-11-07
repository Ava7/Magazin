package controllers;

import classes.DBConnection;
import classes.Messages;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class DeleteArticleController implements Initializable {

    @FXML
    private TextField articleName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void delete(ActionEvent event) {
        String aName = articleName.getText();
        if (aName.isEmpty()) {
            Messages.errorMessage("Попълнете име на артикула");
        } else {

            try {
                String sqlSelect = "SELECT articlename FROM article WHERE articlename = '" + aName + "'";
                ResultSet rs = DBConnection.connect().executeQuery(sqlSelect);
                if (rs.next()) {
                    rs.close();
                    try {
                        String sqlDelete = "DELETE FROM article WHERE articlename = '" + aName + "'";
                        DBConnection.connect().execute(sqlDelete);
                        
                        Messages.warningMessage("Обновете данните в главния списък с артикули");
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    Messages.errorMessage("Артикул с име " + aName + " не съществува");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

}
