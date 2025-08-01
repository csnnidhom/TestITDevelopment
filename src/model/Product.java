/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dhom
 */
public class Product {
    private String productCode;
    private String productName;
    private double price;
    private int stock;

    @Override
    public String toString() {
        return productCode;
    }

    public Product(String productCode, String productName, double price, int stock) {
        this.productCode = productCode;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
    }
    

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
