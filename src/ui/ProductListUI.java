package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.awt.Desktop;

public class ProductListUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JLabel counterLabel;
    private JTextField searchField;

    // Relative folder where invoices are stored
    private final String INVOICE_FOLDER = "invoices";

    // Constructor: sets up the product invoice list interface
    public ProductListUI() {
        setTitle("Product Invoice List"); // Window title
        setSize(700, 450); // Window size
        setLocationRelativeTo(null); // Center window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLayout(new BorderLayout()); // Main layout

        // ---------- TOP PANEL ----------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        counterLabel = new JLabel("Invoices: 0"); // Shows number of invoices
        counterLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topPanel.add(counterLabel, BorderLayout.WEST);

        // Search field panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        searchField = new JTextField(20); // Input for filtering invoices
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchButton = new JButton("Search"); // Button to filter invoices
        styleButton(searchButton); // Apply custom style

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH); // Add top panel to the frame

        // ---------- TABLE ----------
        model = new DefaultTableModel(new Object[]{"File Name", "Size (KB)"}, 0); // Table model
        table = new JTable(model);

        // Table appearance
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setAutoCreateRowSorter(true); // Enable sorting

        add(new JScrollPane(table), BorderLayout.CENTER); // Table with scroll

        // ---------- BOTTOM PANEL ----------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));

        JButton refreshButton = new JButton("Refresh"); // Button to reload invoices
        styleButton(refreshButton);

        JButton openButton = new JButton("Open Invoice"); // Button to open selected invoice
        styleButton(openButton);

        bottomPanel.add(refreshButton);
        bottomPanel.add(openButton);

        add(bottomPanel, BorderLayout.SOUTH); // Add bottom panel

        // ---------- EVENT LISTENERS ----------
        refreshButton.addActionListener(e -> loadInvoices()); // Reload invoices
        openButton.addActionListener(e -> openSelectedInvoice()); // Open selected invoice
        searchButton.addActionListener(e -> filterInvoices()); // Filter invoices by search

        loadInvoices(); // Load invoices when window opens
    }

    // ---------- BUTTON STYLE ----------
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(60, 120, 210)); // Blue background
        btn.setForeground(Color.WHITE); // White text
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
    }

    // ---------- LOAD PRODUCT INVOICES ----------
    private void loadInvoices() {
        model.setRowCount(0); // Clear table

        File folder = new File(INVOICE_FOLDER);
        if (!folder.exists()) folder.mkdirs(); // Create folder if it doesn't exist

        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".txt") &&
                name.toLowerCase().contains("invoice_product") // Only product invoices
        );

        if (files == null || files.length == 0) {
            counterLabel.setText("Invoices: 0"); // Update counter
            return;
        }

        // Add each invoice to the table
        for (File f : files) {
            long sizeKB = f.length() / 1024;
            model.addRow(new Object[]{f.getName(), sizeKB + " KB"});
        }

        counterLabel.setText("Invoices: " + files.length); // Update counter
    }

    // ---------- SEARCH INVOICE ----------
    private void filterInvoices() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        String text = searchField.getText().trim();

        if (text.isEmpty()) {
            sorter.setRowFilter(null); // Show all if search empty
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // Case-insensitive filter
        }
    }

    // ---------- OPEN SELECTED INVOICE ----------
    private void openSelectedInvoice() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select an invoice first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        row = table.convertRowIndexToModel(row); // Get correct index after sorting

        String fileName = (String) model.getValueAt(row, 0);
        File file = new File(INVOICE_FOLDER + File.separator + fileName);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this,
                    "File does not exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Desktop.getDesktop().open(file); // Open invoice with default app
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not open invoice.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductListUI().setVisible(true));
    }
}
