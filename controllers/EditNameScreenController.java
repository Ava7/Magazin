package controllers;

import classes.DBConnection;
import classes.Messages;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class EditNameScreenController implements Initializable {

    @FXML
    private TextField oldName;
    @FXML
    private TextField newName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void edit(ActionEvent event) {
        String on = oldName.getText();
        String nn = newName.getText();
        if ((on.isEmpty())) {
            Messages.errorMessage("Попълнете старото име на артикула");
        } else if (nn.isEmpty()) {
            Messages.errorMessage("Попълнете ново име на артикула");
        } else {
            try {
                String sql = "SELECT articlename FROM article WHERE articlename = '" + on + "'";
                ResultSet rs = DBConnection.connect().executeQuery(sql);
                if (rs.next()) {
                    rs.close();
                    try {
                        String sqlUpdate = "UPDATE article SET articlename = '" + nn + "' WHERE articlename = '" + on + "' ";
                        DBConnection.connect().execute(sqlUpdate);

                        Messages.warningMessage("Обновете данните в главния списък с артикули");
                        ((Node) (event.getSource())).getScene().getWindow().hide();

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    Messages.errorMessage("Артикул с име " + nn + " не съществува");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
