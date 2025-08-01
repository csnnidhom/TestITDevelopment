/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.CustomerView;
import view.DashboardView;
import view.ProductView;
import view.TransactionView;

/**
 *
 * @author Dhom
 */
public class DashboardController {
    private DashboardView view;

    public DashboardController(DashboardView view) {
        this.view = view;
    }


    public void initActions(){
        view.getBtnProduct().addActionListener(e -> btnPageProduct());
        view.getBtnCustomer().addActionListener(e -> btnPageCustomer());
        view.getBtnTransaction().addActionListener(e -> btnPageTransaction());
    }

    private void btnPageProduct() {
        view.dispose();
        ProductView pv = new ProductView();
        pv.setVisible(true);
        pv.setLocationRelativeTo(null);
    }

    private void btnPageCustomer() {
        view.dispose();
        CustomerView pv = new CustomerView();
        pv.setVisible(true);
        pv.setLocationRelativeTo(null);
    }

    private void btnPageTransaction() {
        view.dispose();
        TransactionView pv = new TransactionView();
        pv.setVisible(true);
        pv.setLocationRelativeTo(null);
    }
}
