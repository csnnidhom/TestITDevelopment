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
public class Customer {
    private String customerCode;
    private String customerName;
    private String address;
    private String province;
    private String city;
    private String subdistric;
    private String village;
    private String postalcode;

    @Override
    public String toString() {
        return customerCode;
    }

    public Customer(String customerCode, String customerName, String address, String province, String city, String subdistric, String village, String postalcode) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.address = address;
        this.province = province;
        this.city = city;
        this.subdistric = subdistric;
        this.village = village;
        this.postalcode = postalcode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubdistric() {
        return subdistric;
    }

    public void setSubdistric(String subdistric) {
        this.subdistric = subdistric;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    
}
