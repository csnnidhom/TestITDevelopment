/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Dao.ProductDao;
import java.util.List;
import javax.swing.JOptionPane;
import model.Product;
import view.DashboardView;
import view.ProductView;
import viewModel.ProductViewModel;

/**
 *
 * @author Dhom
 */
public class ProductController {
    private final ProductView view;
    private final ProductDao dao;
    private final ProductViewModel tableModel;

    public ProductController(ProductView view, ProductDao dao, ProductViewModel tableModel) {
        this.view = view;
        this.dao = dao;
        this.tableModel = tableModel;
    }
    
    public void initActions(){
        view.getBtnSaveOrUpdate().addActionListener(e -> saveOrUpdateProduct());
        view.getBtnDelete().addActionListener(e -> deleteProduct());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnBack().addActionListener(e -> btnBack());
        
        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.getTable().getSelectedRow() != -1) {
                int row = view.getTable().getSelectedRow();
                view.getTxtCode().setText(view.getTable().getValueAt(row, 0).toString());
                view.getTxtName().setText(view.getTable().getValueAt(row, 1).toString());
                view.getTxtPrice().setText(view.getTable().getValueAt(row, 2).toString());
                view.getTxtStock().setText(view.getTable().getValueAt(row, 3).toString());

                view.getTxtCode().setEditable(false);
                view.getBtnSaveOrUpdate().setText("Update");
            }
        });
    }

    private boolean saveOrUpdateProduct() {
        String code = view.getTxtCode().getText();
        String name = view.getTxtName().getText();
        String priceText = view.getTxtPrice().getText();
        String stockText = view.getTxtStock().getText();
        
        try{
            if(code.isEmpty() || name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() ){
                JOptionPane.showMessageDialog(view, "Form tidak boleh kosong");
                return false;
            }else if(!isNumerik(priceText, stockText)){
                JOptionPane.showMessageDialog(view, "Harga/Stockharus berupa angka!");
                return false;
            }else if(!code.matches("^[a-zA-Z0-9]+$")){
                JOptionPane.showMessageDialog(view, "Kode produck hanya dapat berupa karakter alfanumerik & tidak boleh berupa karakter khusus!");
                return false;
            }else{
                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);
        
                Product p = new Product(code, name, price, stock);
                if(view.getTxtCode().isEditable()){
                    if(dao.findByCode(code) != null){
                        JOptionPane.showMessageDialog(view, "Produk sudah ada");
                        return false;
                    } 
                    dao.insert(p);
                    JOptionPane.showMessageDialog(view, "Berhasil menyimpan");
                    clearForm();
                } else{
                    dao.update(p);
                    JOptionPane.showMessageDialog(view, "Update produk berhasil!");
                    clearForm();
                }

                refreshTable();
                return true;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(view, "Gagal menyimpan!");
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
                JOptionPane.showMessageDialog(view, "Gagal, product ini sudah di pakai di transaksi!");
            }else{
                JOptionPane.showMessageDialog(view, "Berhasil menghapus product");
                clearForm();
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        List<Product> list = dao.getAll();
        tableModel.setData(list);
    }

    private boolean isNumerik(String price, String stock) {
        try {
            Double.parseDouble(price);
            Integer.parseInt(stock);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearForm() {
        view.getTxtCode().setText("");
        view.getTxtName().setText("");
        view.getTxtPrice().setText("");
        view.getTxtStock().setText("");
        view.getTxtCode().setEditable(true);
        view.getBtnSaveOrUpdate().setText("Save");
        view.getTable().clearSelection();
    }

    private void btnBack() {
        view.dispose();
        
        DashboardView dashboard = new DashboardView();
        dashboard.setVisible(true);
        dashboard.setLocationRelativeTo(null);
    }
    
}
