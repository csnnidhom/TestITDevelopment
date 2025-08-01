/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dhom
 */
public class ConnectDB {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DB_Inventory;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    
    public static Connection getConnection(){
        Connection conn = null;
        
        try{
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e){
            System.err.println("Connection Failed : " + e.getMessage());
        }
        
        return conn;
    }
    
    public static void createTable(){
        Connection connection = ConnectDB.getConnection();
        Statement stmt = null;
        
        tableProduct(connection, stmt);
        tableCustomer(connection, stmt);
        tableTransactionHeader(connection, stmt);
        tableTransactionDetail(connection, stmt);
    }

    private static void tableProduct(Connection connection, Statement stmt) {
            try {
                String nameTable = "Product";
                String checkTableSQL = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + nameTable + "' ";			

                String createTable ="CREATE TABLE Product (" +
                                    "product_code VARCHAR(10) PRIMARY KEY," +
                                    "product_name VARCHAR(50) NOT NULL," +
                                    "price DECIMAL(18,2) NOT NULL," +
                                    "stock INT NOT NULL" +
                                    ")";

                tryCheck(connection, stmt, checkTableSQL, createTable, nameTable);
        }catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
        }
    }

    private static void tableCustomer(Connection connection, Statement stmt) {
        try {
                String nameTable = "Customer";
                String checkTableSQL = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + nameTable + "' ";			

                String createTable ="CREATE TABLE Customer (" +
                                    "customer_code VARCHAR(10) PRIMARY KEY," +
                                    "customer_name VARCHAR(50) NOT NULL," +
                                    "address NVARCHAR(255)," +
                                    "province NVARCHAR(50)," +
                                    "city NVARCHAR(50)," +
                                    "subdistric NVARCHAR(50)," +
                                    "village NVARCHAR(50)," +
                                    "postal_code INT" +
                                    ")";

                tryCheck(connection, stmt, checkTableSQL, createTable, nameTable);
        }catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
        }
    }

    private static void tableTransactionHeader(Connection connection, Statement stmt) {
        try {
                String nameTable = "TransactionHeader";
                String checkTableSQL = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + nameTable + "' ";			

                String createTable ="CREATE TABLE TransactionHeader (" +
                                    "invoice_no VARCHAR(20) PRIMARY KEY,\n" +
                                    "customer_code VARCHAR(10) FOREIGN KEY REFERENCES Customer(customer_code),\n" +
                                    "invoice_date DATE NOT NULL,\n" +
                                    "total DECIMAL(18,2) NOT NULL" +
                                    ")";

                tryCheck(connection, stmt, checkTableSQL, createTable, nameTable);
        }catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
        }
    }

    private static void tableTransactionDetail(Connection connection, Statement stmt) {
        try {
                String nameTable = "TransactionDetail";
                String checkTableSQL = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + nameTable + "' ";			

                String createTable ="CREATE TABLE TransactionDetail (" +
                                    "detail_id INT IDENTITY(1,1) PRIMARY KEY,\n" +
                                    "invoice_no VARCHAR(20) FOREIGN KEY REFERENCES TransactionHeader(invoice_no),\n" +
                                    "product_code VARCHAR(10) FOREIGN KEY REFERENCES Product(product_code),\n" +
                                    "qty INT NOT NULL,\n" +
                                    "price DECIMAL(18,2) NOT NULL,\n" +
                                    "disc1 DECIMAL(5,2) DEFAULT 0,\n" +
                                    "disc2 DECIMAL(5,2) DEFAULT 0,\n" +
                                    "disc3 DECIMAL(5,2) DEFAULT 0,\n" +
                                    "net_price AS (price*(1-disc1/100)*(1-disc2/100)*(1-disc3/100)),\n" +
                                    "amount AS (qty*price*(1-disc1/100)*(1-disc2/100)*(1-disc3/100))\n" +
                                    ")";

                tryCheck(connection, stmt, checkTableSQL, createTable, nameTable);
        }catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
        }
    }
    
    private static void tryCheck(Connection connection, Statement stmt, String checkTableSQL, String createTable, String nameTable){
            try {
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(checkTableSQL);
                if(!rs.next()) {
                        stmt.execute(createTable);
                        System.out.println("Success create " + nameTable +  "TransactionDetail");
                }else {
                        System.out.println(nameTable + " table sudah ada");
                }
            }catch (Exception exception) {
                System.out.println("Error query : " + exception.getMessage());
            }
    }

}
