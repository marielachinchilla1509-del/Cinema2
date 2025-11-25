package ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ProductUI extends JFrame {

    // Se hace estática para que otras clases como SellProductUI puedan acceder a ella.
    private static ArrayList<Product> products = new ArrayList<>();

    public ProductUI() {
        // ... (Tu código original de ProductUI aquí, sin cambios en esta parte)
        setTitle("🛒 Product Module");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ==== COLORS ====
        Color red = new Color(139, 0, 0);
        Color navyBlue = new Color(10, 25, 60);
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // ==== TITLE ====
        JLabel title = new JLabel("🛒 Product Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ==== BUTTON PANEL ====
        // Cambiado el GridLayout para ajustarse a los 4 botones restantes
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 30, 30)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        buttonPanel.setBackground(white);

        JButton btnRegister = createBigButton("➕ Register Product", navyBlue, white);
        JButton btnInventory = createBigButton("📦 View Inventory", navyBlue, white);
        JButton btnSave = createBigButton("💾 Save Products", navyBlue, white);
        // ELIMINADO: JButton btnLoad = createBigButton("📁 Load Products", navyBlue, white); 
        
        //footer back to menu btn
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅️ Back to Menu");
        btnBack.setFont(new Font("Inter", Font.BOLD, 16));
        btnBack.setBackground(red);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose());

        backPanel.add(btnBack);
        add(backPanel, BorderLayout.SOUTH);
        
        // add buttons
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnInventory);
        buttonPanel.add(btnSave);
        // El cuarto botón que rellena el espacio dejado por 'Load'
        JButton btnProductList = createBigButton("🧾 View Product Invoices", navyBlue, white); 
        buttonPanel.add(btnProductList); 
        
        // Se añade un botón vacío para completar la cuadrícula si es necesario, o se ajusta la cuadrícula a 2x2. 
        // He optado por el 3x2 original y dejar el espacio vacío (se podría ajustar a 2x2 o 3x2).
        buttonPanel.add(new JPanel() {{ setBackground(white); }});

        add(buttonPanel, BorderLayout.CENTER);

        // ---- ACTIONS ----
        btnRegister.addActionListener(e -> new RegisterProductUI(products));
        // Se mantiene la creación de InventoryProductUI con la lista estática
        btnInventory.addActionListener(e -> new InventoryProductUI()); 
        btnSave.addActionListener(e -> saveProducts());
        // ELIMINADO: btnLoad.addActionListener(e -> loadProducts());
        // Lanza el nuevo ProductListUI
        btnProductList.addActionListener(e -> new ProductListUI().setVisible(true)); 

        setVisible(true);
    }

    // ======================================================================================
    // BUTTON DESIGN LIKE EMPLOYEE UI
    // ======================================================================================
    private JButton createBigButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 22));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        button.setPreferredSize(new Dimension(350, 120));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e)
            { button.setBackground(bg.darker()); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)
            { button.setBackground(bg); }
        });

        return button;
    }

    // ======================================================================================
    // PRODUCT CLASS
    // ======================================================================================
    public static class Product implements Serializable { 
        String name, category, desc, code, status;
        int stock;
        double price;

        public Product(String n, String c, String d, String co, int s, double p, String st) {
            this.name = n;
            this.category = c;
            this.desc = d;
            this.code = co;
            this.stock = s;
            this.price = p;
            this.status = st;
        }
        
        // Constructor alternativo para simplificar la creación desde SellProductUI
        public Product(String n, String co, double p) { 
            this.name = n;
            this.code = co;
            this.price = p;
            this.category = "Sales";
            this.desc = "Quick Sale Product";
            this.stock = 1;
            this.status = "Active";
        }
        
        // Getters para acceder a los datos
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getCode() { return code; }
        public int getStock() { return stock; }
        public double getPrice() { return price; }
        public String getStatus() { return status; }


        @Override
        public String toString() {
            // Se elimina 'desc' al guardar para que la carga no falle si no se usa
            return name + ";" + category + ";" + desc + ";" + code + ";" +
                    stock + ";" + price + ";" + status;
        }

        String getDesc() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        void setStock(int i) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }

    // ======================================================================================
    // REGISTER PRODUCT
    // ======================================================================================
    public class RegisterProductUI extends JFrame {

        private ArrayList<Product> products; 

        public RegisterProductUI(ArrayList<Product> products) {

            this.products = products;
            
            // ... (Tu código original de RegisterProductUI aquí)
            setTitle("🛒 Register Product");
            setSize(500, 650);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout(10, 10));
            getContentPane().setBackground(Color.WHITE);

            Color navy = new Color(0x001F3F);
            Color red = new Color(0xB22222);

            // ---------- HEADER ----------
            JLabel header = new JLabel("🛒 Register Product", SwingConstants.CENTER);
            header.setFont(new Font("Segoe UI", Font.BOLD, 26));
            header.setOpaque(true);
            header.setBackground(navy);
            header.setForeground(Color.WHITE);
            header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            add(header, BorderLayout.NORTH);

            // ---------- FORM ----------
            JPanel form = new JPanel(new GridLayout(8, 1, 10, 10));
            form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
            form.setBackground(Color.WHITE);

            JTextField txtName = createField("Name");
            JTextField txtDesc = createField("Description");
            JTextField txtCode = createField("Code");
            JTextField txtStock = createField("Stock");
            JTextField txtPrice = createField("Price");
            JTextField txtStatus = createField("Status");
            txtStatus.setText("Active");

            JComboBox<String> cbCategory = new JComboBox<>(new String[]{
                        "Food", "Snack", "Candy", "Soda"
            });
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

            // ---------- BUTTON ----------
            JButton btnSave = new JButton("💾 Save Product");
            btnSave.setFont(new Font("Segoe UI", Font.BOLD, 20));
            btnSave.setBackground(red);
            btnSave.setForeground(Color.WHITE);

            btnSave.addActionListener(e -> {

                try {
                    Product p = new Product(
                                txtName.getText(),
                                (String) cbCategory.getSelectedItem(),
                                txtDesc.getText(),
                                txtCode.getText(),
                                Integer.parseInt(txtStock.getText()),
                                Double.parseDouble(txtPrice.getText()),
                                txtStatus.getText()
                    );

                    products.add(p);

                    JOptionPane.showMessageDialog(this,
                            "✅ Product registered!");

                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "❌ Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            });

            JPanel bottom = new JPanel();
            bottom.setBackground(Color.WHITE);
            bottom.add(btnSave);
            add(bottom, BorderLayout.SOUTH);

            setVisible(true);
        }

        private JTextField createField(String placeholder) {
            JTextField tf = new JTextField();
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            tf.setBorder(BorderFactory.createTitledBorder(placeholder));
            return tf;
        }
    }

    // =========================================================================
    // INVENTORY SCREEN
    // Se ha cambiado para usar la lista estática
    // =========================================================================
    public class InventoryProductUI extends JFrame {

        public InventoryProductUI() {

            setTitle("📦 Product Inventory");
            setSize(750, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            // ======== TÍTULO =========
            JLabel title = new JLabel("📦 Product Inventory", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setOpaque(true);
            title.setBackground(new Color(10, 25, 60));
            title.setForeground(Color.WHITE);
            title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            add(title, BorderLayout.NORTH);

            // ======== COLUMNAS =========
            String[] columnas = {
                    "Name", "Category", "Code", "Stock", "Price", "Status", "Delete"
            };

            // ======== DATA =========
            // Ahora usa la lista estática products
            String[][] datos = new String[products.size()][7]; 

            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                datos[i][0] = p.name;
                datos[i][1] = p.category;
                datos[i][2] = p.code;
                datos[i][3] = String.valueOf(p.stock);
                datos[i][4] = "$" + p.price;
                datos[i][5] = p.status;
                datos[i][6] = "🗑️";  // Icono delete
            }

            JTable table = new JTable(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return col == 6; // Solo delete editable
                }
            };

            table.setFont(new Font("Inter", Font.PLAIN, 15));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(scroll, BorderLayout.CENTER);

            // ======== CLICK EN ELIMINAR =========
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {

                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (col == 6) { // eliminar

                        int confirm = JOptionPane.showConfirmDialog(
                                        null,
                                        "Delete this product?",
                                        "Confirm Delete",
                                        JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {

                            deleteProduct(row);  // ← FUNCIÓN DENTRO DEL MÓDULO
                            dispose();           // refrescar ventana
                            new InventoryProductUI();
                        }
                    }
                }
            });

            // ======== BOTÓN CERRAR =========
            JButton btnClose = new JButton("Close");
            btnClose.setFont(new Font("Inter", Font.BOLD, 18));
            btnClose.addActionListener(e -> dispose());

            JPanel bottom = new JPanel();
            bottom.add(btnClose);
            add(bottom, BorderLayout.SOUTH);

            setVisible(true);
        }

        // ===========================================================
        //    FUNCIÓN DELETE *DENTRO DEL INVENTORY*, IGUAL QUE CUSTOMER
        // ===========================================================
        private void deleteProduct(int index) {

            // 1. Eliminar del ArrayList
            products.remove(index);

            // 2. Guardar Cambios en TXT
            saveProductsToFile();
        }

        // ===========================================================
        //    GUARDAR TXT (REESCRIBE COMPLETO)
        // ===========================================================
        private void saveProductsToFile() {
            try (PrintWriter pw = new PrintWriter(new FileWriter("products.txt"))) {

                for (Product p : products) {
                    // El formato de guardado es diferente al toString(), se unifica al toString() para ser consistente
                    pw.println(p.toString());
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error saving products: " + e.getMessage());
            }
        }
    }


    // ======================================================================================
    // SAVE & AUTOMATIC LOAD (Modified)
    // ======================================================================================
    private void saveProducts() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("products.txt"))) {
            for (Product p : products) pw.println(p.toString());
            JOptionPane.showMessageDialog(this, "Products saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving file.");
        }
    }

    /**
     * Carga los productos desde el archivo products.txt.
     * Este método ya no es un Listener de botón, sino que se llama automáticamente.
     * @param productsList La lista estática de productos a rellenar.
     */
    public static void loadProductsFromFile(ArrayList<Product> productsList) {
        try (BufferedReader br = new BufferedReader(new FileReader("products.txt"))) {
            productsList.clear();
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                // Asegúrate que el formato de carga sea compatible con el toString() y constructor
                productsList.add(new Product(
                        p[0], p[1], p[2], p[3],
                        Integer.parseInt(p[4]),
                        Double.parseDouble(p[5]),
                        p[6]
                ));
            }
            // Eliminado el JOptionPane.showMessageDialog(this, "Products loaded!") para la carga automática
        } catch (FileNotFoundException e) {
            // No se muestra error si el archivo no existe (primera ejecución)
            System.out.println("products.txt not found. Starting with empty list.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading file: " + e.getMessage());
        }
    }
    
    // Método estático para que SellProductUI pueda acceder a la lista de productos
    public static ArrayList<Product> getProducts() {
        return products;
    }


    public static void main(String[] args) {
        // Carga los productos automáticamente antes de crear la interfaz
        loadProductsFromFile(products); 
        // Solo se crea una instancia del UI
        new ProductUI(); 
    }
}