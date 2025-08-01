/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewModel;

import Dao.CustomerDao;
import Dao.ProductDao;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import model.Customer;
import model.Product;
import model.TransactionDetail;
import model.TransactionHeader;

/**
 *
 * @author Dhom
 */
public class TransactionDetailViewModel extends AbstractTableModel{
    private List<TransactionHeader> dataTransactionHeaders;
    private List<TransactionDetail> dataTransactionDetail;
    private List<Product> dataProduct;
    private List<Customer> dataCustomer;
    private Customer currentCustomer;
    private String[] columns = {
        "No Invoice", "Customer", "Kode Produk", "Produk", "Qty", "Harga", 
        "Diskon 1", "Diskon 2", "Diskon 3", "Net Harga", "Jumlah"
    };

    public TransactionDetailViewModel(List<TransactionHeader> dataTransactionHeaders, List<TransactionDetail> dataTransactionDetail, List<Product> dataProduct, List<Customer> dataCustomer) {
        this.dataTransactionHeaders = dataTransactionHeaders;
        this.dataTransactionDetail = dataTransactionDetail;
        this.dataProduct = dataProduct;
        this.dataCustomer = dataCustomer;
    }
    
    @Override
    public int getRowCount() {
        return dataTransactionDetail == null ? 0 : dataTransactionDetail.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        TransactionDetail td = dataTransactionDetail.get(row);
        switch(column){
            case 0 : return td.getInvoiceNo();
            case 1 : {
                if (dataTransactionDetail != null && td.getInvoiceNo() != null) {
                    for (TransactionHeader th : dataTransactionHeaders){
                        if (td.getInvoiceNo().equals(th.getInvoiceNo())){
                            for (Customer c : dataCustomer){
                                if (th.getCustomerCode().equals(c.getCustomerCode())){
                                    return c.getCustomerName();
                                }
                            }
                        }
                    }
                }
                
                return currentCustomer != null ? currentCustomer.getCustomerName() : ""; 
            }
            case 2 : { return td.getProductCode(); }
            case 3 : {
                if (dataProduct != null && td.getProductCode() != null) {
                    for (Product p : dataProduct) {
                        if (td.getProductCode().equals(p.getProductCode())) {
                            return p.getProductName();
                        }
                    }
                }
                return null;
            }
            case 4 : return td.getQty();
            case 5 : return td.getPrice();
            case 6 : return td.getDisc1();
            case 7 : return td.getDisc2();            
            case 8 : return td.getDisc3();
            case 9 : return td.getNetPrice();
            case 10 : return td.getAmount();
            default : return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    public void setData(List<TransactionDetail> dataTransactionDetail){
        this.dataTransactionDetail = dataTransactionDetail;
        fireTableDataChanged();
    }

    public List<TransactionDetail> getDataTransactionDetail(){
        return dataTransactionDetail;
    }
    
}
