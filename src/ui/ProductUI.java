package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ProductUI extends JFrame {

    private static final String PRODUCT_FILE = "products.txt"; // File to store product info

    // *************************************************************************
    // --- MAIN CONSTRUCTOR (ProductUI) ---
    // *************************************************************************
    public ProductUI() {

        setTitle("🛒 Product Module"); // Window title
        setSize(1100, 750); // Bigger window for large buttons
        setLocationRelativeTo(null); // Center window
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Close only this window
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Define colors
        Color red = new Color(139, 0, 0);
        Color navyBlue = new Color(100, 185, 230);
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // Title label
        JLabel title = new JLabel("🛒 Product Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Panel for two central buttons
        JPanel twoButtonPanel = new JPanel(new GridLayout(1, 2, 50, 50)); // Increased gap
        twoButtonPanel.setBackground(white);

        // Create big buttons
        JButton btnRegister = createBigButton("➕ Register Product", navyBlue, white);
        JButton btnInventory = createBigButton("📦 View Inventory", navyBlue, white);

        twoButtonPanel.add(btnRegister);
        twoButtonPanel.add(btnInventory);

        // Center panel using GridBagLayout to center buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(white);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); // Vertical padding
        centerPanel.add(twoButtonPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Back button panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅ Back to Menu");
        btnBack.setFont(new Font("Inter", Font.BOLD, 16));
        btnBack.setBackground(red);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose()); // Close window

        backPanel.add(btnBack);
        add(backPanel, BorderLayout.SOUTH);

        // Button events to open other windows
        btnRegister.addActionListener(e -> new RegisterProductUI());
        btnInventory.addActionListener(e -> new InventoryProductUI());

        setVisible(true); // Show main UI
    }

    /**
 * Reads all products from the file.
 *
 * @return List of products read.
 */
    public static Vector<Product> readAllProductsFromFile() {
        Vector<Product> productsList = new Vector<>();
        File file = new File(PRODUCT_FILE);
        if (!file.exists()) {
            return productsList; // Return empty if file does not exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 7) {
                    productsList.add(new Product(
                            p[0], p[1], p[2], p[3],
                            Integer.parseInt(p[4]),
                            Double.parseDouble(p[5]),
                            p[6]
                    ));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading products from file:"
                    + " " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return productsList;
    }

    /**
     * Appends a single product to the file.
     */
    public static void appendProductToFile(Product p) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter
        (PRODUCT_FILE, true))) {
            bw.write(p.toString());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error appending product to file: "
                    + "" + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Rewrites the file with the given product list.
     */
    public static void writeAllProductsToFile(Vector<Product> productsList) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product p : productsList) {
                pw.println(p.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error rewriting product file: " 
                    + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // *************************************************************************
    // --- GETTERS and MAIN ---
    // *************************************************************************
    public static Vector<Product> getProducts() {
        return readAllProductsFromFile();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductUI()); // Launch main UI
    }

    // *************************************************************************
    // --- INNER CLASS: Product ---
    // *************************************************************************
    public static class Product implements Serializable {

        String name, category, desc, code, status;
        int stock;
        double price;

        public Product(String n, String c, String d, String co, int s, double p,
                String st) {
            this.name = n;
            this.category = c;
            this.desc = d;
            this.code = co;
            this.stock = s;
            this.price = p;
            this.status = st;
        }

        // Getters and setter for stock
        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public String getDesc() {
            return desc;
        }

        public String getCode() {
            return code;
        }

        public int getStock() {
            return stock;
        }

        public double getPrice() {
            return price;
        }

        public String getStatus() {
            return status;
        }

        public void setStock(int newStock) {
            this.stock = newStock;
        }

        @Override
        public String toString() {
            return name + ";" + category + ";" + desc + ";" + code + ";" 
                    + stock + ";" + price + ";" + status;
        }
    }

    // *************************************************************************
    // --- INNER CLASS: RegisterProductUI ---
    // *************************************************************************
    public class RegisterProductUI extends JFrame {

        public RegisterProductUI() {
            setTitle("🛒 Register Product");
            setSize(500, 650);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout(10, 10));
            getContentPane().setBackground(Color.WHITE);

            Color navy = new Color(0x001F3F);
            Color red = new Color(0xB22222);

            // Header label
            JLabel header = new JLabel("🛒 Register Product", SwingConstants.CENTER);
            header.setFont(new Font("Segoe UI", Font.BOLD, 26));
            header.setOpaque(true);
            header.setBackground(navy);
            header.setForeground(Color.WHITE);
            header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            add(header, BorderLayout.NORTH);

            // Form panel
            JPanel form = new JPanel(new GridLayout(8, 1, 10, 10));
            form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
            form.setBackground(Color.WHITE);

            // Input fields
            JTextField txtName = createField("Name");
            JTextField txtDesc = createField("Description");
            JTextField txtCode = createField("Code");
            JTextField txtStock = createField("Stock");
            JTextField txtPrice = createField("Price");
            JTextField txtStatus = createField("Status");
            txtStatus.setText("Active");
            txtStock.setText("0");
            txtPrice.setText("0.0");

            JComboBox<String> cbCategory = new JComboBox<>(new String[]{"Food", "Snack", "Candy", "Soda"});
            cbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            
            JPanel categoryPanel = new JPanel(new GridLayout(1, 1));
            categoryPanel.setBackground(Color.WHITE);
            categoryPanel.setBorder(BorderFactory.createTitledBorder("Category"));
            categoryPanel.add(cbCategory);

            form.add(txtName);
            form.add(categoryPanel);
            form.add(txtDesc);
            form.add(txtCode);
            form.add(txtStock);
            form.add(txtPrice);
            form.add(txtStatus);
            add(form, BorderLayout.CENTER);

            // Save button
            JButton btnSave = new JButton("💾 Save Product");
            btnSave.setFont(new Font("Segoe UI", Font.BOLD, 20));
            btnSave.setBackground(red);
            btnSave.setForeground(Color.WHITE);
            btnSave.addActionListener(e -> {
                if (txtName.getText().trim().isEmpty() || txtCode.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and Code are mandatory.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Product p = new Product(txtName.getText(), (String) cbCategory.getSelectedItem(), txtDesc.getText(), txtCode.getText(), Integer.parseInt(txtStock.getText()), Double.parseDouble(txtPrice.getText()), txtStatus.getText());
                    ProductUI.appendProductToFile(p);
                    JOptionPane.showMessageDialog(this, "✅ Product registered and saved!");
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "❌ Error: Stock and Price must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel bottom = new JPanel();
            bottom.setBackground(Color.WHITE);
            bottom.add(btnSave);
            add(bottom, BorderLayout.SOUTH);

            setVisible(true);
        }

        // Helper method for input fields
        private JTextField createField(String placeholder) {
            JTextField tf = new JTextField();
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            tf.setBorder(BorderFactory.createTitledBorder(placeholder));
            return tf;
        }
    }

    // *************************************************************************
    // --- INNER CLASS: InventoryProductUI ---
    // *************************************************************************
    public class InventoryProductUI extends JFrame {

        public InventoryProductUI() {
            setTitle("📦 Product Inventory");
            setSize(750, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            // Header
            JLabel title = new JLabel("📦 Product Inventory", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setOpaque(true);
            title.setBackground(new Color(10, 25, 60));
            title.setForeground(Color.WHITE);
            title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            add(title, BorderLayout.NORTH);

            // Table setup
            String[] columnas = {"Name", "Category", "Code", "Stock", "Price", "Status", "Delete"};
            Vector<Product> products = ProductUI.readAllProductsFromFile();
            DefaultTableModel model = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return col == 6;
                } // Only delete column editable
            };
            for (Product p : products) {
                model.addRow(new Object[]{p.name, p.category, p.code, p.stock, "$" + String.format("%.2f", p.price), p.status, "🗑"});
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Inter", Font.PLAIN, 15));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(scroll, BorderLayout.CENTER);

            // Delete product on click
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && col == 6) { // Delete column
                        String productName = (String) table.getValueAt(row, 0);
                        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete product: " + productName + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteProduct(products, row);
                            dispose();
                            new InventoryProductUI(); // Refresh table
                        }
                    }
                }
            });

            JButton btnClose = new JButton("Close");
            btnClose.setFont(new Font("Inter", Font.BOLD, 18));
            btnClose.addActionListener(e -> dispose());

            JPanel bottom = new JPanel();
            bottom.add(btnClose);
            add(bottom, BorderLayout.SOUTH);

            setVisible(true);
        }

        // Deletes product from vector and rewrites file
        private void deleteProduct(Vector<Product> currentProducts, int index) {
            currentProducts.remove(index);
            ProductUI.writeAllProductsToFile(currentProducts);
        }
    }

    // *************************************************************************
    // --- INNER CLASS: ProductListUI (Placeholder) ---
    // *************************************************************************
    public class ProductListUI extends JFrame {

        public ProductListUI() {
            setTitle("🧾 Product Invoices List");
            setSize(500, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            JLabel label = new JLabel("Invoices list module is not implemented yet.", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            add(label, BorderLayout.CENTER);
        }
    }

    // *************************************************************************
    // --- HELPER: Create Big Button ---
    // *************************************************************************
    private JButton createBigButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 24)); // Font size
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        button.setPreferredSize(new Dimension(400, 180)); // Large size
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bg);
            }
        });
        return button;
    }
}