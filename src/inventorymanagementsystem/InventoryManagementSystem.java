/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import view.DashboardView;
import connection.ConnectDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.DashboardView;

/**
 *
 * @author Dhom
 */
public class InventoryManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Java Version " + System.getProperty("java.version"));
        
        try {
            ConnectDB.createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardView().setVisible(true);
            }
        });
        
    }
    
}
