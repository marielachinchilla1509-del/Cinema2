package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * ProductEditUI (UI-only)
 * Versión estética simplificada: layout y estilos similares al HTML que pasaste.
 * No tiene integración con modelos ni almacenamiento — solo apariencia y placeholders.
 */
public class EditProductUI extends JFrame {

    // Campos UI (visibles para que luego agregues lógica)
    private final JTextField txtSearch = new JTextField();
    private final JTextField txtCode = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JComboBox<String> comboCategory = new JComboBox<>(new String[]{"Bebidas", "Snacks", "Combos", "Dulces"});
    private final JFormattedTextField txtPrice = new JFormattedTextField();
    private final JFormattedTextField txtStock = new JFormattedTextField();
    private final JCheckBox chkActive = new JCheckBox("Activo");
    private final JLabel imgPreview = new JLabel();

    public EditProductUI() {
        setTitle("Gestión de Productos - Editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(980, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ================= HEADER =================
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // ================= MAIN =================
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(new EmptyBorder(16, 16, 16, 16));
        main.setBackground(Color.WHITE);

        // Top title + description
        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setOpaque(false);
        JLabel title = new JLabel("Gestión de Productos");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        topInfo.add(title, BorderLayout.WEST);
        JLabel subtitle = new JLabel("<html><span style='color:#7a4b4b;'>Ingrese código o nombre para buscar y editar</span></html>");
        subtitle.setBorder(new EmptyBorder(6, 0, 0, 0));
        topInfo.add(subtitle, BorderLayout.SOUTH);
        main.add(topInfo, BorderLayout.NORTH);

        // Search row
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        searchRow.setOpaque(false);
        txtSearch.setPreferredSize(new Dimension(460, 36));
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(0xEC1313));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(110, 36));
        // placeholder action
        btnSearch.addActionListener((ActionEvent e) ->
                JOptionPane.showMessageDialog(this, "Búsqueda no implementada (UI only).")
        );

        searchRow.add(new JLabel("\uD83D\uDD0D")); // search icon emoji
        searchRow.add(txtSearch);
        searchRow.add(btnSearch);
        main.add(searchRow, BorderLayout.CENTER);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(1, 8));
        main.add(sep, BorderLayout.AFTER_LAST_LINE);

        // Form area (left: fields, right: image + status)
        JPanel formArea = new JPanel(new GridBagLayout());
        formArea.setOpaque(false);
        formArea.setBorder(new EmptyBorder(12, 0, 0, 0));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Left column (fields)
        JPanel fields = new JPanel();
        fields.setLayout(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(8, 8, 8, 8);
        f.fill = GridBagConstraints.HORIZONTAL;
        f.gridx = 0; f.gridy = 0; f.weightx = 0;
        fields.add(new JLabel("Código"), f);
        f.gridx = 1; f.gridy = 0; f.weightx = 1;
        txtCode.setEditable(false);
        txtCode.setBackground(new Color(0xF3E7E7));
        fields.add(txtCode, f);

        f.gridx = 0; f.gridy = 1; f.weightx = 0;
        fields.add(new JLabel("Nombre del Producto"), f);
        f.gridx = 1; f.gridy = 1; f.weightx = 1;
        fields.add(txtName, f);

        f.gridx = 0; f.gridy = 2; f.weightx = 0;
        fields.add(new JLabel("Categoría"), f);
        f.gridx = 1; f.gridy = 2; f.weightx = 1;
        fields.add(comboCategory, f);

        f.gridx = 0; f.gridy = 3; f.weightx = 0;
        fields.add(new JLabel("Precio ($)"), f);
        f.gridx = 1; f.gridy = 3; f.weightx = 1;
        txtPrice.setValue(0.0);
        fields.add(txtPrice, f);

        f.gridx = 0; f.gridy = 4; f.weightx = 0;
        fields.add(new JLabel("Stock Actual"), f);
        f.gridx = 1; f.gridy = 4; f.weightx = 1;
        txtStock.setValue(0);
        fields.add(txtStock, f);

        // Place fields into formArea (spanning two columns)
        c.gridx = 0; c.gridy = 0; c.weightx = 0.7; c.gridwidth = 2;
        formArea.add(fields, c);

        // Right column (image + status)
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        imgPreview.setPreferredSize(new Dimension(200, 200));
        imgPreview.setOpaque(true);
        imgPreview.setBackground(new Color(0xF3E7E7));
        imgPreview.setHorizontalAlignment(SwingConstants.CENTER);
        imgPreview.setText("<html><center>Sin<br>imagen</center></html>");
        imgPreview.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));

        JButton btnChangeImg = new JButton("Cambiar Imagen");
        btnChangeImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChangeImg.setFocusPainted(false);
        // placeholder: no heavy logic
        btnChangeImg.addActionListener(e -> JOptionPane.showMessageDialog(this, "Cambiar imagen (no implementado)"));

        chkActive.setSelected(true);
        chkActive.setAlignmentX(Component.CENTER_ALIGNMENT);

        right.add(imgPreview);
        right.add(Box.createVerticalStrut(12));
        right.add(btnChangeImg);
        right.add(Box.createVerticalStrut(12));
        right.add(chkActive);

        c.gridx = 2; c.gridy = 0; c.weightx = 0.3; c.gridwidth = 1;
        formArea.add(right, c);

        main.add(formArea, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);

        // ================= FOOTER ACTIONS =================
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(8, 8, 8, 8));

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton("Guardar Cambios");
        btnSave.setBackground(new Color(0xEC1313));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        // placeholder save
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Guardar (no implementado)"));

        footer.add(btnCancel);
        footer.add(btnSave);

        add(footer, BorderLayout.SOUTH);

        // tweak default fonts (optional, fallbacks used if not installed)
        applyFontDefaults();

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xF0E6E6)));
        header.setPreferredSize(new Dimension(10, 64));

        // left brand
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        left.setOpaque(false);
        JLabel logo = new JLabel("\uD83C\uDF7F");
        logo.setFont(new Font("SansSerif", Font.PLAIN, 28));
        JLabel lblApp = new JLabel("CinePOS");
        lblApp.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblApp.setForeground(new Color(0x1B0D0D));
        left.add(logo);
        left.add(lblApp);

        // right icons
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        right.setOpaque(false);
        String[] icons = {"\uD83D\uDC64", "\u2699", "\uD83D\uDEAA"}; // person, settings, logout
        for (String ic : icons) {
            JButton b = new JButton(ic);
            b.setPreferredSize(new Dimension(42, 36));
            b.setFocusPainted(false);
            b.setBackground(new Color(0xF3E7E7));
            right.add(b);
        }

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private void applyFontDefaults() {
        Font base = new Font("SansSerif", Font.PLAIN, 14);
        txtSearch.setFont(base);
        txtCode.setFont(base);
        txtName.setFont(base);
        comboCategory.setFont(base);
        txtPrice.setFont(base);
        txtStock.setFont(base);
        chkActive.setFont(new Font("SansSerif", Font.BOLD, 14));
    }

    // Test runner
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditProductUI::new);
    }
}
