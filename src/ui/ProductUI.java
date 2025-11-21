package ui;

import javax.swing.*;
import java.awt.*;

public class ProductUI extends JFrame {

    public ProductUI() {
        setTitle("Product Menu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton btnRegister = new JButton("Register Product");
        JButton btnInventory = new JButton("Inventory");
        JButton btnLoad = new JButton("Load TXT");
        JButton btnSave = new JButton("Save TXT");

        btnRegister.addActionListener(e -> new RegisterProductUI().setVisible(true));
        btnInventory.addActionListener(e -> new InventoryProductUI().setVisible(true));
        btnLoad.addActionListener(e -> {
            ProductStorage.loadFromFile("products.txt");
            JOptionPane.showMessageDialog(this, "Products loaded.");
        });
        btnSave.addActionListener(e -> {
            ProductStorage.saveToFile("products.txt");
            JOptionPane.showMessageDialog(this, "Products saved.");
        });

        setLayout(new GridLayout(4, 1, 10, 10));
        add(btnRegister);
        add(btnInventory);
        add(btnLoad);
        add(btnSave);
    }
}
