/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewModel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Product;


/**
 *
 * @author Dhom
 */
public class ProductViewModel extends AbstractTableModel{
    private final String[] columns = {"Kode", "Nama", "Harga", "Stok"};
    private List<Product> data;
    
    public ProductViewModel(List<Product> data){
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
        Product p = data.get(row);
        switch (col){
            case 0 : return p.getProductCode();
            case 1 : return p.getProductName();
            case 2 : return p.getPrice();
            case 3 : return p.getStock();
            default : return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }
    
    public void setData(List<Product> data){
        this.data = data;
        fireTableDataChanged();
    }
    
    
    
}
