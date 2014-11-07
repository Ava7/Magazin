package controllers;

import classes.DBConnection;
import classes.Messages;
import classes.VP;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PaymentController implements Initializable {

    @FXML
    private TableView<VP> table;
    @FXML
    private TableColumn<VP, Integer> articleId;
    @FXML
    private TableColumn<VP, String> articleName;
    @FXML
    private TableColumn<VP, Double> articleValue;
    @FXML
    private TableColumn<VP, String> articleUnit;
    @FXML
    private TableColumn<VP, Double> articlePrice;
    @FXML
    private TableColumn<VP, String> articleCurrency;
    @FXML
    private TableColumn<VP, Double> totalPrice;
    @FXML
    private TextField total;
    
    private Button paymentButton;
    private final ObservableList<VP> data = FXCollections.observableArrayList();
    private int selectedId = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sumPayment();
        loadTableData();
    }

    @FXML
    private void selectItem(MouseEvent event) {
        VP article = (VP) table.getSelectionModel().getSelectedItem();
        if (article != null) {
            selectedId = article.getID();
        }
    }

    @FXML
    private void deleteSelected(ActionEvent event) {
        if (selectedId == 0) {
            Messages.errorMessage("Изберете поръчка");
        } else {

            try {
                String sqlDelete = "DELETE FROM userpayment WHERE id = " + selectedId + "";
                DBConnection.connect().execute(sqlDelete);
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }

            selectedId = 0;
            data.clear();
            loadTableData();
            sumPayment();
            Messages.warningMessage("Изтриите поръчката и от Справки за дневни продажби");

        }
    }

    @FXML
    private void clearPayment(ActionEvent event) {

        try {
            String sqlDelete = "TRUNCATE TABLE userpayment";
            DBConnection.connect().execute(sqlDelete);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            String sqlReset = "ALTER TABLE userpayment ALTER COLUMN id RESTART WITH 1";
            DBConnection.connect().execute(sqlReset);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        paymentButton.setStyle("-fx-text-fill: #000000;");
        data.clear();
        loadTableData();
        sumPayment();
        Messages.successMessage("Сметката е изчистена");
    }

    public void setRootButton(Button rootBtn) {
        this.paymentButton = rootBtn;
    }

    private void loadTableData() {
        String sqlSelect = "SELECT * FROM userpayment ORDER BY id ASC";
        try (ResultSet rs = DBConnection.connect().executeQuery(sqlSelect)) {
            while (rs.next()) {
                int a = rs.getInt("id");
                String b = rs.getString("articlename");
                double c = rs.getDouble("articlevalue");
                String d = rs.getString("articleunit");
                double e = rs.getDouble("articleprice");
                String f = rs.getString("articlecurrency");
                double g = rs.getDouble("total");
                VP list = new VP(a, b, c, d, e, f, g);
                data.add(list);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        table.setPlaceholder(new Label("Сметката е празна"));

        articleId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        articleName.setCellValueFactory(new PropertyValueFactory<>("articleName"));
        articleValue.setCellValueFactory(new PropertyValueFactory<>("articleValue"));
        articleUnit.setCellValueFactory(new PropertyValueFactory<>("articleUnit"));
        articlePrice.setCellValueFactory(new PropertyValueFactory<>("articlePrice"));
        articleCurrency.setCellValueFactory(new PropertyValueFactory<>("articleCurrency"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        table.setItems(data);
    }

    private void sumPayment() {
        String sql = "SELECT sum(total) as t FROM userpayment";
        try (ResultSet rs = DBConnection.connect().executeQuery(sql)) {
            while (rs.next()) {
                double a = rs.getDouble("t");
                a = (int) Math.round(a * 100) / (double) 100;
                total.setText(a + "лв.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
