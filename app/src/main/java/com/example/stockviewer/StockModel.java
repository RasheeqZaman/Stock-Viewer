package com.example.stockviewer;

import java.io.Serializable;
import java.util.Date;

public class StockModel implements Serializable {
    private String companyName, companyFullName, typeOfInstrument, cashDividend, stockDividend, rightIssue, yearEnd;
    private double price, priceChange, priceChangePercent, paidUpCapital, faceParValue, openingPrice;
    private Date lastUpdateTime;
    public boolean isBookmarked;

    public StockModel(String companyName, String companyFullName, String typeOfInstrument, String cashDividend, String stockDividend, String rightIssue, String yearEnd, double price, double priceChange, double priceChangePercent, double paidUpCapital, double faceParValue, double openingPrice, Date lastUpdateTime) {
        this.companyName = companyName;
        this.companyFullName = companyFullName;
        this.typeOfInstrument = typeOfInstrument;
        this.cashDividend = cashDividend;
        this.stockDividend = stockDividend;
        this.rightIssue = rightIssue;
        this.yearEnd = yearEnd;
        this.price = price;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.paidUpCapital = paidUpCapital;
        this.faceParValue = faceParValue;
        this.openingPrice = openingPrice;
        this.lastUpdateTime = lastUpdateTime;
        isBookmarked = false;
    }

    public StockModel(String companyName, double price, double priceChange, double priceChangePercent){
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.companyFullName = "";
        this.typeOfInstrument = "";
        this.cashDividend = "";
        this.stockDividend = "";
        this.rightIssue = "";
        this.yearEnd = "";
        this.paidUpCapital = 0.0;
        this.faceParValue = 0.0;
        this.openingPrice = 0.0;
        this.lastUpdateTime = new Date();
        isBookmarked = false;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyFullName() {
        return companyFullName;
    }

    public void setCompanyFullName(String companyFullName) {
        this.companyFullName = companyFullName;
    }

    public String getTypeOfInstrument() {
        return typeOfInstrument;
    }

    public void setTypeOfInstrument(String typeOfInstrument) {
        this.typeOfInstrument = typeOfInstrument;
    }

    public String getCashDividend() {
        return cashDividend;
    }

    public void setCashDividend(String cashDividend) {
        this.cashDividend = cashDividend;
    }

    public String getStockDividend() {
        return stockDividend;
    }

    public void setStockDividend(String stockDividend) {
        this.stockDividend = stockDividend;
    }

    public String getRightIssue() {
        return rightIssue;
    }

    public void setRightIssue(String rightIssue) {
        this.rightIssue = rightIssue;
    }

    public String getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(String yearEnd) {
        this.yearEnd = yearEnd;
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

    public double getPaidUpCapital() {
        return paidUpCapital;
    }

    public void setPaidUpCapital(double paidUpCapital) {
        this.paidUpCapital = paidUpCapital;
    }

    public double getFaceParValue() {
        return faceParValue;
    }

    public void setFaceParValue(double faceParValue) {
        this.faceParValue = faceParValue;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
