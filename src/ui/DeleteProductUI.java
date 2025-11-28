package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeleteProductUI extends JFrame {

    private JTable table;

    // Constructor: sets up the delete products interface
    public DeleteProductUI() {
        setTitle("Eliminar Productos"); // Window title
        setSize(900, 600); // Window size
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Close only this window
        setLayout(new BorderLayout()); // Use BorderLayout for main layout

        // ===== TITLE =====
        JLabel title = new JLabel("Eliminar Productos", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 26)); // Title font
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16)); // Padding
        add(title, BorderLayout.NORTH); // Place title at the top

        // ===== TABLE =====
        String[] cols = {"Nombre", "Categoría", "Código", "Stock", "Precio", "Estado", "Eliminar"};
        Object[][] dataEjemplo = {
                {"Palomitas Grandes", "Alimentos", "PC001", 150, "$7.50", "Activo", "Eliminar"},
                {"Refresco Mediano", "Bebidas", "RF002", 250, "$4.00", "Activo", "Eliminar"},
                {"Nachos", "Alimentos", "NC003", 120, "$6.50", "Inactivo", "Eliminar"},
                {"Hot Dog", "Alimentos", "HD001", 25, "$5.00", "Activo", "Eliminar"}
        };

        // Table model allowing editing only in the "Eliminar" column
        DefaultTableModel model = new DefaultTableModel(dataEjemplo, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 6; // Only the delete column is editable
            }
        };

        table = new JTable(model);
        table.setRowHeight(32); // Row height for better appearance

        // Render delete button in the table
        table.getColumn("Eliminar").setCellRenderer((t, v, sel, foc, r, c) -> {
            JButton btn = new JButton("Eliminar");
            btn.setBackground(new Color(230, 80, 80)); // Red background
            btn.setForeground(Color.WHITE); // White text
            return btn;
        });

        // Editor for the delete button with action listener
        table.getColumn("Eliminar").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable t, Object value, boolean isSelected, int row, int col) {
                JButton btn = new JButton("Eliminar");
                btn.setBackground(new Color(230, 80, 80));
                btn.setForeground(Color.WHITE);

                // Ask for confirmation and remove the row if confirmed
                btn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            DeleteProductUI.this,
                            "¿Seguro que deseas eliminar este producto?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        ((DefaultTableModel) table.getModel()).removeRow(row);
                    }
                });

                return btn;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER); // Add table inside scroll pane

        setVisible(true); // Show the window
    }

    // Main method to launch the interface
    public static void main(String[] args) {
        new DeleteProductUI();
    }
}
