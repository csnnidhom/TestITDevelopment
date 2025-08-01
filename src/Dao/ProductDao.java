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
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Product;

/**
 *
 * @author Dhom
 */
public class ProductDao {
    private Connection conn = ConnectDB.getConnection();
    
    public boolean insert(Product p){
        String sql ="INSERT INTO Product(product_code, product_name, price, stock) VALUES (?,?,?,?)";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, p.getProductCode());
            ps.setString(2, p.getProductName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Product> getAll(){
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * From Product";
        try(Statement st = conn.createStatement()){
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("product_code"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return list;
    }
    
    public boolean update(Product p){
        String sql = "UPDATE Product SET product_name=?, price=?, stock=? WHERE product_code=?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, p.getProductName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getProductCode());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(String productCode){
        String sql = "DELETE From Product WHERE product_code=?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, productCode);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Cannot delete product, maybe it already exists in the transaction!");
            return false;
        }
    }
    
    public Product findByCode(String code){
        String sql = "Select * FROM Product WHERE product_code=?";

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return new Product(
                    rs.getString("product_code"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public int getStock(String productCode) {
        int stock = 0;
        String sql = "SELECT stock FROM Product WHERE product_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stock = rs.getInt("stock");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stock;
    }
}
