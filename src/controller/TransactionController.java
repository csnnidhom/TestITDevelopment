/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Dao.CustomerDao;
import Dao.ProductDao;
import Dao.TransactionDao;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import model.Customer;
import model.Product;
import model.TransactionDetail;
import model.TransactionHeader;
import view.DashboardView;
import view.TransactionView;
import viewModel.TransactionDetailViewModel;

/**
 *
 * @author Dhom
 */
public class TransactionController {
    private final TransactionView view;
    private final TransactionDao transactionDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final List<TransactionDetail> detailList = new ArrayList<>();
    private TransactionDetailViewModel tableModel;
    boolean dataDb = false;

    public TransactionController(TransactionView view, TransactionDao transactionDao, ProductDao productDao, CustomerDao customerDao, TransactionDetailViewModel tableModel) {
        this.view = view;
        this.transactionDao = transactionDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.tableModel = tableModel;
    }
   
    public void initActions(){
        String newInvoiceNo = transactionDao.generateInvoiceNumber();
        view.getNoInvoice().setText(newInvoiceNo);
        
        setDate();
        
        view.getCmbCustomer().addActionListener(e -> {
            Customer selected = (Customer) view.getCmbCustomer().getSelectedItem();
            if(selected != null){
                Customer dataCustomer = customerDao.findByCode(selected.getCustomerCode());
                if(dataCustomer != null){
                    view.getCustomerName().setText(dataCustomer.getCustomerName());
                    view.getCustomerAddress().setText(dataCustomer.getAddress());
                } else {
                    view.getCustomerName().setText("");
                    view.getCustomerAddress().setText("");
                }
            }
        });
        
        view.getCmbProduct().addActionListener(e -> {
            Product selected = (Product) view.getCmbProduct().getSelectedItem();
            if(selected != null){
                Product dataProduct = productDao.findByCode(selected.getProductCode());
                if(dataProduct != null){
                    view.getProductName().setText(dataProduct.getProductName());
                    view.getPrice().setText(String.valueOf(dataProduct.getPrice()));
                }else{
                    view.getProductName().setText("");
                    view.getPrice().setText("");
                }
            }
        });
        
        view.getTableTransaction().getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting() && view.getTableTransaction().getSelectedRow() != -1){
                int row = view.getTableTransaction().getSelectedRow();
                        
                String productCode = view.getTableTransaction().getValueAt(row, 2).toString();

                for (int i = 0; i < view.getCmbProduct().getItemCount(); i++) {
                    Product item = (Product) view.getCmbProduct().getItemAt(i);
                    if(item.getProductCode().equals(productCode)){
                        view.getCmbProduct().setSelectedIndex(i);
                    } 
                }
                view.getProductName().setText(view.getTableTransaction().getValueAt(row, 3).toString());
                view.getQty().setText(view.getTableTransaction().getValueAt(row, 4).toString());
                view.getPrice().setText(view.getTableTransaction().getValueAt(row, 5).toString());
                view.getDisc1().setText(view.getTableTransaction().getValueAt(row, 6).toString());
                view.getDisc2().setText(view.getTableTransaction().getValueAt(row, 7).toString());
                view.getDisc3().setText(view.getTableTransaction().getValueAt(row, 8).toString());

                view.getBtnAddDetailTransaction().setText("Update Detail");
            }
        });
        
        view.getBtnAddDetailTransaction().addActionListener(e -> addDetailTransaction());
        view.getBtnSaveTransaction().addActionListener(e -> saveTransaction());
        view.getBtnDelete().addActionListener(e -> removeDetailTransaction());
        view.getBtnClear().addActionListener(e -> clearFormDetailTransaction());
        view.getBtnBack().addActionListener(e -> btnBack());
        
        refreshTable();
    }

    private boolean saveTransaction() {
        try{
            String noInvoice = view.getNoInvoice().getText();
            Product selectedProduct = (Product) view.getCmbProduct().getSelectedItem();
            Customer selectedCustomer = (Customer) view.getCmbCustomer().getSelectedItem();
            String productCode = selectedProduct.getProductCode();
            String customerCode = selectedCustomer.getCustomerCode();
            String customerName = view.getCustomerName().getText();
            String customerAddress = view.getCustomerAddress().getText();

            Date date = view.getInvoiceDate().getDate();
            double total = detailList.stream()
                    .mapToDouble(TransactionDetail::getAmount)
                    .sum();

            //save header
            TransactionHeader th = new TransactionHeader(noInvoice, customerCode, date, total);
            
            if (customerName.isEmpty() || customerAddress.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Pilih customer terlebih dahulu!");
                return false;
            }

            transactionDao.insertHeader(th);
            generateNoInvoice();


            //save detail transaction & update stock produck
            if(detailList.isEmpty()){
                JOptionPane.showMessageDialog(view, "Detail transaksi belum ada");
                return false;
            }else{
                for(TransactionDetail d : detailList){
                    transactionDao.insertTransactionDetails(d);
                    transactionDao.updateStock(productCode, d.getQty());
                }

                int confirm = JOptionPane.showConfirmDialog(view, "Apakah anda yakin menyimpan transaksi ini ?"
                        + "\nketika tersimpan data tidak bisa update/hapus",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION){
                    JOptionPane.showMessageDialog(view, "Transaksi berhasil dibuat");
                    detailList.clear();
                    clearFormHeaderTransaction();
                    clearFormDetailTransaction();
                    refreshTable();
                    dataDb=false;
                    return true;
                }
                
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        
    }

    private boolean addDetailTransaction() {
            String noInvoice = view.getNoInvoice().getText();
            Product selectedProduct = (Product) view.getCmbProduct().getSelectedItem();
            String productCode = selectedProduct.getProductCode();
            String productName = view.getProductName().getText();
            String qtyText = view.getQty().getText();
            int stock = productDao.getStock(productCode);
            String priceText = view.getPrice().getText();
            String d1Text = view.getDisc1().getText();
            String d2Text = view.getDisc2().getText();
            String d3Text = view.getDisc3().getText();
            
        try{ 
            if (productName.isEmpty() ) {
                JOptionPane.showMessageDialog(view, "Pilih produk terlebih dahulu!");
                return false;
            }else if (priceText.isEmpty() || qtyText.isEmpty() || d1Text.isEmpty() || d2Text.isEmpty() || d3Text.isEmpty()){
                JOptionPane.showMessageDialog(view, "form tidak boleh kosong!");
                return false;
            }else if(!isNumerik(priceText) || !isNumerik(d1Text) || !isNumerik(d2Text) || !isNumerik(d3Text) ){
                JOptionPane.showMessageDialog(view, "Price/Diskon1/Diskon2/Diskon3 harus berupa angka");
                return false;
            }else{
                int qty = Integer.parseInt(view.getQty().getText());
                if(qty > stock){
                    JOptionPane.showMessageDialog(view, "Qty melebihi stok!");
                    return false;
                }
                double price = Double.parseDouble(view.getPrice().getText());
                double d1 = Double.parseDouble(view.getDisc1().getText());
                double d2 = Double.parseDouble(view.getDisc2().getText());
                double d3 = Double.parseDouble(view.getDisc3().getText());

                double netPrice = price;
                netPrice -= netPrice * d1 / 100;
                netPrice -= netPrice * d2 / 100;
                netPrice -= netPrice * d3 / 100;

                double amount = netPrice * qty;

                TransactionDetail detail = new TransactionDetail(noInvoice, productCode, qty, price, d1, d2, d3, netPrice, amount);
                
                int selectedRow = view.getTableTransaction().getSelectedRow();
                if(selectedRow == -1){
                    detailList.add(detail);
                    tableModel.setData(detailList);
                    clearFormDetailTransaction();
                    columnCustomerName();
                    hiddencodeProduct();
                    dataDb=true;
                    return true;
                }else{
                    if(!dataDb){
                        JOptionPane.showMessageDialog(view, "maaf tidak bisa diupdate!");
                        return false;
                    }else{
                        //update di list data yg belum tersimpan di databasesout
                        tableModel.getDataTransactionDetail().set(selectedRow, detail);
                        view.getTableTransaction().clearSelection();
                        clearFormDetailTransaction();
                        return true;
                    }
                }

            }
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Gagal menambah detail!");
            return false;
        }
    }
    
    private void clearFormHeaderTransaction(){
        view.getCustomerName().setText("");
        view.getCustomerAddress().setText("");
    }

    private void clearFormDetailTransaction() {
        view.getProductName().setText("");
        view.getQty().setText("");
        view.getPrice().setText("");
        view.getDisc1().setText("");
        view.getDisc2().setText("");
        view.getDisc3().setText("");
        view.getNoInvoice().setEditable(false);
        view.getBtnAddDetailTransaction().setText("Simpan Detail");
        view.getTableTransaction().clearSelection();
    }
    
    public void refreshTable() {
        tableModel = new TransactionDetailViewModel(transactionDao.getAllTransactionHeader(), transactionDao.getAllTransactionDetail(), productDao.getAll(), customerDao.getAll());
        view.getTableTransaction().setModel(tableModel);
    }
    
    //load product code in combo box
    public void loadProductCode(JComboBox<Product> comboBox){
        comboBox.removeAllItems();
        List<Product> products = productDao.getAll();
        for(Product p : products){
            comboBox.addItem(p);
        }
    }
    
    //load customer
    public void loadCustomerCode(JComboBox<Customer> comboBox){
        comboBox.removeAllItems();
        List<Customer> customers = customerDao.getAll();
        for(Customer c : customers){
            comboBox.addItem(c);
        }
    }

    private void setDate() {
        Date date = new Date();
        view.getInvoiceDate().setDate(date);
    }

    private void generateNoInvoice() {
        view.getNoInvoice().setText("");
        String newInvoiceNo = transactionDao.generateInvoiceNumber();
        view.getNoInvoice().setText(newInvoiceNo);
    }

    private boolean removeDetailTransaction() {
        int selectedRow = view.getTableTransaction().getSelectedRow();
        
        if (selectedRow < 0){
            JOptionPane.showMessageDialog(view, "Pilih baris yang ingin dihapus!");
            return false;
        }
         
        if(!dataDb){
            JOptionPane.showMessageDialog(view, "maaf tidak bisa dihapus!");
            return false;
        }else{
            detailList.remove(selectedRow);
            tableModel.setData(detailList);
            JOptionPane.showMessageDialog(view, "Transaksi berhasil dihapus!");
            clearFormDetailTransaction();
            return true;
        }

    }

    private boolean isNumerik(String angka) {
        try{
            Double.parseDouble(angka);
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

    private void columnCustomerName() {
        TableColumn customerColumn = view.getTableTransaction().getColumnModel().getColumn(1);
        customerColumn.setMinWidth(0);
        customerColumn.setMaxWidth(0);
        customerColumn.setWidth(0);
    }
    
    private void hiddencodeProduct() {
        TableColumn codeProduct = view.getTableTransaction().getColumnModel().getColumn(2);
        codeProduct.setMinWidth(0);
        codeProduct.setMaxWidth(0);
        codeProduct.setWidth(0);
    }

}
