/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import com.toedter.calendar.JDateChooser;
import connection.ConnectDB;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import model.TransactionDetail;
import model.TransactionHeader;

/**
 *
 * @author Dhom
 */
public class TransactionDao {
    private Connection conn = ConnectDB.getConnection();
    
    public boolean insertHeader(TransactionHeader header){
        String sqlHeader = "INSERT INTO TransactionHeader(invoice_no, customer_code, invoice_date, total) VALUES (?,?,?,?)";

        try(PreparedStatement ps = conn.prepareStatement(sqlHeader)){
           java.util.Date utilDate = header.getInvoiceDate();

           ps.setString(1, header.getInvoiceNo());
           ps.setString(2, header.getCustomerCode());
           ps.setDate(3, new java.sql.Date(utilDate.getTime()));
           ps.setDouble(4, header.getTotal());
           ps.executeUpdate();
           
           return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } 
    }
    
    public boolean insertTransactionDetails(TransactionDetail detail){
        String sqlDetail = "INSERT INTO TransactionDetail(invoice_no, product_code, qty, price, disc1, disc2, disc3) VALUES (?,?,?,?,?,?,?)";
        
        try(PreparedStatement ps = conn.prepareStatement(sqlDetail)){
            //insert detail
            ps.setString(1, detail.getInvoiceNo());
            ps.setString(2, detail.getProductCode());
            ps.setInt(3, detail.getQty());
            ps.setDouble(4, detail.getPrice());
            ps.setDouble(5, detail.getDisc1());
            ps.setDouble(6, detail.getDisc2());
            ps.setDouble(7, detail.getDisc3());
            ps.executeUpdate();
          
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }  
    }
    
    public void updateStock(String productCode, int qty){
        String sql = "UPDATE Product SET stock = stock - ? WHERE product_code=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setString(2, productCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<TransactionDetail> getAllTransactionDetail(){
        List<TransactionDetail> list = new ArrayList<>();
        String sql = "SELECT * From TransactionDetail";
        try(Statement st = conn.createStatement()){
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                list.add(new TransactionDetail(
                    rs.getString("invoice_no"),
                    rs.getString("product_code"),
                    rs.getInt("qty"),
                    rs.getDouble("price"),
                    rs.getDouble("disc1"),
                    rs.getDouble("disc2"),
                    rs.getDouble("disc3"),
                    rs.getDouble("net_price"),
                    rs.getDouble("amount")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return list;
    }
    
    public List<TransactionHeader> getAllTransactionHeader(){
        List<TransactionHeader> list = new ArrayList<>();
        String sql = "SELECT * FROM TransactionHeader";
        try(Statement st = conn.createStatement()){
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                list.add(new TransactionHeader(
                        rs.getString("invoice_no"), 
                        rs.getString("customer_code"), 
                        rs.getDate("invoice_date"),
                        rs.getDouble("total")
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return list;
    }
    
    public String generateInvoiceNumber(){
        String prefix = "INV";
        LocalDate now = LocalDate.now();
        String yearMount = String.format("%02d%02d", now.getYear() % 100, now.getMonthValue());
        
        String sql ="SELECT TOP 1 RIGHT(invoice_no,4) AS last_number FROM TransactionHeader " +
                    "WHERE invoice_no LIKE ? ORDER BY last_number DESC";
        
        String likePrefix = prefix + "/" + yearMount + "/%";
        int nextNumber = 1;
        
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, likePrefix);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nextNumber = rs.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return String.format("%s/%s/%04d", prefix,yearMount,nextNumber);
    }   
    
}
