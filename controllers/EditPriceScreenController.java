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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import static controllers.MainScreenController.isNumeric;

public class EditPriceScreenController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField value;
    @FXML
    private ComboBox<String> dim;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dim.getItems().addAll("лв.", "usd", "eur");
    }

    @FXML
    private void edit(ActionEvent event) {

        String n = name.getText();
        String v = value.getText();
        v = v.replace(",", ".");
        String d = dim.getValue();

        if ((n.isEmpty())) {
            Messages.errorMessage("Попълнете име на артикула");
        } else if (v.isEmpty()) {
            Messages.errorMessage("Попълнете цена на артикула");
        } else if (isNumeric(v) == false) {
            Messages.errorMessage("Попълнете число в полето за цена");
        } else if (dim.getSelectionModel().isEmpty()) {
            Messages.errorMessage("Изберете дименсия към цена");
        } else {
            try {
                String sql = "SELECT articlename, articlevalue FROM article WHERE articlename = '" + n + "'";
                ResultSet rs = DBConnection.connect().executeQuery(sql);
                if (rs.next()) {

                    double vv = Double.parseDouble(String.valueOf(v));
                    double p = rs.getDouble("articlevalue");
                    double t = vv * p;
                    t = (double) Math.round(t * 100.0) / 100.0;
                    rs.close();

                    try {
                        String sqlUpdate = "UPDATE article SET articleprice = " + vv + ", articlecurrency = '" + d + "', totalprice = " + t + " WHERE articlename = '" + n + "' ";
                        DBConnection.connect().execute(sqlUpdate);

                        Messages.warningMessage("Обновете данните в главния списък с артикули");
                        ((Node) (event.getSource())).getScene().getWindow().hide();

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    Messages.errorMessage("Артикул с име " + n + " не съществува");
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
