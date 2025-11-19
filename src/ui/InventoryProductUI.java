package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryProductUI extends JFrame {

    private JTable table;

    public InventoryProductUI() {
        setTitle("Inventario de Productos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TÍTULO =====
        JLabel title = new JLabel("Inventario de Productos", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(title, BorderLayout.NORTH);

        // ===== TABLA =====
        String[] cols = {"Nombre", "Categoría", "Código", "Stock", "Precio", "Estado"};
        Object[][] dataEjemplo = {
            {"Palomitas Grandes", "Alimentos", "PC001", 150, "$7.50", "Activo"},
            {"Refresco Mediano", "Bebidas", "RF002", 250, "$4.00", "Activo"},
            {"Nachos", "Alimentos", "NC003", 120, "$6.50", "Inactivo"},
            {"Hot Dog", "Alimentos", "HD001", 25, "$5.00", "Activo"}
        };

        DefaultTableModel model = new DefaultTableModel(dataEjemplo, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ===== BOTÓN NUEVO PRODUCTO =====
        JButton btnAddProduct = new JButton("Nuevo Producto");
        btnAddProduct.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnAddProduct.addActionListener(e -> new RegisterProductUI(this)); // abre ventana registro
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnAddProduct);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ===== MÉTODO PARA AGREGAR PRODUCTO NUEVO =====
    public void addProductToTable(Object[] productData) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(productData);
    }

    public static void main(String[] args) {
        new InventoryProductUI();
    }
}
