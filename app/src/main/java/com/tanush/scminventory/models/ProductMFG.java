package com.tanush.scminventory.models;

public class ProductMFG {

    String prodID;
    int docID;
    String day, month, year;
    int stock;

    public ProductMFG() {
    }

    public ProductMFG(String prodID, int docID, String day, String month, String year, int stock) {
        this.prodID = prodID;
        this.docID = docID;
        this.day = day;
        this.month = month;
        this.year = year;
        this.stock = stock;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
