package com.example.stockviewer;

public class StockModel {
    private String companyName;
    private double price, priceChange, priceChangePercent;
    private boolean priceIncrease;

    public StockModel(String companyName, double price, double priceChange, double priceChangePercent, boolean priceIncrease) {
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.priceIncrease = priceIncrease;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isPriceIncrease() {
        return priceIncrease;
    }

    public void setPriceIncrease(boolean priceIncrease) {
        this.priceIncrease = priceIncrease;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(double priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }
}
