package controllers;

import classes.DBConnection;
import classes.DS;
import classes.Messages;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class DailySalesController implements Initializable {

    @FXML
    public TableView<DS> table;
    @FXML
    private TableColumn<DS, Integer> articleId;
    @FXML
    private TableColumn<DS, String> articleName;
    @FXML
    private TableColumn<DS, Double> articleValue;
    @FXML
    private TableColumn<DS, String> articleUnit;
    @FXML
    private TableColumn<DS, Double> articlePrice;
    @FXML
    private TableColumn<DS, String> articleCurrency;
    @FXML
    private TableColumn<DS, Double> totalPrice;
    @FXML
    private TextField total;
    private final ObservableList<DS> data = FXCollections.observableArrayList();
    LocalDate date = LocalDate.now();
    private int selectedId = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTableData();
        sumDailySale();
    }

    @FXML
    private void deleteSelected(ActionEvent event) {
        if (selectedId == 0) {
            Messages.errorMessage("Иберете продажба");
        } else {

            try {
                String select = "SELECT article.articlename as aan, article.articlevalue as aav, totalprice, dailysale.articlevalue as dav, total "
                        + "FROM article "
                        + "JOIN dailysale "
                        + "ON article.articlename=dailysale.articlename "
                        + "WHERE dailysale.id = " + selectedId + "";
                ResultSet rs = DBConnection.connect().executeQuery(select);
                if (rs.next()) {

                    String aName = rs.getString("aan");
                    double aCurrentValue = rs.getDouble("aav");
                    double aCurrentTotalprice = rs.getDouble("totalprice");
                    double dCurrentValue = rs.getDouble("dav");
                    double dCurrentTotalprice = rs.getDouble("total");

                    double aNewValue = dCurrentValue + aCurrentValue;
                    aNewValue = (double) Math.round(aNewValue * 100.0) / 100.0;

                    double aNewTotal = aCurrentTotalprice + dCurrentTotalprice;
                    aNewTotal = (double) Math.round(aNewTotal * 100.0) / 100.0;

                    rs.close();

                    try {
                        String update = "UPDATE article SET articlevalue = " + aNewValue + ", totalprice = " + aNewTotal + " WHERE articlename = '" + aName + "' ";
                        DBConnection.connect().execute(update);
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage());
                    }

                    try {
                        String sqlDelete = "DELETE FROM dailysale WHERE id = " + selectedId + "";
                        DBConnection.connect().execute(sqlDelete);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    selectedId = 0;
                    data.clear();
                    loadTableData();
                    sumDailySale();
                    Messages.warningMessage("Обновете данните в главния списък с артикули");

                }
            } catch (SQLException | NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void loadTableData() {
        try {
            String sqlSelect = "SELECT * FROM dailysale ORDER BY id ASC";
            ResultSet rs = DBConnection.connect().executeQuery(sqlSelect);
            while (rs.next()) {
                int a = rs.getInt("id");
                String b = rs.getString("articlename");
                double c = rs.getDouble("articlevalue");
                String d = rs.getString("articleunit");
                double e = rs.getDouble("articleprice");
                String f = rs.getString("articlecurrency");
                double g = rs.getDouble("total");
                DS list = new DS(a, b, c, d, e, f, g);
                data.add(list);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        table.setPlaceholder(new Label("Няма намерени резултати"));

        articleId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        articleName.setCellValueFactory(new PropertyValueFactory<>("articleName"));
        articleValue.setCellValueFactory(new PropertyValueFactory<>("articleValue"));
        articleUnit.setCellValueFactory(new PropertyValueFactory<>("articleUnit"));
        articlePrice.setCellValueFactory(new PropertyValueFactory<>("articlePrice"));
        articleCurrency.setCellValueFactory(new PropertyValueFactory<>("articleCurrency"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        table.setItems(data);
    }

    @FXML
    private void selectItem(MouseEvent event) {
        DS article = (DS) table.getSelectionModel().getSelectedItem();
        if (article != null) {
            selectedId = article.getID();
        }
    }

    private void sumDailySale() {
        try {
            String sql = "SELECT sum(total) as t FROM dailysale";
            ResultSet rs = DBConnection.connect().executeQuery(sql);
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
