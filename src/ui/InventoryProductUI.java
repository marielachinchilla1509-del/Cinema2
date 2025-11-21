package ui;

import proyecto1.cinema.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryProductUI extends JFrame {

    public InventoryProductUI() {
        setTitle("Product Inventory");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] cols = {"Name", "Category", "Description", "Code", "Stock", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);

        for (Product p : ProductStorage.getProducts()) {
            model.addRow(new Object[]{
                p.getProductName(), p.getCategory(), p.getDescription(), p.getCode(),
                p.getStock(), p.getPrice(), p.getStatus()
            });
        }

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                ProductStorage.getProducts().remove(row);
                model.removeRow(row);
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnDelete, BorderLayout.SOUTH);
    }
}
