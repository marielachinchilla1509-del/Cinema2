package ui;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RegisterProductUI extends JFrame {

    private Object productModule;

    public RegisterProductUI() {
        setTitle("Register New Product");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load ProductModule dynamically
        try {
            Class<?> moduleCls = Class.forName("proyecto1.cinema.ProductModule");
            productModule = moduleCls.getConstructor().newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading ProductModule:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(15, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Register New Product", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(title);

        JLabel lblName = new JLabel("Product Name:");
        JTextField txtName = new JTextField();
        panel.add(lblName);
        panel.add(txtName);

        JLabel lblCategory = new JLabel("Category:");
        String[] categories = {"Drinks", "Popcorn", "Candy", "Combos"};
        JComboBox<String> cbCategory = new JComboBox<>(categories);
        panel.add(lblCategory);
        panel.add(cbCategory);

        JLabel lblDescription = new JLabel("Description:");
        JTextArea txtDescription = new JTextArea(3, 20);
        JScrollPane scroll = new JScrollPane(txtDescription);
        panel.add(lblDescription);
        panel.add(scroll);

        JLabel lblSKU = new JLabel("Product Code / SKU:");
        JTextField txtSKU = new JTextField();
        panel.add(lblSKU);
        panel.add(txtSKU);

        JLabel lblStock = new JLabel("Initial Stock:");
        JTextField txtStock = new JTextField();
        panel.add(lblStock);
        panel.add(txtStock);

        JLabel lblPrice = new JLabel("Sale Price:");
        JTextField txtPrice = new JTextField();
        panel.add(lblPrice);
        panel.add(txtPrice);

        JCheckBox cbStatus = new JCheckBox("Active");
        cbStatus.setSelected(true);
        panel.add(new JLabel("Status:"));
        panel.add(cbStatus);

        JButton btnCancel = new JButton("Cancel");
        JButton btnSave = new JButton("Save Product");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnCancel);
        buttons.add(btnSave);

        panel.add(new JLabel());
        panel.add(buttons);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e ->
                saveProduct(txtName, cbCategory, txtDescription, txtSKU, txtStock, txtPrice, cbStatus));

        add(panel);
        setVisible(true);
    }

    RegisterProductUI(InventoryProductUI aThis) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void saveProduct(JTextField txtName, JComboBox<String> cbCategory,
                             JTextArea txtDescription, JTextField txtSKU,
                             JTextField txtStock, JTextField txtPrice,
                             JCheckBox cbStatus) {

        String name = txtName.getText().trim();
        String category = cbCategory.getSelectedItem().toString();
        String description = txtDescription.getText().trim();
        String sku = txtSKU.getText().trim();
        String stockStr = txtStock.getText().trim();
        String priceStr = txtPrice.getText().trim();
        boolean active = cbStatus.isSelected();

        if (name.isEmpty() || sku.isEmpty() || stockStr.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all required fields.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int stock;
        double price;
        try {
            stock = Integer.parseInt(stockStr);
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Stock must be integer and price must be numeric.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = invokeRegisterProduct(name, category, description, sku, stock, price, active);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Product saved successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Could not register product: ProductModule signature does not match.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Reflection to call your ProductModule
    private boolean invokeRegisterProduct(String name, String category, String description,
                                         String sku, int stock, double price, boolean active) {

        Class<?> cls = productModule.getClass();

        // Try 7-arg
        try {
            Method m = cls.getMethod("registerProduct",
                    String.class, String.class, String.class,
                    String.class, int.class, double.class, boolean.class);

            m.invoke(productModule, name, category, description, sku, stock, price, active);
            return true;
        } catch (Exception ignored) {}

        // Try 6-arg
        try {
            Method m = cls.getMethod("registerProduct",
                    String.class, String.class, String.class,
                    String.class, int.class, double.class);

            m.invoke(productModule, name, category, description, sku, stock, price);
            return true;
        } catch (Exception ignored) {}

        // Try direct array assignment (products[] or product[])
        try {
            Field arrayField;

            // Try products[]
            try {
                arrayField = cls.getDeclaredField("products");
            } catch (Exception e) {
                // try product[]
                arrayField = cls.getDeclaredField("product");
            }

            arrayField.setAccessible(true);
            Object arr = arrayField.get(productModule);

            if (arr instanceof Object[]) {
                Object[] products = (Object[]) arr;

                int idx = -1;
                for (int i = 0; i < products.length; i++) {
                    if (products[i] == null) {
                        idx = i;
                        break;
                    }
                }
                if (idx == -1) return false;

                Class<?> pClass = Class.forName("proyecto1.cinema.Product");
                Object p = pClass.getConstructor().newInstance();

                // Compatible setters
                try { pClass.getMethod("setProductName", String.class).invoke(p, name); } catch (Exception ignored) {}
                try { pClass.getMethod("setName", String.class).invoke(p, name); } catch (Exception ignored) {}

                try { pClass.getMethod("setCategory", String.class).invoke(p, category); } catch (Exception ignored) {}

                try { pClass.getMethod("setDescription", String.class).invoke(p, description); } catch (Exception ignored) {}

                try { pClass.getMethod("setCode", String.class).invoke(p, sku); } catch (Exception ignored) {}
                try { pClass.getMethod("setSKU", String.class).invoke(p, sku); } catch (Exception ignored) {}

                try { pClass.getMethod("setStock", int.class).invoke(p, stock); } catch (Exception ignored) {}

                try { pClass.getMethod("setPrice", double.class).invoke(p, price); } catch (Exception ignored) {}

                try { pClass.getMethod("setStatus", boolean.class).invoke(p, active); } catch (Exception ignored) {}
                try { pClass.getMethod("setActive", boolean.class).invoke(p, active); } catch (Exception ignored) {}

                products[idx] = p;
                return true;
            }

        } catch (Exception ignored) {}

        return false;
    }

    public static void main(String[] args) {
        new RegisterProductUI();
    }
}
