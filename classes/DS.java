package classes;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DS {
    
    private final  SimpleIntegerProperty id;
    private final  SimpleStringProperty articleName;
    private final  SimpleDoubleProperty articleValue;
    private final  SimpleStringProperty articleUnit;
    private final  SimpleDoubleProperty articlePrice;
    private final  SimpleStringProperty articleCurrency;
    private final  SimpleDoubleProperty totalPrice;
 
    public DS(int aId, String aName, double  aValue, String aUnit, double aPrise, String aCurrency, double aTotal) {
        this.id = new SimpleIntegerProperty(aId);
        this.articleName = new SimpleStringProperty(aName);
        this.articleValue = new SimpleDoubleProperty(aValue);
        this.articleUnit = new SimpleStringProperty(aUnit);
        this.articlePrice = new SimpleDoubleProperty(aPrise);
        this.articleCurrency = new SimpleStringProperty(aCurrency);
        this.totalPrice = new SimpleDoubleProperty(aTotal);
    }
    public int getID() {
        return id.get();
    }
    public void setID(int aId) {
        id.set(aId);
    }
 
    public String getArticleName() {
        return articleName.get();
    }
    public void setArticleName(String aName) {
        articleName.set(aName);
    }
        
    public double getArticleValue() {
        return articleValue.get();
    }
    public void setArticleValue(double aValue) {
        articleValue.set(aValue);
    }
    
    public String getArticleUnit() {
        return articleUnit.get();
    }
    public void setArticleUnit(String aUnit) {
        articleUnit.set(aUnit);
    }
    
    public double getArticlePrice() {
        return articlePrice.get();
    }
    public void setArticlePrice(double aPrise) {
        articlePrice.set(aPrise);
    }
    
    public String getArticleCurrency() {
        return articleCurrency.get();
    }
    public void setArticleCurrency(String aCurrency) {
        articleCurrency.set(aCurrency);
    }
    
    public double getTotalPrice() {
        return totalPrice.get();
    }
    public void setTotalPrice(double aTotal) {
        totalPrice.set(aTotal);
    } 
}
