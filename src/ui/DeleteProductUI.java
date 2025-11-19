package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeleteProductUI extends JFrame {

    private JTable table;

    public DeleteProductUI() {
        setTitle("Eliminar Productos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TÍTULO =====
        JLabel title = new JLabel("Eliminar Productos", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(title, BorderLayout.NORTH);

        // ===== TABLA =====
        String[] cols = {"Nombre", "Categoría", "Código", "Stock", "Precio", "Estado", "Eliminar"};
        Object[][] dataEjemplo = {
                {"Palomitas Grandes", "Alimentos", "PC001", 150, "$7.50", "Activo", "Eliminar"},
                {"Refresco Mediano", "Bebidas", "RF002", 250, "$4.00", "Activo", "Eliminar"},
                {"Nachos", "Alimentos", "NC003", 120, "$6.50", "Inactivo", "Eliminar"},
                {"Hot Dog", "Alimentos", "HD001", 25, "$5.00", "Activo", "Eliminar"}
        };

        DefaultTableModel model = new DefaultTableModel(dataEjemplo, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 6; // solo la columna eliminar
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);

        // Botones dentro de la tabla
        table.getColumn("Eliminar").setCellRenderer((t, v, sel, foc, r, c) -> {
            JButton btn = new JButton("Eliminar");
            btn.setBackground(new Color(230, 80, 80));
            btn.setForeground(Color.WHITE);
            return btn;
        });

        table.getColumn("Eliminar").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable t, Object value, boolean isSelected, int row, int col) {
                JButton btn = new JButton("Eliminar");
                btn.setBackground(new Color(230, 80, 80));
                btn.setForeground(Color.WHITE);

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

        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new DeleteProductUI();
    }
}
