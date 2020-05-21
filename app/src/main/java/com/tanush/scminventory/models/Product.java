package com.tanush.scminventory.models;

public class Product {
    String prodID;
    int prodPrice;
    String prodName;

    public Product() {
    }

    public Product(String prodID, int prodPrice, String prodName) {
        this.prodID = prodID;
        this.prodPrice = prodPrice;
        this.prodName = prodName;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public int getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(int prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
}
