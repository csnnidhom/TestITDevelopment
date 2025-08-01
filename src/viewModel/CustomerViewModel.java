/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewModel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Customer;

/**
 *
 * @author Dhom
 */
public class CustomerViewModel extends AbstractTableModel{

    private final String[] columns = {"Kode", "Nama", "Alamat", "Provinsi", "Kota", "Kecamatan", "Kelurahan", "Kode Pos"};
    private List<Customer> data;

    public CustomerViewModel(List<Customer> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Customer c = data.get(row);
        switch(col){
            case 0 : return c.getCustomerCode();
            case 1 : return c.getCustomerName();
            case 2 : return c.getAddress();
            case 3 : return c.getProvince();
            case 4 : return c.getCity();
            case 5 : return c.getSubdistric();
            case 6 : return c.getVillage();
            case 7 : return c.getPostalcode();
            default : return null;
        }
    }
    
    @Override
    public String getColumnName(int col) {
        return columns[col];  
    }
    
    public void setData(List<Customer> data){
        this.data = data;
        fireTableDataChanged();
    }
    
}
