/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import connection.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Product;

/**
 *
 * @author Dhom
 */
public class CustomerDao {
    private Connection conn = ConnectDB.getConnection();
    
    public boolean insert(Customer c){
        String sql = "INSERT INTO Customer(customer_code, customer_name, address, province, city, subdistric, village, postal_code) VALUES (?,?,?,?,?,?,?,?)";
        
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, c.getCustomerCode());
            ps.setString(2, c.getCustomerName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getProvince());
            ps.setString(5, c.getCity());
            ps.setString(6, c.getSubdistric());
            ps.setString(7, c.getVillage());
            ps.setString(8, c.getPostalcode());
            ps.executeUpdate();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Customer> getAll(){
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * From Customer";
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("customer_code"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("province"),
                        rs.getString("city"),
                        rs.getString("subdistric"),
                        rs.getString("village"),
                        rs.getString("postal_code")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return list;
    }
    
    public boolean update(Customer c){
        String sql = "UPDATE Customer SET customer_name=?, address=?, province=?, city=?, subdistric=?, village=?, postal_code=? WHERE customer_code=?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getProvince());
            ps.setString(4, c.getCity());
            ps.setString(5, c.getSubdistric());
            ps.setString(6, c.getVillage());
            ps.setString(7, c.getPostalcode());
            ps.setString(8, c.getCustomerCode());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(String customerCode){
        String sql = "DELETE From Customer WHERE customer_code=?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, customerCode);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Cannot delete product, maybe it already exists in the transaction!");
            return false;
        }
    }
        
    public Customer findByCode(String customerCode){
        Customer customer = null;
        String sql = "Select * FROM Customer WHERE customer_code=?";

        try(PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, customerCode);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return new Customer(
                        rs.getString("customer_code"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("province"),
                        rs.getString("city"),
                        rs.getString("subdistric"),
                        rs.getString("village"),
                        rs.getString("postal_code")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return customer;
    }
}
