/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.toedter.calendar.JDateChooser;
import java.util.Date;

/**
 *
 * @author Dhom
 */
public class TransactionHeader {
    private String invoiceNo;
    private String customerCode;
    private Date invoiceDate;
    private Double total;

    public TransactionHeader(String invoiceNo, String customerCode, Date invoiceDate, Double total) {
        this.invoiceNo = invoiceNo;
        this.customerCode = customerCode;
        this.invoiceDate = invoiceDate;
        this.total = total;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    
    
    
}
