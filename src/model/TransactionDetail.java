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
public class TransactionDetail {
    private int detailId;
    private String invoiceNo;
    private String productCode;
    private int qty;
    private double price;
    private double disc1;
    private double disc2;
    private double disc3;
    private double netPrice;
    private double amount;

    public TransactionDetail(String invoiceNo, String productCode, int qty, double price, double disc1, double disc2, double disc3, double netPrice, double amount) {
        this.invoiceNo = invoiceNo;
        this.productCode = productCode;
        this.qty = qty;
        this.price = price;
        this.disc1 = disc1;
        this.disc2 = disc2;
        this.disc3 = disc3;
        this.netPrice = netPrice;
        this.amount = amount;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDisc1() {
        return disc1;
    }

    public void setDisc1(double disc1) {
        this.disc1 = disc1;
    }

    public double getDisc2() {
        return disc2;
    }

    public void setDisc2(double disc2) {
        this.disc2 = disc2;
    }

    public double getDisc3() {
        return disc3;
    }

    public void setDisc3(double disc3) {
        this.disc3 = disc3;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = netPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    
    
}
