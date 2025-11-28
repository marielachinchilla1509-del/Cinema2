package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.awt.Desktop;

/**
 * InvoiceListUI: A graphical interface for listing, searching, and opening
 * ticket invoices stored as text files.
 *
 * Features:
 * - Displays a table of invoice files (.txt) from the "invoice" folder.
 * - Shows file size in KB.
 * - Search/filter invoices by name.
 * - Refresh invoice list.
 * - Open selected invoice in the default system application.
 */
public class InvoiceListUI extends JFrame {

    /** JTable displaying invoice files */
    private JTable table;

    /** Table model for invoice data */
    private DefaultTableModel model;

    /** Label showing total number of invoices */
    private JLabel counterLabel;

    /** Search field to filter invoices by name */
    private JTextField searchField;

    /** Relative path to invoice folder inside the project */
    private final String INVOICE_PATH = "invoice";

    /**
     * Constructor: Sets up the UI, initializes components, and loads invoices.
     */
    public InvoiceListUI() {
        setTitle("Ticket Invoice List");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- TOP PANEL ----------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        counterLabel = new JLabel("Invoices: 0");
        counterLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topPanel.add(counterLabel, BorderLayout.WEST);

        // 🔍 Search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchButton = new JButton("Search");
        styleButton(searchButton);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new Object[]{"File Name", "Size (KB)"}, 0);
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(200, 220, 255));

        // 🔄 Column sorting enabled
        table.setAutoCreateRowSorter(true);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---------- BOTTOM PANEL ----------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton);

        JButton openButton = new JButton("Open Invoice");
        styleButton(openButton);

        bottomPanel.add(refreshButton);
        bottomPanel.add(openButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------- EVENT LISTENERS ----------
        refreshButton.addActionListener(e -> loadInvoices());
        openButton.addActionListener(e -> openSelectedInvoice());
        searchButton.addActionListener(e -> filterInvoices());

        // Load invoices at startup
        loadInvoices();
    }

    // -------------------------------------------
    // STYLE FOR ALL BUTTONS
    // -------------------------------------------
    /**
     * Applies consistent style to all buttons.
     *
     * @param btn JButton to style
     */
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(60, 120, 210));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // -------------------------------------------
    // LOAD ONLY TICKET INVOICES (.txt) FROM FOLDER
    // -------------------------------------------
    /**
     * Loads all invoice text files from the "invoice" folder
     * and populates the table with file name and size in KB.
     */
    private void loadInvoices() {
        model.setRowCount(0);

        // Create folder if it does not exist
        File folder = new File(INVOICE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Filter only .txt files excluding product invoices
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".txt") &&
                !name.toLowerCase().contains("invoice_product")
        );

        if (files == null || files.length == 0) {
            counterLabel.setText("Invoices: 0");
            return;
        }

        for (File f : files) {
            long sizeKB = f.length() / 1024;
            model.addRow(new Object[]{f.getName(), sizeKB + " KB"});
        }

        counterLabel.setText("Invoices: " + files.length);
    }

    // -------------------------------------------
    // SEARCH INVOICE BY NAME
    // -------------------------------------------
    /**
     * Filters the invoice table according to the search field text.
     */
    private void filterInvoices() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        String text = searchField.getText().trim();

        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    // -------------------------------------------
    // OPEN SELECTED .TXT INVOICE
    // -------------------------------------------
    /**
     * Opens the currently selected invoice file using the system default application.
     * Shows warning if no row is selected or error if file cannot be opened.
     */
    private void openSelectedInvoice() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select an invoice first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Adjust for sorter
        row = table.convertRowIndexToModel(row);

        String fileName = (String) model.getValueAt(row, 0);
        File file = new File(INVOICE_PATH + File.separator + fileName);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this,
                    "File does not exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not open invoice.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // -------------------------------------------
    // MAIN METHOD
    // -------------------------------------------
    /**
     * Main method to launch the InvoiceListUI.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InvoiceListUI().setVisible(true));
    }
}
