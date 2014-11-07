package classes;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Article {
    
    private final  SimpleStringProperty articleName;
    private final  SimpleDoubleProperty articleValue;
    private final  SimpleStringProperty articleUnit;
    private final  SimpleDoubleProperty articlePrice;
    private final  SimpleStringProperty articleCurrency;
    private final  SimpleDoubleProperty total;
 
    public Article(String aName, double  aValue, String aUnit, double aPrise, String aCurrency, double aTotal) {
        this.articleName = new SimpleStringProperty(aName);
        this.articleValue = new SimpleDoubleProperty(aValue);
        this.articleUnit = new SimpleStringProperty(aUnit);
        this.articlePrice = new SimpleDoubleProperty(aPrise);
        this.articleCurrency = new SimpleStringProperty(aCurrency);
        this.total = new SimpleDoubleProperty(aTotal);
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
        return total.get();
    }
    public void setTotalPrice(double aTotal) {
        total.set(aTotal);
    } 
}
