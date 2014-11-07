package controllers;

import classes.Article;
import classes.Edit;
import classes.DBConnection;
import classes.Help;
import classes.Messages;
import classes.Reports;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScreenController implements Initializable {

    @FXML
    private TextField totalInStore;
    @FXML
    private TextField totalForDay;
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Article> table;
    @FXML
    private TableColumn<Article, String> articleName;
    @FXML
    private TableColumn<Article, Double> articleValue;
    @FXML
    private TableColumn<Article, String> articleUnit;
    @FXML
    private TableColumn<Article, Double> articlePrice;
    @FXML
    private TableColumn<Article, String> articleCurrency;
    @FXML
    private TableColumn<Article, Double> totalPrice;

    @FXML
    private TextField sellArticleName;
    @FXML
    private TextField sellArticleValue;
    @FXML
    private Button paymentButton;

    @FXML
    private TextField addArticleName;
    @FXML
    private TextField addArticleValue;
    @FXML
    private ComboBox<String> addArticleCurrency;
    @FXML
    private TextField addArticlePrice;
    @FXML
    private ComboBox<String> addArticleUnit;

    @FXML
    private TextField editArticleName;
    @FXML
    private TextField editArticleValue;
    @FXML
    private TextField editArticlePrice;

    private final ObservableList<Article> data = FXCollections.observableArrayList();
    LocalDate date = LocalDate.now();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deleteDailySale();
        deleteDailyDelivery();
        loadTableData();
        sumStoreStock();
        sumDailySale();
        chechPaymentButton();
        addArticleUnit.getItems().addAll("бр.", "кг.", "л.", "м.");
        addArticleCurrency.getItems().addAll("лв.", "usd", "eur");
    }

    @FXML
    private void sellArticle(ActionEvent event) throws IOException {

        String aName = sellArticleName.getText();
        String a = sellArticleValue.getText();
        a = a.replace(",", ".");

        if ((aName.isEmpty())) {
            Messages.errorMessage("Попълнете име на артикула");
        } else if (a.isEmpty()) {
            Messages.errorMessage("Попълнете количество на артикула");
        } else if (isNumeric(a) == false) {
            Messages.errorMessage("Попълнете число в полето за количество");
        } else {

            try {
                String sql = "SELECT articlevalue, articleunit, articleprice, articlecurrency FROM article WHERE articlename = '" + aName + "'";
                ResultSet rs = DBConnection.connect().executeQuery(sql);
                if (rs.next()) {

                    double aValue = Double.parseDouble(String.valueOf(a));
                    double currentValue = rs.getDouble("articlevalue");
                    String aUnit = rs.getString("articleunit");
                    double currentPrice = rs.getDouble("articleprice");
                    String currentCurrency = rs.getString("articlecurrency");

                    double newValue = currentValue - aValue;
                    newValue = (double) Math.round(newValue * 100.0) / 100.0;

                    double aTotal = newValue * currentPrice;
                    aTotal = (double) Math.round(aTotal * 100.0) / 100.0;

                    double total = aValue * currentPrice;
                    total = (double) Math.round(total * 100.0) / 100.0;

                    rs.close();

                    try {
                        String sqlInsert = "INSERT INTO dailysale (dateadded, articlename, articlevalue, articleunit, articleprice, articlecurrency, total)"
                                + " VALUES('" + date + "', '" + aName + "', " + aValue + ", '" + aUnit + "', " + currentPrice + ", '" + currentCurrency + "', " + total + ")";
                        DBConnection.connect().execute(sqlInsert);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        String sqlInsert = "INSERT INTO userpayment (articlename, articlevalue, articleunit, articleprice, articlecurrency, total)"
                                + " VALUES('" + aName + "'," + aValue + ",'" + aUnit + "'," + currentPrice + ",'" + currentCurrency + "', " + total + ")";
                        DBConnection.connect().execute(sqlInsert);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        String sqlUpdate = "UPDATE article SET articlevalue = " + newValue + ", totalprice = " + aTotal + "  WHERE articlename = '" + aName + "'";
                        DBConnection.connect().execute(sqlUpdate);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                    clearInputs();
                    data.clear();
                    loadTableData();
                    chechPaymentButton();
                    sumStoreStock();
                    sumDailySale();
                    Messages.successMessage("Продадохте " + aValue + aUnit + aName);

                } else {
                    Messages.errorMessage("Артикул с име " + aName + " не съществува");
                }
            } catch (NumberFormatException | SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void addArticle(ActionEvent event) {

        String aName = addArticleName.getText();
        String a = addArticleValue.getText();
        a = a.replace(",", ".");
        String b = addArticlePrice.getText();
        b = b.replace(",", ".");

        if ((aName.isEmpty())) {
            Messages.errorMessage("Попълнете име на артикула");
        } else if (a.isEmpty()) {
            Messages.errorMessage("Попълнете количество на артикула");
        } else if (isNumeric(a) == false) {
            Messages.errorMessage("Попълнете число в полето за количество");
        } else if (addArticleUnit.getSelectionModel().isEmpty()) {
            Messages.errorMessage("Изберете дименсия към количество");
        } else if (b.isEmpty()) {
            Messages.errorMessage("Попълнете цена на артикула");
        } else if (isNumeric(b) == false) {
            Messages.errorMessage("Попълнете число в полето за цена");
        } else if (addArticleCurrency.getSelectionModel().isEmpty()) {
            Messages.errorMessage("Изберете дименсия към цена");
        } else {

            double aValue = Double.parseDouble(String.valueOf(a));
            String aUnit = addArticleUnit.getValue();
            double aPrice = Double.parseDouble(String.valueOf(b));
            String aCurrency = addArticleCurrency.getValue();

            double aTotal = aValue * aPrice;
            aTotal = (double) Math.round(aTotal * 100.0) / 100.0;

            try {
                String sql = "SELECT articlename FROM article WHERE articlename = '" + aName + "'";
                ResultSet rs = DBConnection.connect().executeQuery(sql);
                if (rs.next()) {
                    Messages.errorMessage("Артикул с име " + aName + " съществува");
                } else {
                    rs.close();
                    try {
                        String sqlInsert = "INSERT INTO dailydelivery (dateadded, articlename, articlevalue, articleunit, articleprice, articlecurrency, total)"
                                + " VALUES('" + date + "', '" + aName + "', " + aValue + ", '" + aUnit + "', " + aPrice + ", '" + aCurrency + "', " + aTotal + ")";
                        DBConnection.connect().execute(sqlInsert);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        String sqlInsert = "INSERT INTO article(articlename, articlevalue, articleunit, articleprice, articlecurrency, totalprice) "
                                + "VALUES ('" + aName + "'," + aValue + ",'" + aUnit + "'," + aPrice + ",'" + aCurrency + "'," + aTotal + ")";
                        DBConnection.connect().execute(sqlInsert);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                    clearInputs();
                    data.clear();
                    loadTableData();
                    sumStoreStock();
                    sumDailySale();
                    Messages.successMessage("Добавихте нов артикул: " + aName + " " + aValue + aUnit + " на цена " + aPrice + aCurrency);

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void editArticle(ActionEvent event) {

        String aName = editArticleName.getText();
        String a = editArticleValue.getText();
        a = a.replace(",", ".");
        String b = editArticlePrice.getText();
        b = b.replace(",", ".");

        if ((aName.isEmpty())) {
            Messages.errorMessage("Попълнете име на артикула");
        } else if (a.isEmpty()) {
            Messages.errorMessage("Попълнете количество на артикула");
        } else if (isNumeric(a) == false) {
            Messages.errorMessage("Попълнете число в полето за добавено количество");
        } else if (b.isEmpty()) {
            Messages.errorMessage("Попълнете цена на артикула");
        } else if (isNumeric(b) == false) {
            Messages.errorMessage("Попълнете число в полето за нова цена");
        } else {
            try {
                String sql = "SELECT articlename, articlevalue, articleunit, articlecurrency FROM article WHERE articlename = '" + aName + "'";
                ResultSet rs = DBConnection.connect().executeQuery(sql);
                if (rs.next()) {

                    double aValue = Double.parseDouble(String.valueOf(a));
                    String aUnit = rs.getString("articleunit");
                    double aPrice = Double.parseDouble(String.valueOf(b));
                    String aCurrency = rs.getString("articlecurrency");
                    double currentValue = rs.getDouble("articlevalue");

                    double newValue = currentValue + aValue;
                    newValue = (double) Math.round(newValue * 100.0) / 100.0;

                    double aTotal = newValue * aPrice;
                    aTotal = (double) Math.round(aTotal * 100.0) / 100.0;

                    double total = aValue * aPrice;
                    total = (double) Math.round(total * 100.0) / 100.0;

                    rs.close();

                    try {
                        String sqlInsert = "INSERT INTO dailydelivery (dateadded, articlename, articlevalue, articleunit, articleprice, articlecurrency, total)"
                                + " VALUES('" + date + "', '" + aName + "', " + aValue + ", '" + aUnit + "', " + aPrice + ", '" + aCurrency + "', " + total + ")";
                        DBConnection.connect().execute(sqlInsert);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        String sqlUpdate = "UPDATE article SET articlevalue = " + newValue + ", articleprice = " + aPrice + ", totalprice = " + aTotal + " WHERE articlename = '" + aName + "' ";
                        DBConnection.connect().execute(sqlUpdate);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                    clearInputs();
                    data.clear();
                    loadTableData();
                    sumStoreStock();
                    sumDailySale();
                    Messages.successMessage("Добавихте " + aValue + aUnit + aName + " на цена " + aPrice + aCurrency);

                } else {
                    Messages.errorMessage("Артикул с име " + aName + " не съществува");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void showPayment(ActionEvent event) throws IOException {

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/screens/Payment.fxml"));
        Pane childLayout = (Pane) loader.load();
        Scene scene = new Scene(childLayout);
        stage.setScene(scene);

        PaymentController cc = ((PaymentController) loader.getController());
        cc.setRootButton(paymentButton);

        stage.setTitle("Клиентска сметка");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.show();
        stage.show();

    }

    @FXML
    private void editArticleName(ActionEvent event) throws IOException {
        Edit edit = new Edit();
        edit.editName();
    }

    @FXML
    private void editArticleValue(ActionEvent event) throws IOException {
        Edit edit = new Edit();
        edit.editValue();
    }

    @FXML
    private void editArticlePrice(ActionEvent event) throws IOException {
        Edit edit = new Edit();
        edit.editPrice();
    }

    @FXML
    private void deleteArticle(ActionEvent event) throws IOException {
        Edit delete = new Edit();
        delete.deleteArticle();
    }

    @FXML
    private void referenceSales(ActionEvent event) throws IOException {
        Reports report = new Reports();
        report.referenceSales();

    }

    @FXML
    private void referenceSupply(ActionEvent event) throws IOException {
        Reports report = new Reports();
        report.referenceSupply();
    }

    @FXML
    private void help(ActionEvent event) throws IOException {
        Help help = new Help();
        help.helpWindow();
    }

    @FXML
    private void exitProgram(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void searching(KeyEvent event) {
        data.clear();
        String search = searchField.getText();
        try {
            String sqlSelect = "SELECT * FROM article WHERE articlename LIKE '%" + search + "%' ORDER BY articlename ASC";
            ResultSet rs = DBConnection.connect().executeQuery(sqlSelect);
            while (rs.next()) {
                String a = rs.getString("articlename");
                double b = rs.getDouble("articlevalue");
                String c = rs.getString("articleunit");
                double d = rs.getDouble("articleprice");
                String e = rs.getString("articlecurrency");
                double f = rs.getDouble("totalprice");
                Article list = new Article(a, b, c, d, e, f);
                data.add(list);
            }

            table.setPlaceholder(new Label("Няма намерени резултати"));

            articleName.setCellValueFactory(new PropertyValueFactory<>("articleName"));
            articleValue.setCellValueFactory(new PropertyValueFactory<>("articleValue"));
            articleUnit.setCellValueFactory(new PropertyValueFactory<>("articleUnit"));
            articlePrice.setCellValueFactory(new PropertyValueFactory<>("articlePrice"));
            articleCurrency.setCellValueFactory(new PropertyValueFactory<>("articleCurrency"));
            totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            table.setItems(data);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void selectItem(MouseEvent event) {
        Article article = (Article) table.getSelectionModel().getSelectedItem();
        if (article != null) {
            sellArticleName.setText(article.getArticleName());
            editArticleName.setText(article.getArticleName());
        }
    }

    @FXML
    private void refreshButton(ActionEvent event) {
        searchField.clear();
        clearInputs();
        data.clear();
        loadTableData();
        sumDailySale();
        sumStoreStock();
        deleteDailySale();
        deleteDailyDelivery();
    }

    private void loadTableData() {
        try {
            String sqlSelect = "SELECT * FROM article ORDER BY articlename ASC";
            ResultSet rs = DBConnection.connect().executeQuery(sqlSelect);
            while (rs.next()) {
                String a = rs.getString("articlename");
                double b = rs.getDouble("articlevalue");
                String c = rs.getString("articleunit");
                double d = rs.getDouble("articleprice");
                String e = rs.getString("articlecurrency");
                double f = rs.getDouble("totalprice");
                Article list = new Article(a, b, c, d, e, f);
                data.add(list);
            }

            table.setPlaceholder(new Label("Няма артикули в склада"));

            articleName.setCellValueFactory(new PropertyValueFactory<>("articleName"));
            articleValue.setCellValueFactory(new PropertyValueFactory<>("articleValue"));
            articleUnit.setCellValueFactory(new PropertyValueFactory<>("articleUnit"));
            articlePrice.setCellValueFactory(new PropertyValueFactory<>("articlePrice"));
            articleCurrency.setCellValueFactory(new PropertyValueFactory<>("articleCurrency"));
            totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            table.setItems(data);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sumStoreStock() {
        try {
            String sql = "SELECT sum(totalprice) as t FROM article";
            ResultSet rs = DBConnection.connect().executeQuery(sql);
            while (rs.next()) {
                double a = rs.getDouble("t");
                a = (double) Math.round(a * 100.0) / 100.0;
                totalInStore.setText(a + "лв.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sumDailySale() {
        try {
            String sql = "SELECT sum(total) as t FROM dailysale";
            ResultSet rs = DBConnection.connect().executeQuery(sql);
            while (rs.next()) {
                double a = rs.getDouble("t");
                a = (double) Math.round(a * 100.0) / 100.0;
                totalForDay.setText(a + "лв.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteDailySale() {
        try {
            String Select = "SELECT dateadded FROM dailysale WHERE dateadded != '" + date + "'";
            ResultSet rs = DBConnection.connect().executeQuery(Select);
            if (rs.next()) {
                rs.close();
                try {
                    String sqlDelete = "TRUNCATE TABLE dailysale";
                    DBConnection.connect().execute(sqlDelete);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                try {
                    String sqlReset = "ALTER TABLE dailysale ALTER COLUMN id RESTART WITH 1";
                    DBConnection.connect().execute(sqlReset);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteDailyDelivery() {
        try {
            String Select = "SELECT dateadded FROM dailydelivery WHERE dateadded != '" + date + "'";
            ResultSet rs = DBConnection.connect().executeQuery(Select);
            if (rs.next()) {
                rs.close();
                try {
                    String sqlDelete = "TRUNCATE TABLE dailydelivery";
                    DBConnection.connect().execute(sqlDelete);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                try {
                    String sqlReset = "ALTER TABLE dailydelivery ALTER COLUMN id RESTART WITH 1";
                    DBConnection.connect().execute(sqlReset);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void chechPaymentButton() {
        String sql = "SELECT id FROM userpayment";
        try (ResultSet rs = DBConnection.connect().executeQuery(sql)) {
            if (rs.next()) {
                paymentButton.setStyle("-fx-text-fill: #f20000;");
            }

        } catch (Exception e) {
        }
    }

    private void clearInputs() {
        sellArticleName.clear();
        sellArticleValue.clear();
        addArticleName.clear();
        addArticleValue.clear();
        addArticlePrice.clear();
        editArticleName.clear();
        editArticleValue.clear();
        editArticlePrice.clear();
        addArticleUnit.getSelectionModel().clearSelection();
        addArticleCurrency.getSelectionModel().clearSelection();
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
