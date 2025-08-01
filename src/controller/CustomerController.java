/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Dao.CustomerDao;
import java.util.List;
import javax.swing.JOptionPane;
import model.Customer;
import view.CustomerView;
import view.DashboardView;
import viewModel.CustomerViewModel;

/**
 *
 * @author Dhom
 */
public class CustomerController {
    private final CustomerViewModel tableModel;
    private final CustomerDao dao;
    private final CustomerView view;

    public CustomerController(CustomerViewModel tableModel, CustomerDao dao, CustomerView view) {
        this.tableModel = tableModel;
        this.dao = dao;
        this.view = view;
    }
    
    public void initActions(){
        view.getBtnSaveOrUpdate().addActionListener(e -> saveOrUpdateCustomer());
        view.getBtnDelete().addActionListener(e -> deleteProduct());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> btnBack());
        
        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.getTable().getSelectedRow() != -1) {
                int row = view.getTable().getSelectedRow();
                view.getTextCodeCustomer().setText(view.getTable().getValueAt(row, 0).toString());
                view.getTextNameCustomer().setText(view.getTable().getValueAt(row, 1).toString());
                view.getTextAddress().setText(view.getTable().getValueAt(row, 2).toString());
                view.getTextProvince().setText(view.getTable().getValueAt(row, 3).toString());
                view.getTextCity().setText(view.getTable().getValueAt(row, 4).toString());
                view.getTextSubdistric().setText(view.getTable().getValueAt(row, 5).toString());
                view.getTextVillage().setText(view.getTable().getValueAt(row, 6).toString());
                view.getTextPostalcode().setText(view.getTable().getValueAt(row, 7).toString());

                view.getTextCodeCustomer().setEditable(false);
                view.getBtnSaveOrUpdate().setText("Update");
            }
        });
    }

    private boolean saveOrUpdateCustomer() {
        String codeCustomer = view.getTextCodeCustomer().getText();
        String nameCustomer = view.getTextNameCustomer().getText();
        String address = view.getTextAddress().getText();
        String province = view.getTextProvince().getText();
        String city = view.getTextCity().getText();
        String subdistric = view.getTextSubdistric().getText();
        String village = view.getTextVillage().getText();
        String postalcode = view.getTextPostalcode().getText();
        
        try{
            if(codeCustomer.isEmpty() || nameCustomer.isEmpty() || address.isEmpty() || province.isEmpty() || city.isEmpty() || subdistric.isEmpty() || village.isEmpty() || postalcode.isEmpty()){
                JOptionPane.showMessageDialog(view, "form tidak boleh kosong");
                return false;
            } else if(!codeCustomer.matches("^[a-zA-Z0-9]+$")){
                JOptionPane.showMessageDialog(view, "Kode Customer hanya dapat berupa karakter alfanumerik & tidak boleh berupa karakter khusus!");
                return false;
            } else if (!isNumerik(postalcode)){
                JOptionPane.showMessageDialog(view, "Kode pos harus berupa angka");
                return false;
            }else {
                Customer c = new Customer(codeCustomer, nameCustomer, address, province, city, subdistric, village, postalcode);
                
                if(view.getTextCodeCustomer().isEditable()){
                    if(dao.findByCode(codeCustomer) != null){
                        JOptionPane.showMessageDialog(view, "Customer sudah ada!");
                        return false;
                    }

                    dao.insert(c);
                    JOptionPane.showMessageDialog(view, "Berhasil menyimpan");
                    clearForm();
                }else{
                    dao.update(c);
                    JOptionPane.showMessageDialog(view,"Update customer berhasil");
                    clearForm();
                }
                
               refreshTable();
               return true;
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(view, "Gagal menyimpan");
            e.printStackTrace();
            return false;
        }
    }

    private void deleteProduct() {
        int row = view.getTable().getSelectedRow();
        if (row >= 0) {
            String code = (String) view.getTable().getValueAt(row, 0);
            boolean status = dao.delete(code);
            if(!status){
                JOptionPane.showMessageDialog(view, "Gagal, Customer ini sudah di pakai di transaksi!");
            }else{
                JOptionPane.showMessageDialog(view, "Berhasil menghapus customer");
                clearForm();
                refreshTable();
            }
        }
    }

    private void clearForm() {
        view.getTextCodeCustomer().setText("");
        view.getTextNameCustomer().setText("");
        view.getTextAddress().setText("");
        view.getTextProvince().setText("");
        view.getTextCity().setText("");
        view.getTextSubdistric().setText("");
        view.getTextVillage().setText("");
        view.getTextPostalcode().setText("");
        view.getTextCodeCustomer().setEditable(true);
        view.getBtnSaveOrUpdate().setText("Save");
        view.getTable().clearSelection();
    }

    private void refreshTable() {
        List<Customer> data = dao.getAll();
        tableModel.setData(data);
    }

    private boolean isNumerik(String postalcode) {
        try{
            Integer.parseInt(postalcode);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    private void btnBack() {
        view.dispose();
        
        DashboardView dashboard = new DashboardView();
        dashboard.setVisible(true);
        dashboard.setLocationRelativeTo(null);
    }
}
